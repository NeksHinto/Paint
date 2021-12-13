package backend;

import backend.model.*;

import java.util.*;

public class CanvasState {

    private final Deque<Figure> allFigures = new LinkedList<>();
    private final Map<Boolean, List<Figure>> figuresBySelectProperty = new HashMap<>();

    public CanvasState() {
        figuresBySelectProperty.putIfAbsent(false, new ArrayList<>());
        figuresBySelectProperty.putIfAbsent(true, new ArrayList<>());
    }

    public void addFigure(Figure figure) {
        figuresBySelectProperty.get(false).add(figure);
        allFigures.add(figure);
    }

    public boolean removeAllSelectedFigures(){
        boolean figureRemoved = false;
        if(isAFigureSelected()){
            figureRemoved = removeFigures(figuresBySelectProperty.get(true));
            System.out.println("REMOVED FROM allFigures: "+ figureRemoved);
            figureRemoved = figuresBySelectProperty.keySet().removeIf(key -> key);
            System.out.println("REMOVED FROM figuresBySelectProperty: "+ figureRemoved);
        }
        return figureRemoved;
    }

    public boolean removeFigures(List<Figure> figuresToRemove){
        boolean figureRemoved = false;
        Iterator<Figure> iter = allFigures.iterator();
        while(iter.hasNext()){
            Figure toRemove = iter.next();
            if(shouldRemove(toRemove, figuresToRemove)){
                iter.remove();
                figureRemoved = true;
            }
        }
        return figureRemoved;
    }

    public boolean shouldRemove(Figure toRemove, List<Figure> figuresToRemove){
        for(Figure figure : figuresToRemove){
            if(toRemove.equals(figure)){
                return true;
            }
        }
        return false;
    }

    public void selectFigure(Figure figure){
        System.out.println(figure.isSelected);
        figure.select();
        figuresBySelectProperty.get(false).remove(figure);
        figuresBySelectProperty.get(true).add(figure);
        System.out.println(figuresBySelectProperty);

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
