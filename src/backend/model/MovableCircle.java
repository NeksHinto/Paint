package backend.model;

public class MovableCircle extends Circle implements MovableFigure {

    public MovableCircle(MovablePoint centerPoint, double radius) {
        super(centerPoint, radius);
    }

    @Override
    public MovablePoint[] getPoints() {
        return new MovablePoint[]{(MovablePoint) centerPoint};
    }

}
