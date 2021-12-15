package CanvasState;

import backend.model.*;
import javafx.scene.paint.Color;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class CanvasStateTest {
    private Deque<Figure> myFigures = new LinkedList<>();
    private Deque<Figure> myFiguresExpect = new LinkedList<>();

    private final List<Deque<Figure>> myHistory = new ArrayList<>();
    private final List<Deque<Figure>> myHistoryExpect = new ArrayList<>();
    private int currentIdxMyHistory = 0;
    private int currentIdxMyHistoryExpect = 0;

    private final Map<Boolean, List<Figure>> myFiguresBySelected = new HashMap<>();
    private final Map<Boolean, List<Figure>> myFiguresBySelectedExpect = new HashMap<>();

    private List<Figure> figuresForTesting = new ArrayList<>();
    private Figure figForTesting = null;

    Color lineColor = Color.BLACK;
    Color fillColor = Color.YELLOW;
    double lineWidth = 1;

    @BeforeEach
    public void setUp() {
        myFiguresBySelected.putIfAbsent(false, new ArrayList<>());
        myFiguresBySelected.putIfAbsent(true, new ArrayList<>());

        Point pointFig1 = new Point(1.0, 2.0);
        Figure fig1 = new Ellipse(pointFig1, 5.0, 3.0, fillColor.toString(), lineColor.toString(), lineWidth);

        Point pointFig2 = new Point(3.0, 6.7);
        figForTesting = new Circle(pointFig2, 2.0, fillColor.toString(), lineColor.toString(), lineWidth);

        Point pointAFig3 = new Point(14.5, 2.3);
        Point pointBFig3 = new Point(23.4, 7.8);
        Figure fig3 = new Rectangle(pointAFig3, pointBFig3, fillColor.toString(), lineColor.toString(), lineWidth);

        Point pointAFig4 = new Point(1.5, 2.3);
        Point pointBFig4 = new Point(9.4, 7.8);
        Figure fig4 = new Square(pointAFig4, pointBFig4, fillColor.toString(), lineColor.toString(), lineWidth);

        Point pointAFig5 = new Point(10.3, 29.8);
        Point pointBFig5 = new Point(19.5, 40.8);
        Figure fig5 = new Line(pointAFig5, pointBFig5, fillColor.toString(), lineColor.toString(), lineWidth);

        figuresForTesting.add(fig1);
        figuresForTesting.add(figForTesting);
        figuresForTesting.add(fig3);
        figuresForTesting.add(fig4);
        figuresForTesting.add(fig5);

        myFiguresExpect.add(fig1);
        myFiguresExpect.add(figForTesting);
        myFiguresExpect.add(fig3);
        myFiguresExpect.add(fig4);
        myFiguresExpect.add(fig5);
        myFiguresBySelectedExpect.get(false).add(fig1);
        myFiguresBySelectedExpect.get(false).add(figForTesting);
        myFiguresBySelectedExpect.get(false).add(fig3);
        myFiguresBySelectedExpect.get(false).add(fig4);
        myFiguresBySelectedExpect.get(false).add(fig5);
        myHistoryExpect.add(0, myFiguresExpect);
    }

    @Test
    public void addFigureTest() {
        for(Figure figure : figuresForTesting){
            myFiguresBySelected.get(false).add(figure);
            myFigures.add(figure);
        }
        assertArrayEquals(myFiguresExpect.toArray(), myFigures.toArray());
        int expectedInitSizeSelected = 0;
        assertEquals(expectedInitSizeSelected, myFiguresBySelected.get(true).size());
        int expectedInitSizeNonSelected = 5;
        assertEquals(expectedInitSizeNonSelected, myFiguresBySelected.get(false).size());

        updateHistoryTest();
    }

    @Test
    public void undoTest(){
        //Simulate removing a figure from state
        myFiguresExpect.removeLast();
        myHistoryExpect.get(1).addAll(myFiguresExpect);

        Deque<Figure> previousState = null;
        if(canUndo()){
            previousState = myHistory.get(--currentIdxMyHistory);
            assertEquals(currentIdxMyHistoryExpect, currentIdxMyHistory);
            assertArrayEquals(myHistoryExpect.get(0).toArray(), myHistory.get(0).toArray());
            myFigures = previousState;
        }
    }

    private boolean canUndo(){
        return currentIdxMyHistory > 0;
    }

    private boolean canRedo(){
        return currentIdxMyHistory < myHistory.size()-1;
    }

    @Test
    public void redoTest(){
        Deque<Figure> previousState = null;
        if(canRedo()){
            previousState = myHistory.get(++currentIdxMyHistory);
            myFigures = previousState;
        }
    }

    @Test
    public void updateHistoryTest(){
        assertEquals(currentIdxMyHistoryExpect, currentIdxMyHistory);
        myHistory.add(currentIdxMyHistory, new LinkedList<>());
        currentIdxMyHistoryExpect++;
        myHistory.get(currentIdxMyHistory++).addAll(myFigures);
        assertEquals(currentIdxMyHistoryExpect, currentIdxMyHistory);
        assertArrayEquals(myHistoryExpect.get(0).toArray(), myHistory.get(0).toArray());
    }

    @Test
    public void removeFiguresTest(){
        Iterator<Figure> iter = myFigures.iterator();
        Object[] myFiguresArray = myFiguresExpect.toArray();
        int count = 0;
        while(iter.hasNext()){
            Figure toRemove = iter.next();
            assertEquals(myFiguresArray[count++], toRemove);
            if(shouldRemove(toRemove, figuresForTesting)){
                iter.remove();
            }
        }
    }

    @Test
    public void removeFigureTest(){
        boolean key = true;
        Object[] myFiguresBySelectedArray = myFiguresBySelectedExpect.get(key).toArray();
        int count = 0;

        boolean figureRemoved = false;
        Iterator<Figure> iter = myFiguresBySelected.get(key).iterator();
        while(iter.hasNext()){
            Figure toRemove = iter.next();
            assertEquals(myFiguresBySelectedArray[count++], toRemove);
            if(toRemove.equals(figForTesting)){
                iter.remove();
                figureRemoved = true;
            }
        }
    }

    private boolean shouldRemove(Figure toRemove, List<Figure> figuresToRemove){
        for(Figure figure : figuresToRemove){
            if(toRemove.equals(figure)){
                return true;
            }
        }
        return false;
    }

    @Test
    public void bringToFront(){
        Object[] myFiguresArray = myFiguresExpect.toArray();
        int index = myFiguresArray.length;
        Figure figForTest2 = new Line(new Point(2.0, 1.0), new Point(7.8, 9.3), fillColor.toString(), lineColor.toString(), lineWidth);
        myFiguresArray[index] = figForTest2;
        myFigures.addLast(figForTest2);
        assertEquals(myFiguresArray[index], myFigures.getLast());
    }

    @Test
    public void sendToBack(){
        Object[] myFiguresArray = myFiguresExpect.toArray();
        Figure figForTest2 = new Line(new Point(2.0, 1.0), new Point(7.8, 9.3), fillColor.toString(), lineColor.toString(), lineWidth);
        myFiguresArray[0] = figForTest2;
        myFigures.addFirst(figForTest2);
        assertEquals(myFiguresArray[0], myFigures.getFirst());
    }

    @Test
    public void selectFigure(){
        figForTesting.select();
        myFiguresBySelected.get(true).add(figForTesting);
    }

    @Test
    public void unselectFigure(){
        figForTesting.unselect();
        assertEquals(false, figForTesting.isSelected);
    }

    @Test
    public void unSelectAllFiguresTest(){
        for(Figure figure : figuresForTesting){
            if(figure.isSelected()){
                figure.unselect();
                assertEquals(false, figure.isSelected);
            }
        }
    }

    private List<Figure> getSelectedFiguresTest() {
        return myFiguresBySelected.getOrDefault(true, new ArrayList<>());
    }

    @Test
    public void isAFigureSelectedTest(){
        myFiguresBySelected.get(true).addAll(new ArrayList<>());
        assertTrue(figForTesting.isSelected == !getSelectedFiguresTest().isEmpty());
    }

    @Test
    public void moveSelectedFiguresTest() {
        int x = 1;
        int y = 2;
        figForTesting.move(x, y);
        Point pointFig2 = new Point(3.0+x, 6.7+y);
        Figure expectedFigureToMove = new Circle(pointFig2, 2.0, fillColor.toString(), lineColor.toString(), lineWidth);
        assertArrayEquals(expectedFigureToMove.getPoints(), figForTesting.getPoints());
    }
}
