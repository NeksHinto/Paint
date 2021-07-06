package backend.model;

public class Rectangle extends Figure2D {

    protected final Point topLeft, bottomRight;

    public Rectangle(Point topLeft, Point bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    @Override
    public MovablePoint[] getPoints() {
        return new MovablePoint[]{(MovablePoint) topLeft, (MovablePoint) bottomRight};
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public Point getBottomRight() {
        return bottomRight;
    }

    public double base() {
        return Math.abs(topLeft.getX() - bottomRight.getX());
    }

    @Override
    public double width() {
        return Math.abs(topLeft.getX() - bottomRight.getX());
    }

    @Override
    public double height() {
        return Math.abs(topLeft.getY() - bottomRight.getY());
    }

    @Override
    public double area() {
        return base() * height();
    }

    @Override
    public double perimeter() {
        return (base() + height()) * 2;
    }

    @Override
    public boolean belongs(Point point) {
        return point.getX() > topLeft.getX() && point.getX() < bottomRight.getX() &&
                point.getY() > topLeft.getY() && point.getY() < bottomRight.getY();
    }

    @Override
    public String toString() {
        return String.format("Rectángulo [ %s , %s ]", topLeft, bottomRight);
    }

}
