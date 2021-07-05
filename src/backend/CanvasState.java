package backend;

import backend.model.*;

import java.util.*;

public class CanvasState {

    private final Map<String, List<Figure>> figuresByKind= new HashMap<>();
    private final List<Figure1D> figures1D = new ArrayList<>();
    private final List<Figure2D> figures2D = new ArrayList<>();

    private final Map<String, List<Figure>> selectedFiguresByKind= new HashMap<>();
    private final List<Figure2D> selectedFigures2D = new ArrayList<>();
    private final List<Figure1D> selectedFigures1D = new ArrayList<>();

    public void addFigure(Figure figure) {
        figuresByKind.putIfAbsent(figure.getClass().getSimpleName(), new ArrayList<>());
        figuresByKind.get(figure.getClass().getSimpleName()).add(figure);
    }

    public List<Figure> figures() {
        List<Figure> figures = new ArrayList<>();
        for(Map.Entry<String, List<Figure>> figure : figuresByKind.entrySet()){
            figures.addAll(figure.getValue());
        }
        return figures;
    }

    public List<Figure1D> getFigures1D() {
        return figures1D;
    }

    public List<Figure2D> getFigures2D() {
        return figures2D;
    }

    public List<Figure> getSelectedFigures() {
        List<Figure> selectedFigures = new ArrayList<>();
        for(Figure figure : figures()){
            if(figure.isSelected()){
                selectedFigures.add(figure);
            }
        }
        return selectedFigures;
    }

    public List<Figure2D> getSelectedFigures2D() {
        return selectedFigures2D;
    }
}
