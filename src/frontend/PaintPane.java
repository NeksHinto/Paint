package frontend;

import backend.CanvasState;
import backend.model.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.util.List;

public class PaintPane extends BorderPane {

	// BackEnd
	CanvasState canvasState;

	// Canvas: for figure drawing graphics
	Canvas canvas = new Canvas(1000, 800);
	// Canvas' context
	GraphicsContext gc = canvas.getGraphicsContext2D();

	Color lineColor = Color.BLACK;
	Color fillColor = Color.YELLOW;
	Double lineWidth;

	// Left menu
	ToolPane toolPane = new ToolPane(lineColor, fillColor);

	// Dibujar una figura
	MovablePoint startPoint;

	// StatusBar
	StatusPane statusPane;

	public PaintPane(CanvasState canvasState, StatusPane statusPane) {
		this.canvasState = canvasState;
		this.statusPane = statusPane;

		// Default line width for strokes
		gc.setLineWidth(1);

		// Left Panel Tool event listeners
		setToolPaneListeners();

		// Event: Mouse Moved
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

		// Event: Click
		canvas.setOnMouseClicked(event -> {
			if(toolPane.selectionButton.isSelected()) {
				Point eventPoint = new Point(event.getX(), event.getY());
				boolean figureSelected = false;
				StringBuilder label = new StringBuilder("Se seleccionó: ");
				for (Figure figure : canvasState.figures()) {
					if(figureBelongs(figure, eventPoint)) {
						figureSelected = true;
						figure.select();
						label.append(figure.toString());
					}
				}
				if (figureSelected) {
					statusPane.updateStatus(label.toString());
				} else {
					statusPane.updateStatus("Ninguna figura encontrada");
				}
				drawCanvas();
			} else {
				canvasState.unSelectAllFigures();
			}
		});

		// Event: Pressed Mouse
		canvas.setOnMousePressed(event -> {
			startPoint = new MovablePoint(event.getX(), event.getY());
		});

		// Event: Dragged Mouse
		canvas.setOnMouseDragged(event -> {
			// Move selected figures, if there are any
			if(toolPane.selectionButton.isSelected()) {
				Point eventPoint = new Point(event.getX(), event.getY());
				moveFigures(eventPoint);
				drawCanvas();
			}
		});

		// Event: Release of Pressing Mouse
		canvas.setOnMouseReleased(event -> {
			MovablePoint endPoint = new MovablePoint(event.getX(), event.getY());
			if(invalidMouseDrag(endPoint)){
				return ;
			}
			Figure1D newFigure1D = null;
			Figure2D newFigure2D = null;
			if(isNewFigure2D()){
				newFigure2D = addNewFigure2D(newFigure2D, endPoint);
			}
			else if(toolPane.lineButton.isSelected()) {
				newFigure1D = new Line(startPoint, endPoint);
			}
			// Transparent rectangle for multiple selection
			else if(toolPane.selectionButton.isSelected()){
				Rectangle areaSelected = new Rectangle(startPoint, endPoint);
				areaSelected.setBorderColor(Color.TRANSPARENT.toString());
				areaSelected.setBorderColor(Color.TRANSPARENT.toString());
				selectFigures(areaSelected);
			} else {
				return ;
			}
			if(newFigure2D != null){
				canvasState.addFigure2D(newFigure2D);
			}
			else if(newFigure1D != null){
				canvasState.addFigure1D(newFigure1D);
			}
			startPoint = null;
			drawCanvas();
		});

		setLeft(toolPane.menu);
		setRight(canvas);
	}

	void setSelectedFiguresFillingColor(){
		for(Figure2D figure : canvasState.getSelectedFigures2D()){
			figure.setFillingColor(fillColor.toString());
		}
	}

	void setSelectedFiguresBorderColor(){
		for(Figure figure : canvasState.getSelectedFigures()){
			figure.setBorderColor(fillColor.toString());
		}
	}

	// Redraw entire canvas
	void drawCanvas() {
		// Clear canvas
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

		// Draw 1D figures
		for(Figure1D figure1D : canvasState.getFigures1D()) {
			// Set border and filling colors
			setFigureBorderColor(figure1D);
			draw1DFigure(figure1D);
		}

		//Draw 2D figures
		for(Figure2D figure2D : canvasState.getFigures2D()){
			setFigureBorderColor(figure2D);
			setFigureFillingColor(figure2D);
			draw2DFigure(figure2D);
		}
	}

	void setFigureBorderColor(Figure figure){
		if(figure.isSelected()) {
			gc.setStroke(Color.RED);
			figure.setBorderColor(Color.RED.toString());
		} else {
			figure.setBorderColor(lineColor.toString());
			gc.setStroke(Color.web(figure.getBorderColor()));
		}
	}

	void setFigureFillingColor(Figure2D figure){
		gc.setFill(fillColor);
		figure.setFillingColor(fillColor.toString());
	}

	void draw1DFigure(Figure1D figure){
		if(figure instanceof Line) {
			drawLine((Line)figure);
		}
	}

	void drawLine(Line line){
		gc.strokeLine(line.getStartPoint().getX(), line.getStartPoint().getY(), line.getEndPoint().getX(), line.getEndPoint().getY());
	}

	void draw2DFigure(Figure2D figure){
		if(figure instanceof Rectangle) {
			drawRect2D((Rectangle) figure);
		}
		else {
			drawOval2D((Ellipse) figure);
		}
	}

	void drawRect2D(Rectangle rectFigure){
		if(rectFigure instanceof Square) {
			drawSquare((Square) rectFigure);
		} else {
			gc.fillRect(rectFigure.getTopLeft().getX(), rectFigure.getTopLeft().getY(), rectFigure.width(), rectFigure.height());
			gc.strokeRect(rectFigure.getTopLeft().getX(), rectFigure.getTopLeft().getY(),rectFigure.width(), rectFigure.height());
		}
	}

	void drawSquare(Square square2D){
		gc.fillRect(square2D.getTopLeft().getX(), square2D.getTopLeft().getY(), square2D.width(), square2D.width());
		gc.strokeRect(square2D.getTopLeft().getX(), square2D.getTopLeft().getY(), square2D.width(), square2D.width());
	}

	void drawOval2D(Ellipse ovalFigure){
		if(ovalFigure instanceof Circle){
			drawCircle((Circle) ovalFigure);
		} else {
			gc.fillOval(ovalFigure.getTopLeft().getX(), ovalFigure.getTopLeft().getY(), ovalFigure.width(), ovalFigure.height());
			gc.strokeOval(ovalFigure.getTopLeft().getX(), ovalFigure.getTopLeft().getY(), ovalFigure.width(), ovalFigure.height());
		}
	}

	void drawCircle(Circle circle){
		gc.fillOval(circle.getTopLeft().getX(), circle.getTopLeft().getY(), circle.width(), circle.width());
		gc.strokeOval(circle.getTopLeft().getX(), circle.getTopLeft().getY(), circle.width(), circle.width());
	}

	boolean isNewFigure2D(){
		return toolPane.rectangleButton.isSelected() ||
				toolPane.squareButton.isSelected() ||
				toolPane.ellipseButton.isSelected() ||
				toolPane.circleButton.isSelected();
	}

	Figure2D addNewFigure2D(Figure2D newFigure, Point endPoint){
		if(toolPane.rectangleButton.isSelected()) {
			return new Rectangle(startPoint, endPoint);
		}
		else if(toolPane.squareButton.isSelected()) {
			return new Square(startPoint, endPoint);
		}
		else if(toolPane.ellipseButton.isSelected()) {
			double horizontalDistance = Math.abs(endPoint.getX() - startPoint.getX());
			double verticalDistance = Math.abs(endPoint.getY() - startPoint.getY());
			return new Ellipse(startPoint, horizontalDistance, verticalDistance);
		}
		else if(toolPane.circleButton.isSelected()) {
			double circleDiameter = Math.abs(endPoint.getX() - startPoint.getX());
			return new Circle(startPoint, circleDiameter);
		}
		else {
			return null;
		}
	}

	boolean figureBelongs(Figure figure, Point eventPoint) {
		return figure.belongs(eventPoint);
	}

	void selectFigures(Rectangle area){
		for(Figure figure : canvasState.figures()){
			if(figure.inside(area)){
				figure.select();
			}
		}
	}

	void clearFigures(List<Figure> figuresToDelete){
		for(Figure figure : figuresToDelete){
			deleteFigure(figure);
		}
	}

	void deleteFigure(Figure figure){
		canvasState.removeFigure(figure);
	}

	void moveFigures(Point endPoint){
		// Cursor offsets
		double diffX = (endPoint.getX() - startPoint.getX()) / 100;
		double diffY = (endPoint.getY() - startPoint.getY()) / 100;
		// Each figure must know how to move
		for(Figure figure : canvasState.getSelectedFigures()){
			figure.move(diffX, diffY);
		}
	}

	void setToolPaneListeners(){
		toolPane.getBorderSlider().valueProperty().addListener((ov, old_val, new_val) -> {
			gc.setLineWidth((double) new_val);
		});

		toolPane.getBorderCp().setOnAction(event -> {
			lineColor = toolPane.borderCp.getValue();
			setSelectedFiguresBorderColor();
		});

		toolPane.getFillingCp().setOnAction(event -> {
			fillColor = toolPane.fillingCp.getValue();
			setSelectedFiguresFillingColor();
		});

		toolPane.deleteButton.setOnAction(event -> {
			if(canvasState.isAFigureSelected()){
				clearFigures(canvasState.getSelectedFigures());
			}
			canvasState.unSelectAllFigures();
		});
	}

	boolean invalidMouseDrag(Point endPoint) {
		return startPoint == null || (endPoint.getX() < startPoint.getX() || endPoint.getY() < startPoint.getY());
	}

}
