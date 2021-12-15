package backend.model;

import javafx.scene.control.ToggleButton;

public abstract class FigureButton extends ToggleButton {

    public FigureButton(String text) {
        super(text);
    }

    public abstract Figure returnFigureToDraw(Point start, Point end, String fillColor, String borderColor, double borderWidth);
}
