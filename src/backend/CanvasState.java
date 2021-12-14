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
        // System.out.println("ADDDDDDDDDD");
        figuresBySelectProperty.get(false).add(figure);
        allFigures.add(figure);
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

    public boolean removeFigure(Figure figureToRemove, boolean key){
        boolean figureRemoved = false;
        Iterator<Figure> iter = figuresBySelectProperty.get(key).iterator();
        while(iter.hasNext()){
            Figure toRemove = iter.next();
            if(toRemove.equals(figureToRemove)){
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
        figure.select();
        removeFigure(figure, false);
        figuresBySelectProperty.get(true).add(figure);
        System.out.println("AFTER SELECTING: "+figuresBySelectProperty);
    }

    public void unselectFigure(Figure figure){
        figure.unselect();
        removeFigure(figure, true);
        figuresBySelectProperty.get(false).add(figure);
        System.out.println("AFTER UNSELECTING: "+figuresBySelectProperty);
    }

    public void unSelectAllFigures(){
        for(Figure figure : figures()){
            if(figure.isSelected()){
                //System.out.println("UNSELECT FIG: "+figure);
                unselectFigure(figure);
            }
        }
        //System.out.println("ALL FIGURES AFTER UNSELECTALL:"+allFigures);
        //System.out.println(figuresBySelectProperty);
    }

    public Deque<Figure> figures() {
        return allFigures;
    }

    public List<Figure> getSelectedFigures() {
        return figuresBySelectProperty.getOrDefault(true, new ArrayList<>());
    }

    public boolean isAFigureSelected(){
        System.out.println("GetSelectedFigs"+getSelectedFigures());
        return !getSelectedFigures().isEmpty();
    }

    public void moveSelectedFigures(double x, double y) {
        // Each figure must know how to move
        for(Figure figure : getSelectedFigures()){
            // System.out.println("FIGURA A MOVER:" + figure);
            figure.move(x, y);
        }
    }
}
