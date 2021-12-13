package backend.model;

public class CircleButton extends FigureButton {
    public static final String CIRCLE_BUTTON_TEXT = "Círculo";
    double circleDiameter;

    public CircleButton() {
        super(CIRCLE_BUTTON_TEXT);
    }

    @Override
    public Figure returnFigureToDraw(Point start, Point end) {
        circleDiameter = Math.abs(end.getX() - start.getX());
        return new Circle(start, circleDiameter);
    }
}
