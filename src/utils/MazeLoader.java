package utils;

import model.Cell;
import model.Maze;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MazeLoader {
    public static Maze loadFromFile(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }

        int rows = lines.size();
        int cols = lines.get(0).length();
        Maze maze = new Maze(rows, cols);

        for (int i = 0; i < rows; i++) {
            String line = lines.get(i);
            for (int j = 0; j < cols; j++) {
                maze.setCell(i, j, Cell.CellType.fromSymbol(line.charAt(j)));
            }
        }

        return maze;
    }

    public static void saveToFile(Maze maze, String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.print(maze.toString());
        }
    }

    public static Maze generateRandom(int rows, int cols, double wallDensity) {
        if (rows < 3 || cols < 3) {
            throw new IllegalArgumentException("Le labyrinthe doit être au moins de taille 3x3");
        }

        if (wallDensity < 0.0 || wallDensity > 1.0) {
            throw new IllegalArgumentException("La densité doit être entre 0 et 1");
        }

        Maze maze = new Maze(rows, cols);
        Random random = new Random();

        // Remplir avec des passages d'abord
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze.setCell(i, j, Cell.CellType.PATH);
            }
        }

        // Ajouter les murs extérieurs
        for (int i = 0; i < rows; i++) {
            maze.setCell(i, 0, Cell.CellType.WALL);
            maze.setCell(i, cols-1, Cell.CellType.WALL);
        }
        for (int j = 0; j < cols; j++) {
            maze.setCell(0, j, Cell.CellType.WALL);
            maze.setCell(rows-1, j, Cell.CellType.WALL);
        }

        // Placer le point de départ (S) sur la gauche
        int startRow = 1 + random.nextInt(rows-2);
        maze.setCell(startRow, 1, Cell.CellType.START);

        // Placer le point d'arrivée (E) sur la droite
        int endRow = 1 + random.nextInt(rows-2);
        maze.setCell(endRow, cols-2, Cell.CellType.END);

        // Calculer le nombre de murs à ajouter pour respecter la densité
        int innerCells = (rows-2) * (cols-2); // Cellules intérieures totales
        int availableCells = innerCells - 2; // Soustraire S et E
        int wallsToAdd = (int)(availableCells * wallDensity);

        // Liste des positions disponibles pour les murs
        List<int[]> availablePositions = new ArrayList<>();
        for (int i = 1; i < rows-1; i++) {
            for (int j = 1; j < cols-1; j++) {
                Cell cell = maze.getCell(i, j);
                if (cell.getType() != Cell.CellType.START && 
                    cell.getType() != Cell.CellType.END) {
                    availablePositions.add(new int[]{i, j});
                }
            }
        }

        // Mélanger les positions disponibles
        for (int i = availablePositions.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int[] temp = availablePositions.get(i);
            availablePositions.set(i, availablePositions.get(j));
            availablePositions.set(j, temp);
        }

        // Ajouter les murs
        int wallsAdded = 0;
        for (int[] pos : availablePositions) {
            if (wallsAdded >= wallsToAdd) break;
            
            int row = pos[0];
            int col = pos[1];
            
            // Ne pas bloquer complètement le chemin entre S et E
            maze.setCell(row, col, Cell.CellType.WALL);
            if (!ensurePathExists(maze, startRow, 1, endRow, cols-2)) {
                maze.setCell(row, col, Cell.CellType.PATH);
            } else {
                wallsAdded++;
            }
        }

        return maze;
    }

    private static boolean ensurePathExists(Maze maze, int startRow, int startCol, int endRow, int endCol) {
        // Vérifier si un chemin existe déjà en utilisant DFS
        boolean[][] visited = new boolean[maze.getRows()][maze.getCols()];
        return dfsPathCheck(maze, startRow, startCol, endRow, endCol, visited);
    }

    private static boolean dfsPathCheck(Maze maze, int row, int col, int endRow, int endCol, boolean[][] visited) {
        if (row == endRow && col == endCol) {
            return true;
        }

        visited[row][col] = true;
        int[][] directions = {{-1,0}, {1,0}, {0,-1}, {0,1}}; // haut, bas, gauche, droite

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (isValidMove(maze, newRow, newCol, visited)) {
                if (dfsPathCheck(maze, newRow, newCol, endRow, endCol, visited)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isValidMove(Maze maze, int row, int col, boolean[][] visited) {
        return row > 0 && row < maze.getRows()-1 && 
               col > 0 && col < maze.getCols()-1 && 
               !visited[row][col] && 
               maze.getCell(row, col).isWalkable();
    }

    private static boolean isAdjacentToStartOrEnd(Maze maze, int row, int col) {
        int[][] directions = {{-1,0}, {1,0}, {0,-1}, {0,1}}; // haut, bas, gauche, droite
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (newRow >= 0 && newRow < maze.getRows() && newCol >= 0 && newCol < maze.getCols()) {
                Cell cell = maze.getCell(newRow, newCol);
                if (cell.getType() == Cell.CellType.START || cell.getType() == Cell.CellType.END) {
                    return true;
                }
            }
        }
        return false;
    }
} 