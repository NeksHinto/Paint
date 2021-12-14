package backend.model;

public interface MovableFigure extends Movable {

    MovablePoint[] getPoints();

    @Override
    default void moveHorizontally(double delta) {
        for(MovablePoint movablePoint : getPoints()) {
            System.out.println("MOV_X="+movablePoint+" DELTA="+delta);
            movablePoint.moveHorizontally(delta);
        }
    }

    @Override
    default void moveVertically(double delta) {
        for(MovablePoint movablePoint : getPoints()) {
            System.out.println("MOV_Y="+movablePoint+" DELTA="+delta);
            movablePoint.moveVertically(delta);
        }
    }

}