package backend.model;

public class Line extends Figure1D {
    protected Point startPoint, endPoint;

    public Line(Point startPoint, Point endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    @Override
    public boolean belongs(Point point) {
        return startPoint.distanceTo(point) + endPoint.distanceTo(point) == length();
    }

    @Override
    public double length() {
        return startPoint.distanceTo(endPoint);
    }

    @Override
    public String toString() {
        return String.format("Línea [ %s , %s ]", startPoint, endPoint);
    }
}
