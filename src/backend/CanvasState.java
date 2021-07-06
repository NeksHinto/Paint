package backend;

import backend.model.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CanvasState {

    private final Deque<Figure> allFigures = new LinkedList<>();
    private final Map<Boolean, List<Figure1D>> figures1DBySelectProperty = new HashMap<>();
    private final Map<Boolean, List<Figure2D>> figures2DBySelectProperty = new HashMap<>();
    private final List<Figure1D> figures1D = new ArrayList<>();
    private final List<Figure2D> figures2D = new ArrayList<>();

    public void addFigure1D(Figure1D figure) {
        figures1DBySelectProperty.putIfAbsent(false, new ArrayList<>());
        figures1DBySelectProperty.get(false).add(figure);
        allFigures.add(figure);
        figures1D.add(figure);
    }

    public void addFigure2D(Figure2D figure){
        figures2DBySelectProperty.putIfAbsent(false, new ArrayList<>());
        figures2DBySelectProperty.get(false).add(figure);
        allFigures.add(figure);
        figures2D.add(figure);
    }

    public boolean removeFigure(Figure figure){
        boolean figureRemoved;
        figureRemoved = figures1DBySelectProperty.get(true).remove(figure);
        figureRemoved = figures2DBySelectProperty.get(true).remove(figure);
        figureRemoved = figures1D.remove(figure);
        figureRemoved = figures2D.remove(figure);
        figureRemoved = allFigures.remove(figure);

        return figureRemoved;
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

    public List<Figure1D> getFigures1D() {
        return figures1D;
    }

    public List<Figure2D> getFigures2D() {
        return figures2D;
    }

    public List<Figure1D> getSelectedFigures1D() {
        return figures1DBySelectProperty.getOrDefault(true, new ArrayList<>());
    }

    public List<Figure2D> getSelectedFigures2D() {
        return figures2DBySelectProperty.getOrDefault(true, new ArrayList<>());
    }

    public List<Figure> getSelectedFigures() {
        return Stream.of(getSelectedFigures1D(), getSelectedFigures2D())
                .flatMap(x -> x.stream())
                .collect(Collectors.toList());
    }

    public boolean isAFigureSelected(){
        return !getSelectedFigures().isEmpty();
    }
}
