package frontend;

import backend.CanvasState;
import backend.model.*;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class PaintPane extends BorderPane {

	// BackEnd
	CanvasState canvasState;

	// Canvas: for figure drawing graphics
	Canvas canvas = new Canvas(800, 600);
	// Canvas' context
	GraphicsContext gc = canvas.getGraphicsContext2D();
	Color lineColor = Color.BLACK;
	Color fillColor = Color.YELLOW;

	// Botones Barra Izquierda
	ToggleButton selectionButton = new ToggleButton("Seleccionar");
	ToggleButton lineButton = new ToggleButton("Línea");
	ToggleButton rectangleButton = new ToggleButton("Rectángulo");
	ToggleButton squareButton = new ToggleButton("Cuadrado");
	ToggleButton circleButton = new ToggleButton("Círculo");
	ToggleButton ellipseButton = new ToggleButton("Elipse");

	// Dibujar una figura
	MovablePoint startPoint;

	// Seleccionar una figura movible
	MovableFigure selectedFigure;

	// StatusBar
	StatusPane statusPane;

	public PaintPane(CanvasState canvasState, StatusPane statusPane) {
		this.canvasState = canvasState;
		this.statusPane = statusPane;

		// Components group (buttons)
		ToggleGroup tools = new ToggleGroup();
		ToggleButton[] toolsArr = {selectionButton, lineButton, rectangleButton, squareButton, ellipseButton, circleButton};
		for (ToggleButton tool : toolsArr) {
			tool.setMinWidth(90);
			tool.setToggleGroup(tools);
			tool.setCursor(Cursor.HAND);
		}

		// Button container
		VBox buttonsBox = new VBox(10);
		buttonsBox.getChildren().addAll(toolsArr);
		buttonsBox.setPadding(new Insets(5));
		buttonsBox.setStyle("-fx-background-color: #999");
		buttonsBox.setPrefWidth(100);
		gc.setLineWidth(1);

		canvas.setOnMousePressed(event -> {
			startPoint = new MovablePoint(event.getX(), event.getY());
		});

		canvas.setOnMouseReleased(event -> {
			MovablePoint endPoint = new MovablePoint(event.getX(), event.getY());
			if(startPoint == null) {
				return ;
			}
			if(endPoint.getX() < startPoint.getX() || endPoint.getY() < startPoint.getY()) {
				return ;
			}

			MovableFigure newFigure = null;
			if(rectangleButton.isSelected()) {
				newFigure = new MovableRectangle(startPoint, endPoint);
			}
			else if(squareButton.isSelected()) {
				newFigure = new MovableSquare(startPoint, endPoint);
			}
			else if(ellipseButton.isSelected()) {
				double horizontalDistance = Math.abs(endPoint.getX() - startPoint.getX());
				double verticalDistance = Math.abs(endPoint.getY() - startPoint.getY());
				newFigure = new MovableEllipse(startPoint, horizontalDistance, verticalDistance);
			}
			else if(circleButton.isSelected()) {
				double circleRadius = Math.abs(endPoint.getX() - startPoint.getX());
				newFigure = new MovableCircle(startPoint, circleRadius);
			}
			else if(lineButton.isSelected()) {
				newFigure = new MovableLine(startPoint, endPoint);
			} else {
				return ;
			}
			canvasState.addFigure((Figure)newFigure);
			startPoint = null;
			redrawCanvas();
		});

		canvas.setOnMouseMoved(event -> {
			Point eventPoint = new Point(event.getX(), event.getY());
			boolean found = false;
			StringBuilder label = new StringBuilder();
			for(Figure figure : canvasState.figures()) {
				if(figureBelongs(figure, eventPoint)) {
					found = true;
					label.append(figure.toString());
				}
			}
			if(found) {
				statusPane.updateStatus(label.toString());
			} else {
				statusPane.updateStatus(eventPoint.toString());
			}
		});

		canvas.setOnMouseClicked(event -> {
			if(selectionButton.isSelected()) {
				Point eventPoint = new Point(event.getX(), event.getY());
				boolean found = false;
				StringBuilder label = new StringBuilder("Se seleccionó: ");
				for (Figure figure : canvasState.figures()) {
					if(figureBelongs(figure, eventPoint)) {
						found = true;
						selectedFigure = (MovableFigure) figure;
						label.append(figure.toString());
					}
				}
				if (found) {
					statusPane.updateStatus(label.toString());
				} else {
					selectedFigure = null;
					statusPane.updateStatus("Ninguna figura encontrada");
				}
				redrawCanvas();
			}
		});
		canvas.setOnMouseDragged(event -> {
			if(selectionButton.isSelected()) {
				Point eventPoint = new Point(event.getX(), event.getY());
				// Cursor offsets
				double diffX = (eventPoint.getX() - startPoint.getX()) / 100;
				double diffY = (eventPoint.getY() - startPoint.getY()) / 100;

				// Each figure must know how to move
				selectedFigure.move(diffX, diffY);
				redrawCanvas();
			}
		});
		setLeft(buttonsBox);
		setRight(canvas);
	}

	void redrawCanvas() {
		// Clear canvas
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

		// Redraws everything
		for(Figure figure : canvasState.figures()) {

			// Set border and filling colors
			if(figure == selectedFigure) {
				gc.setStroke(Color.RED);
			} else {
				gc.setStroke(lineColor);
			}
			gc.setFill(fillColor);

			if(figure instanceof Rectangle) {
				Rectangle rectangle = (Rectangle) figure;
				gc.fillRect(rectangle.getTopLeft().getX(), rectangle.getTopLeft().getY(), rectangle.width(), rectangle.height());
				gc.strokeRect(rectangle.getTopLeft().getX(), rectangle.getTopLeft().getY(),rectangle.width(), rectangle.height());
			} else if(figure instanceof Square) {
				Square square = (Square) figure;
				gc.fillRect(square.getTopLeft().getX(), square.getTopLeft().getY(), square.width(), square.width());
				gc.strokeRect(square.getTopLeft().getX(), square.getTopLeft().getY(), square.width(), square.width());
			} else if(figure instanceof Ellipse) {
				Ellipse ellipse = (Ellipse) figure;
				gc.fillOval(ellipse.getTopLeft().getX(), ellipse.getTopLeft().getY(), ellipse.width(), ellipse.height());
				gc.strokeOval(ellipse.getTopLeft().getX(), ellipse.getTopLeft().getY(), ellipse.width(), ellipse.height());
			} else if(figure instanceof Circle) {
				Circle circle = (Circle) figure;
				gc.fillOval(circle.getTopLeft().getX(), circle.getTopLeft().getY(), circle.width(), circle.width());
				gc.strokeOval(circle.getTopLeft().getX(), circle.getTopLeft().getY(), circle.width(), circle.width());
			}
			else if(figure instanceof Line) {
				Line line = (Line) figure;
				gc.strokeLine(line.getStartPoint().getX(), line.getStartPoint().getY(), line.getEndPoint().getX(), line.getEndPoint().getY());
			}
		}
	}

//	void drawFigureSameWH(MovableFigure figure){
//		gc.fillOval(figure.getTopLeft().getX(), figure.getTopLeft().getY(), figure.width(), figure.width());
//		gc.strokeOval(figure.getTopLeft().getX(), figure.getTopLeft().getY(), figure.width(), figure.width());
//	}
//
//	void drawFigureDistinctWH(Figure figure){
//		gc.fillRect(figure.getTopLeft().getX(), figure.getTopLeft().getY(), figure.width(), figure.height());
//		gc.strokeRect(figure.getTopLeft().getX(), figure.getTopLeft().getY(),figure.width(), figure.height());
//
//	}

	boolean figureBelongs(Figure figure, Point eventPoint) {
		return figure.belongs(eventPoint);
	}

}
