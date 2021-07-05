package backend.model;

public abstract class Figure {

    public String borderColor;
    public boolean isSelected;

    public abstract boolean belongs(Point point);

    public void setBorderColor(String color){
        borderColor = color;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void select(){
        isSelected = true;
    }

    public void unselect(){
        isSelected = false;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
