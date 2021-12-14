package backend.model;

import javafx.scene.canvas.GraphicsContext;

public class Line extends Figure {
    protected Point startPoint, endPoint;

    public Line(Point startPoint, Point endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    @Override
    public MovablePoint[] getPoints() {
        return new MovablePoint[]{(MovablePoint) startPoint, (MovablePoint) endPoint};
    }    public Point getStartPoint() {
        return startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    @Override
    public boolean belongs(Point point) {
        return startPoint.distanceTo(point) + endPoint.distanceTo(point) == length();
    }

    public double length() {
        return startPoint.distanceTo(endPoint);
    }

    @Override
    public String toString() {
        return String.format("Línea [ %s , %s ]", startPoint, endPoint);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.strokeLine(getStartPoint().getX(), getStartPoint().getY(), getEndPoint().getX(), getEndPoint().getY());
    }
}
