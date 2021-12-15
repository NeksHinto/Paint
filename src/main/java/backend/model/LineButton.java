package backend.model;

public class LineButton extends FigureButton {
    public static final String LINE_BUTTON_TEXT = "Línea";

    public LineButton() {
        super(LINE_BUTTON_TEXT);
    }

    @Override
    public Figure returnFigureToDraw(Point start, Point end, String fillColor, String borderColor, double borderWidth) {
        return new Line(start, end, fillColor, borderColor, borderWidth);
    }
}
