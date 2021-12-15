package backend.model;

public class RectangleButton extends FigureButton {
    public static final String RECTANGLE_BUTTON_TEXT = "Rectángulo";

    public RectangleButton() {
        super(RECTANGLE_BUTTON_TEXT);
    }

    @Override
    public Figure returnFigureToDraw(Point start, Point end, String fillColor, String borderColor, double borderWidth) {
        return new Rectangle(start, end, fillColor, borderColor, borderWidth);
    }
}
