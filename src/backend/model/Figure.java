package backend.model;

public abstract class Figure implements MovableFigure{

    public static final String SELECTED_BORDER_COLOR = "red";

    public String borderColor;
    public boolean isSelected;

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

    public void setBorderColor(String color){
        borderColor = color;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void select(){
        isSelected = true;
        setBorderColor(SELECTED_BORDER_COLOR);
    }

    public void unselect(){
        isSelected = false;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
