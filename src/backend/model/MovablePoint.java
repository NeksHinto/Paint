package backend.model;

public class MovablePoint extends Point implements Movable {

    public MovablePoint(double x, double y) {
        super(x, y);
    }

    @Override
    public void moveVertically(double delta) {
        y += delta;
    }

    @Override
    public void moveHorizontally(double delta) {
        x += delta;
    }

}
