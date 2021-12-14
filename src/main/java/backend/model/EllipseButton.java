package backend.model;

import javafx.scene.canvas.GraphicsContext;

public class EllipseButton extends FigureButton {
    public static final String ELLIPSE_BUTTON_TEXT = "Elipse";
    double horizontalDistance;
    double verticalDistance;

    public EllipseButton() {
        super(ELLIPSE_BUTTON_TEXT);
    }

    @Override
    public Figure returnFigureToDraw(Point start, Point end) {
        horizontalDistance = Math.abs(end.getX() - start.getX());
        verticalDistance = Math.abs(end.getY() - start.getY());
        return new Ellipse(start, horizontalDistance, verticalDistance);
    }
}
