package backend.model;

public class Circle extends Ellipse{

    public Circle(Point centerPoint, double radius) {
        super(centerPoint, 2*radius, 2*radius);
    }

    @Override
    public MovablePoint[] getPoints() {
        return new MovablePoint[]{(MovablePoint) centerPoint};
    }

    @Override
    public String toString() {
        return String.format("Círculo [Centro: %s, Radio: %.2f]", getCenterPoint(), getHorizontalAxis()/2);
    }

}
