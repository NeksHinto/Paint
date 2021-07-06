package backend.model;

public class Ellipse extends Figure2D {
    protected Point centerPoint;
    private double horizontalAxis, verticalAxis;

    public Ellipse(Point centerPoint, double horizontalAxis, double minorAxis) {
        this.centerPoint = centerPoint;
        this.horizontalAxis = horizontalAxis;
        this.verticalAxis = minorAxis;
    }

    @Override
    public MovablePoint[] getPoints() {
        return new MovablePoint[]{(MovablePoint) centerPoint};
    }

    public Point getCenterPoint() {
        return centerPoint;
    }

    public double getHorizontalAxis() {
        return horizontalAxis;
    }

    public Point getTopLeft(){
        return new Point(centerPoint.getX() - horizontalAxis, centerPoint.getY() - verticalAxis);
    }

    public double getVerticalAxis() {
        return verticalAxis;
    }

    @Override
    public double width() {
        return getHorizontalAxis();
    }

    @Override
    public double height() {
        return getVerticalAxis();
    }

    @Override
    public double area() {
        return Math.PI / 4 * horizontalAxis * verticalAxis;
    }

    @Override
    public double perimeter() {
        return Math.PI / 2 * (horizontalAxis + verticalAxis);
    }

    @Override
    public boolean belongs(Point point) {
        return Math.sqrt(Math.pow(centerPoint.getX() - point.getX(), 2) +
                Math.pow(centerPoint.getY() - point.getY(), 2)) < horizontalAxis;
    }

    @Override
    public String toString() {
        return String.format("Elipse [Centro: %s, DHorizontal: %.2f, DVertical: %.2f]", centerPoint, horizontalAxis, verticalAxis);
    }
}