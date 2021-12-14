package backend.model;

public interface MovableFigure extends Movable {

    MovablePoint[] getPoints();

    @Override
    default void moveHorizontally(double delta) {
        for(MovablePoint movablePoint : getPoints()) {
            movablePoint.moveHorizontally(delta);
        }
    }

    @Override
    default void moveVertically(double delta) {
        for(MovablePoint movablePoint : getPoints()) {
            movablePoint.moveVertically(delta);
        }
    }

}