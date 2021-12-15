package backend.model;

public class Circle extends Ellipse{

    public Circle(Point centerPoint, double radius, String fillColor, String borderColor, double borderWidth) {
        super(centerPoint, radius, radius, fillColor, borderColor, borderWidth);
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
