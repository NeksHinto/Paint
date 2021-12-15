package backend;

import backend.model.*;

import java.util.*;

public class CanvasState {

    private Deque<Figure> allFigures = new LinkedList<>();
    private final List<Deque<Figure>> history = new ArrayList<>();
    private int currentStateIndex = 0;
    private final Map<Boolean, List<Figure>> figuresBySelectProperty = new HashMap<>();

    public CanvasState() {
        figuresBySelectProperty.putIfAbsent(false, new ArrayList<>());
        figuresBySelectProperty.putIfAbsent(true, new ArrayList<>());
        history.add(currentStateIndex, new LinkedList<>());
    }

    public void addFigure(Figure figure) {
        figuresBySelectProperty.get(false).add(figure);
        allFigures.add(figure);
        updateHistory();
    }

    public void undo(){
        Deque<Figure> previousState = null;
        if(canUndo()){
            previousState = history.get(--currentStateIndex);
            allFigures = previousState;
        }
    }

    public boolean canUndo(){
        return currentStateIndex > 0;
    }

    public boolean canRedo(){
        return currentStateIndex < history.size()-1;
    }

    public void redo(){
        Deque<Figure> previousState = null;
        if(canRedo()){
            previousState = history.get(++currentStateIndex);
            allFigures = previousState;
        }
    }

    public void updateHistory(){
        // Add current canvas state to history
        history.add(++currentStateIndex, new LinkedList<>());
        history.get(currentStateIndex).addAll(allFigures);
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
        updateHistory();
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

    public void bringToFront(List<Figure> figures){
        removeFigures(figures);
        for(Figure figure : figures){
            // Draws from back to front, first element to last respectively
            allFigures.addLast(figure);
        }
        updateHistory();
    }

    public void sendToBack(List<Figure> figures){
        removeFigures(figures);
        for(Figure figure : figures){
            // Draws from back to front, first element to last respectively
            allFigures.addFirst(figure);
        }
        updateHistory();
    }

    public void selectFigure(Figure figure){
        figure.select();
        removeFigure(figure, false);
        figuresBySelectProperty.get(true).add(figure);
    }

    public void unselectFigure(Figure figure){
        figure.unselect();
        removeFigure(figure, true);
        figuresBySelectProperty.get(false).add(figure);
    }

    public void unSelectAllFigures(){
        for(Figure figure : figures()){
            if(figure.isSelected()){
                unselectFigure(figure);
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

    public void moveSelectedFigures(double x, double y) {
        // Each figure must know how to move
        for(Figure figure : getSelectedFigures()){
            figure.move(x, y);
        }
    }
}
