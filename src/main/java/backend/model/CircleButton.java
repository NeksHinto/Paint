package backend.model;

public class CircleButton extends FigureButton {
    public static final String CIRCLE_BUTTON_TEXT = "Círculo";
    double circleRadius;

    public CircleButton() {
        super(CIRCLE_BUTTON_TEXT);
    }

    @Override
    public Figure returnFigureToDraw(Point start, Point end, String fillColor, String borderColor, double borderWidth) {
        circleRadius = Math.sqrt(Math.pow(end.getX() - start.getX(), 2) + Math.pow(end.getY() - start.getY(), 2));
        return new Circle(start, circleRadius, fillColor, borderColor, borderWidth);
    }
}
