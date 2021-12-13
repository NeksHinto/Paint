package backend.model;

import javafx.scene.canvas.GraphicsContext;

public interface DrawableFigure extends MovableFigure, SelectableFigure {
    void draw(GraphicsContext gc);
}
