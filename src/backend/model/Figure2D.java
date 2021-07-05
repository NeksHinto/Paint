package backend.model;

public abstract class Figure2D extends Figure {
    public String fillingColor = "0xcc3333ff"; // test

    public void setFillingColor(String color){
        fillingColor = color;
    }
    public abstract double width();
    public abstract double height();
    public abstract double area();
    public abstract double perimeter();
}
