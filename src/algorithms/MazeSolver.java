package algorithms;

import model.Cell;
import model.Maze;
import java.util.List;

public interface MazeSolver {
    List<Cell> solve(Maze maze);
    String getName();
    int getStepCount();
    long getExecutionTime();
} 