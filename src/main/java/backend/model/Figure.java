package backend.model;

public abstract class Figure implements DrawableFigure, MovableFigure, SelectableFigure {

    public static final String SELECTED_BORDER_COLOR = "red";

    public String borderColor;
    public boolean isSelected;
    public double borderWidth;
    private String fillColor;

    public abstract boolean belongs(Point point);

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

    public void setFillingColor(String color) {
        this.fillColor = color;
    }

    public String getFillColor() {
        return fillColor;
    }

    public void setBorderColor(String color){
        borderColor = color;
    }

    public void setBorderWidth(double width){
        borderWidth = width;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public double getBorderWidth() {
        return borderWidth;
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
