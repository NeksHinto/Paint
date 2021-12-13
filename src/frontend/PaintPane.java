package frontend;

import backend.CanvasState;
import backend.model.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Toggle;
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

	FigureButton selectedButton;

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
				// TODO error popup
				return ;
			}

			Figure newFigure = null;
			newFigure = selectedButton.returnFigureToDraw(startPoint, endPoint);

			// Transparent rectangle for multiple selection
			if(toolPane.selectionButton.isSelected()){
				Rectangle areaSelected = new Rectangle(startPoint, endPoint);
				areaSelected.setBorderColor(Color.TRANSPARENT.toString());
				areaSelected.setBorderColor(Color.TRANSPARENT.toString());
				selectFigures(areaSelected);
			} else {
				return ;
			}
			if(newFigure != null){
				canvasState.addFigure(newFigure);
			}
			startPoint = null;
			drawCanvas();
		});

		setLeft(toolPane.menu);
		setRight(canvas);
	}

	void selectFigures(Rectangle area){
		for(Figure figure : canvasState.figures()){
			if(figure.inside(area)){
				figure.select();
			}
		}
	}

	void setSelectedFiguresFillingColor(){
		for(Figure figure : canvasState.getSelectedFigures()){
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

		// Draw figures
		for(Figure figure : canvasState.figures()) {
			// Set border and filling colors
			setFigureBorderColor(figure);
			figure.draw(gc);
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

	void setFigureFillingColor(Figure figure){
		gc.setFill(fillColor);
		figure.setFillingColor(fillColor.toString());
	}

	boolean figureBelongs(Figure figure, Point eventPoint) {
		return figure.belongs(eventPoint);
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
		double diffX = (endPoint.getX() - startPoint.getX());
		double diffY = (endPoint.getY() - startPoint.getY());
		// Each figure must know how to move
		for(Figure figure : canvasState.getSelectedFigures()){
			System.out.println(figure);
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

		// Selected toggle button listener
		toolPane.tools.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				System.out.println(newValue);
				selectedButton = (FigureButton) newValue;
			}
		});
	}

	boolean invalidMouseDrag(Point endPoint) {
		return startPoint == null || (endPoint.getX() < startPoint.getX() || endPoint.getY() < startPoint.getY());
	}

}
