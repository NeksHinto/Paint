package backend;

import backend.model.*;

import java.util.*;

public class CanvasState {

    private final Deque<Figure> allFigures = new LinkedList<>();
    private final Map<Boolean, List<Figure>> figuresBySelectProperty = new HashMap<>();

    public void addFigure(Figure figure) {
        figuresBySelectProperty.putIfAbsent(false, new ArrayList<>());
        figuresBySelectProperty.get(false).add(figure);
        allFigures.add(figure);
    }

    public boolean removeFigure(Figure figure){
        boolean figureRemoved;
        figureRemoved = figuresBySelectProperty.get(true).remove(figure);
        figureRemoved = allFigures.remove(figure);

        return figureRemoved;
    }

    public void selectFigure(Figure figure){
        figure.select();
        figuresBySelectProperty.get(false).remove(figure);
        figuresBySelectProperty.get(true).add(figure);
    }

    public void unSelectAllFigures(){
        for(Figure figure : figures()){
            if(figure.isSelected()){
                figure.unselect();
            }
        }
    }

    public Deque<Figure> figures() {
        return allFigures;
    }

    public List<Figure> getSelectedFigures() {
        return figuresBySelectProperty.getOrDefault(true, new ArrayList<>());
    }

    public boolean isAFigureSelected(){
        return !getSelectedFigures().isEmpty();
    }
}
