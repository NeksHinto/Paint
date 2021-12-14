package backend.model;

public interface Movable {

    void moveHorizontally(double delta);

    void moveVertically(double delta);

    default void move(double deltaX, double deltaY) {
        System.out.println("DELTAX="+deltaX+"DELTAY="+deltaY);
        moveHorizontally(deltaX);
        moveVertically(deltaY);
    }

}
