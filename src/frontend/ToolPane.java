package frontend;

import backend.model.*;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ToolPane extends BorderPane {

    VBox menu = new VBox(15);

    // Toggle buttons
    ToggleGroup tools;
    ToggleButton selectionButton = new ToggleButton("Seleccionar");
    ToggleButton lineButton = new LineButton();
    ToggleButton rectangleButton = new RectangleButton();
    ToggleButton squareButton = new SquareButton();
    ToggleButton circleButton = new CircleButton();
    ToggleButton ellipseButton = new EllipseButton();
    // Delete button
    Button deleteButton = new Button("Borrar");

    Slider borderSlider = new Slider(0, 50, 0);
    ColorPicker borderCp = new ColorPicker();
    ColorPicker fillingCp = new ColorPicker();

    public ToolPane(Color lineColor, Color fillColor){
        // Border components
        this.borderCp.setValue(lineColor);
        Label borderLabel = new Label("Borde");
        // Filling components
        this.fillingCp.setValue(fillColor);
        Label fillingLabel = new Label("Relleno");
        // Components group (buttons)
        tools = new ToggleGroup();
        ToggleButton[] toolsArr = {selectionButton, lineButton, rectangleButton, squareButton, ellipseButton, circleButton};
        for (ToggleButton tool : toolsArr) {
            tool.setMinWidth(100);
            tool.setToggleGroup(tools);
            tool.setCursor(Cursor.HAND);
        }
        deleteButton.setMinWidth(100);
        deleteButton.setCursor(Cursor.HAND);
        // Button container
        VBox buttonsBox = new VBox(10);
        buttonsBox.getChildren().addAll(toolsArr);
        buttonsBox.getChildren().add(deleteButton);

        // Border container
        VBox borderBox = new VBox(10);
        borderSlider.setShowTickMarks(true);
        borderSlider.setShowTickLabels(true);
        borderSlider.setBlockIncrement(10);
        borderBox.getChildren().addAll(borderLabel, borderSlider, borderCp);

        // Filling container
        VBox fillingBox = new VBox(10);
        fillingBox.getChildren().addAll(fillingLabel, fillingCp);

        menu.setPadding(new Insets(5));
        menu.setStyle("-fx-background-color: #999");
        menu.setPrefWidth(110);
        menu.getChildren().addAll(buttonsBox, borderBox, fillingBox);
    }

    public Slider getBorderSlider() {
        return borderSlider;
    }

    public ColorPicker getBorderCp() {
        return borderCp;
    }

    public ColorPicker getFillingCp() {
        return fillingCp;
    }
}
