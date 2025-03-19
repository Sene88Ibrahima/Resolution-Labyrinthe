package model;

import java.util.ArrayList;
import java.util.List;

public class Maze {
    private Cell[][] grid;
    private Cell start;
    private Cell end;
    private int rows;
    private int cols;

    public Maze(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Cell[rows][cols];
        initializeGrid();
    }

    // Constructeur de copie
    public Maze(Maze other) {
        this.rows = other.rows;
        this.cols = other.cols;
        this.grid = new Cell[rows][cols];
        
        // Copier chaque cellule
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Cell originalCell = other.grid[i][j];
                this.grid[i][j] = new Cell(i, j, originalCell.getType());
                if (originalCell == other.start) {
                    this.start = this.grid[i][j];
                }
                if (originalCell == other.end) {
                    this.end = this.grid[i][j];
                }
            }
        }
    }

    private void initializeGrid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new Cell(i, j, Cell.CellType.WALL);
            }
        }
    }

    public void setCell(int row, int col, Cell.CellType type) {
        grid[row][col].setType(type);
        if (type == Cell.CellType.START) {
            start = grid[row][col];
        } else if (type == Cell.CellType.END) {
            end = grid[row][col];
        }
    }

    public Cell getCell(int row, int col) {
        return grid[row][col];
    }

    public Cell getStart() {
        return start;
    }

    public Cell getEnd() {
        return end;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public List<Cell> getNeighbors(Cell cell) {
        List<Cell> neighbors = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // haut, bas, gauche, droite

        for (int[] dir : directions) {
            int newRow = cell.getRow() + dir[0];
            int newCol = cell.getCol() + dir[1];

            if (isValidPosition(newRow, newCol) && grid[newRow][newCol].isWalkable()) {
                neighbors.add(grid[newRow][newCol]);
            }
        }

        return neighbors;
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    public void resetVisited() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j].setVisited(false);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sb.append(grid[i][j].toString());
            }
            sb.append("\n");
        }
        return sb.toString();
    }
} 