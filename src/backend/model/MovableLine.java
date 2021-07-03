package backend.model;

public class MovableLine extends Line implements MovableFigure {
    public MovableLine(MovablePoint startPoint, MovablePoint endPoint) {
        super(startPoint, endPoint);
    }

    @Override
    public MovablePoint[] getPoints() {
        return new MovablePoint[]{(MovablePoint) startPoint, (MovablePoint) endPoint};
    }
}
