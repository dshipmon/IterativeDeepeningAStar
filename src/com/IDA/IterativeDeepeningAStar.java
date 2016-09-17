package com.IDA;

import java.util.Stack;

/**
 * Created by Dominique Shipmon on 09-Sep-16.
 * Iterative Deepening A Star search algorithm for a NxN puzzle with 1 empty. This version uses the
 * Manhattan Heuristic. Performance can be increased through the usage of Linear Conflict
 * and Manhattan Heuristic.
 * References:
 *   http://courses.cs.washington.edu/courses/cse473/06au/homework/korf96.pdf
 *   https://courses.cs.washington.edu/courses/csep573/11wi/lectures/03-hsearch.pdf
 *   http://www.ibm.com/developerworks/library/j-ai-search/
 * Process Adapted from PseudoCode: https://en.wikipedia.org/wiki/Iterative_deepening_A*
 *
 * @author Dominique Shipmon
 */
public class IterativeDeepeningAStar {
    private Puzzle puzzleGoal = new Puzzle();

    public Puzzle IDA(Puzzle root){
        puzzleGoal.getStartConfig(root.getDimension(), 1);
        int additiveBound = root.calculateManhattanSum();
        Puzzle solved;
        while (true){
            int bound = additiveBound;
            solved = depthSearch(root, bound);
            additiveBound += 2;
            if (solved != null){
                return solved;
                break;
            }
        }
    }


    private Puzzle depthSearch(Puzzle node, int bound){
        if (node.equals(puzzleGoal)){
            return node;
        }
        Puzzle[] childArr = createPossibleStates(node);
        for (Puzzle currentConfiguration: childArr) {
            if (currentConfiguration!=null) {
                if (currentConfiguration.equals(puzzleGoal)) {
                    return currentConfiguration;
                }
                else {
                    int cost = currentConfiguration.calculateManhattanSum() + currentConfiguration.getLength();
                    if (cost <= bound) {
                        Puzzle state1 = depthSearch(currentConfiguration, bound);
                        if (state1 != null) {
                            return state1;
                        }
                    }
                }
            }
        }
        return null;
    }

    private Object[] opSelector(int num, int x, int y){
        Direction retop = null;
        PuzzlePoint point = null;
        switch (num){
            case 0:
                retop = Direcion.EAST;
                point = new PuzzlePoint(x, y - 1);
                break;
            case 1:
                retop = Direction.NORTH;
                point = new PuzzlePoint(x + 1, y);
                break;
            case 2:
                retop = Direction.WEST;
                point = new PuzzlePoint(x, y + 1);
                break;
            case 3:
                retop = Direction.SOUTH;
                point = new PuzzlePoint(x - 1, y);
                break;
        }
        return new Object[]{retop, point};
    }

    private Puzzle[] createPossibleStates(Puzzle node){
        Puzzle[] childArr = new Puzzle[4];
        PuzzlePoint zeroLoc = node.getZeroLocatios()[0];
        Object[] opArr;
        PuzzlePoint opPoint;
        Direction direction;
        for (int i = 0; i < 4; i++) {
            opArr = opSelector(i, zeroLoc.getRow(), zeroLoc.getColumn());
            direction = (Direction) opArr[0];
            opPoint = (PuzzlePoint) opArr[1];
            if (!(opPoint.getRow() < 0 || opPoint.getColumn() < 0)) {
                if (opPoint.getRow() < node.getDimension() && opPoint.getColumn() < node.getDimension()) {
                    Puzzle newState = (Puzzle) node.move(opPoint.getRow(), opPoint.getColumn(), operation);
                    if (newState != null) {
                        childArr[i] = newState;
                    }
                }
            }
        }
        return childArr;
    }
}
