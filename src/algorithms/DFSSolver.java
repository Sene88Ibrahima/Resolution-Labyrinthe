package algorithms;

import model.Cell;
import model.Maze;
import java.util.*;

public class DFSSolver implements MazeSolver {
    private int stepCount;
    private long executionTime;

    @Override
    public List<Cell> solve(Maze maze) {
        stepCount = 0;
        long startTime = System.currentTimeMillis();
        
        Stack<Cell> stack = new Stack<>();
        Map<Cell, Cell> parentMap = new HashMap<>();
        
        Cell start = maze.getStart();
        Cell end = maze.getEnd();
        
        stack.push(start);
        start.setVisited(true);
        
        while (!stack.isEmpty()) {
            Cell current = stack.pop();
            stepCount++;
            
            if (current == end) {
                executionTime = System.currentTimeMillis() - startTime;
                return reconstructPath(parentMap, start, end);
            }
            
            for (Cell neighbor : maze.getNeighbors(current)) {
                if (!neighbor.isVisited()) {
                    stack.push(neighbor);
                    neighbor.setVisited(true);
                    parentMap.put(neighbor, current);
                }
            }
        }
        
        executionTime = System.currentTimeMillis() - startTime;
        return new ArrayList<>();
    }

    private List<Cell> reconstructPath(Map<Cell, Cell> parentMap, Cell start, Cell end) {
        List<Cell> path = new ArrayList<>();
        Cell current = end;
        
        while (current != start) {
            path.add(current);
            current = parentMap.get(current);
        }
        path.add(start);
        
        Collections.reverse(path);
        return path;
    }

    @Override
    public String getName() {
        return "DFS (Depth-First Search)";
    }

    @Override
    public int getStepCount() {
        return stepCount;
    }

    @Override
    public long getExecutionTime() {
        return executionTime;
    }
} 