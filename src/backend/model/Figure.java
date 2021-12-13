package backend.model;

public abstract class Figure implements DrawableFigure, MovableFigure, SelectableFigure {

    public static final String SELECTED_BORDER_COLOR = "red";

    public String borderColor;
    public boolean isSelected;

    public boolean belongs(Point point){
        return this.belongs(point);
    }

    // Checks if "this" figure is inside figure (parameter)
    // by checking if all "this" figure's points belong to figure (parameter)
    public boolean inside(Figure figure){
        for(Point point : this.getPoints()){
            if(!figure.belongs(point)){
                return false;
            }
        }
        return true;
    }

    public void setFillingColor(String color){
        this.setFillingColor(color);
    }

    public void setBorderColor(String color){
        borderColor = color;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void select() {
        isSelected = true;
        setBorderColor(SELECTED_BORDER_COLOR);
    }

    @Override
    public void unselect() {
        isSelected = false;
    }
}
