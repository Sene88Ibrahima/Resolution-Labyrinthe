package algorithms;

import model.Cell;
import model.Maze;
import java.util.*;

public class BFSSolver implements MazeSolver {
    private int stepCount;
    private long executionTime;

    @Override
    public List<Cell> solve(Maze maze) {
        stepCount = 0;
        long startTime = System.currentTimeMillis();
        
        Queue<Cell> queue = new LinkedList<>();
        Map<Cell, Cell> parentMap = new HashMap<>();
        
        Cell start = maze.getStart();
        Cell end = maze.getEnd();
        
        queue.offer(start);
        start.setVisited(true);
        
        while (!queue.isEmpty()) {
            Cell current = queue.poll();
            stepCount++;
            
            if (current == end) {
                executionTime = System.currentTimeMillis() - startTime;
                return reconstructPath(parentMap, start, end);
            }
            
            for (Cell neighbor : maze.getNeighbors(current)) {
                if (!neighbor.isVisited()) {
                    queue.offer(neighbor);
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
        return "BFS (Breadth-First Search)";
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