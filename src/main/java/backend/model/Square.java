package backend.model;

import javafx.scene.canvas.GraphicsContext;

public class Square extends Rectangle {

    public Square(Point topLeft, Point bottomRight, String fillColor, String borderColor, double borderWidth) {
        super(topLeft, bottomRight, fillColor, borderColor, borderWidth);
    }

    @Override
    public String toString() {
        return String.format("Cuadrado [ %s , %s ]", getTopLeft(), getBottomRight());
    }

    @Override
    public MovablePoint[] getPoints() {
        return new MovablePoint[]{(MovablePoint) topLeft, (MovablePoint) bottomRight};
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.fillRect(getTopLeft().getX(), getTopLeft().getY(), width(), width());
        gc.strokeRect(getTopLeft().getX(), getTopLeft().getY(), width(), width());
    }
}
