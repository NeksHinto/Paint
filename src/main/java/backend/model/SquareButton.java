package backend.model;

public class SquareButton extends FigureButton {
    public static final String SQUARE_BUTTON_TEXT = "Cuadrado";

    public SquareButton() {
        super(SQUARE_BUTTON_TEXT);
    }

    @Override
    public Figure returnFigureToDraw(Point start, Point end, String fillColor, String borderColor, double borderWidth) {
        return new Square(start, end, fillColor, borderColor, borderWidth);
    }
}
