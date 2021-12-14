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

	FigureButton selectedFigureButton;

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
					if(figureBelongs(figure, eventPoint) && !figure.isSelected) {
						figureSelected = true;
						canvasState.selectFigure(figure);
						label.append(figure.toString());
					}
				}
				if (figureSelected) {
					statusPane.updateStatus(label.toString());
				} else {
					statusPane.updateStatus("Ninguna figura encontrada");
				}
				drawCanvas();
                // Clear selection button
                toolPane.selectionButton.setSelected(false);
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
			if(!toolPane.selectionButton.isSelected() && !selectedFigureButton.isSelected() && canvasState.isAFigureSelected()) {
				System.out.println("MOVER");
				MovablePoint eventPoint = new MovablePoint(event.getX(), event.getY());
				moveFigures(eventPoint);
				System.out.println("Back in Pane"+canvasState.figures());
				startPoint = eventPoint;
				drawCanvas();
				// Clear selection button
				toolPane.selectionButton.setSelected(false);
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

			// Transparent rectangle for multiple selection
			if(toolPane.selectionButton.isSelected()){
				Rectangle areaSelected = new Rectangle(startPoint, endPoint);
				areaSelected.setBorderColor(Color.TRANSPARENT.toString());
				areaSelected.setBorderColor(Color.TRANSPARENT.toString());
				selectFigures(areaSelected);
			} else if (selectedFigureButton.isSelected()){
				newFigure = selectedFigureButton.returnFigureToDraw(startPoint, endPoint);
			}
			if(newFigure != null){
				//System.out.println("SELECTED FIG BUTTON IS SELECTED: "+ selectedFigureButton);
				canvasState.addFigure(newFigure);
			}
			startPoint = null;
			// Clear selected figure button
			selectedFigureButton.setSelected(false);
			drawCanvas();
		});

		setLeft(toolPane.menu);
		setRight(canvas);
	}

	void selectFigures(Rectangle area){
		for(Figure figure : canvasState.figures()){
			if(figure.inside(area)){
				canvasState.selectFigure(figure);
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

	void setSelectedFiguresBorderWidth(){
		for(Figure figure : canvasState.getSelectedFigures()){
			//System.out.println(figure);
			figure.setBorderWidth(lineWidth);
		}
	}

	// Redraw entire canvas
	void drawCanvas() {
		System.out.println("DRAW CANVASSSSSSSSS");
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
		canvasState.removeFigures(figuresToDelete);
	}

	void moveFigures(Point endPoint){
		// Cursor offsets
		double diffX = (endPoint.getX() - startPoint.getX());
		double diffY = (endPoint.getY() - startPoint.getY());
		canvasState.moveSelectedFigures(diffX, diffY);
	}

	void bringToFrontSelectedFigures(List<Figure> figures){
		canvasState.bringToFront(figures);
	}

	void sendToBackSelectedFigures(List<Figure> figures){
		canvasState.sendToBack(figures);
	}

	void setToolPaneListeners(){
		toolPane.getBorderSlider().valueProperty().addListener((ov, old_val, new_val) -> {
			gc.setLineWidth((double) new_val);
			drawCanvas();
			lineWidth = (double) new_val;
			setSelectedFiguresBorderWidth();
		});

		toolPane.getBorderCp().valueProperty().addListener((ov, old_val, new_val) -> {
			gc.setStroke(new_val);
			drawCanvas();
			setSelectedFiguresBorderColor();
		});

		toolPane.getFillingCp().valueProperty().addListener((ov, old_val, new_val) -> {
			gc.setFill(new_val);
			drawCanvas();
			fillColor = new_val;
			setSelectedFiguresFillingColor();
		});

		toolPane.deleteButton.setOnAction(event -> {
			clearFigures(canvasState.getSelectedFigures());
			drawCanvas();
		});

		toolPane.bringToFront.setOnAction(event -> {
			bringToFrontSelectedFigures(canvasState.getSelectedFigures());
			drawCanvas();
		});

		toolPane.sendToBack.setOnAction(event -> {
			sendToBackSelectedFigures(canvasState.getSelectedFigures());
			drawCanvas();
		});

		// Selected toggle button listener
		toolPane.tools.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if(newValue instanceof FigureButton){
                    selectedFigureButton = (FigureButton) newValue;
                }
			}
		});
	}

	boolean invalidMouseDrag(Point endPoint) {
		return startPoint == null || (endPoint.getX() < startPoint.getX() || endPoint.getY() < startPoint.getY());
	}

}
