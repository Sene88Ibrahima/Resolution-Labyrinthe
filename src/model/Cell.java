package model;

public class Cell {
    private int row;
    private int col;
    private CellType type;
    private boolean visited;

    public enum CellType {
        WALL('#'),
        PATH('='),
        START('S'),
        END('E'),
        SOLUTION('+');

        private final char symbol;

        CellType(char symbol) {
            this.symbol = symbol;
        }

        public char getSymbol() {
            return symbol;
        }

        public static CellType fromSymbol(char symbol) {
            for (CellType type : CellType.values()) {
                if (type.getSymbol() == symbol) {
                    return type;
                }
            }
            return WALL;
        }
    }

    public Cell(int row, int col, CellType type) {
        this.row = row;
        this.col = col;
        this.type = type;
        this.visited = false;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public CellType getType() {
        return type;
    }

    public void setType(CellType type) {
        this.type = type;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isWalkable() {
        // Une cellule est traversable si c'est un chemin, le départ, l'arrivée ou une solution
        return type == CellType.PATH || type == CellType.START || 
               type == CellType.END || type == CellType.SOLUTION;
    }

    @Override
    public String toString() {
        return String.valueOf(type.getSymbol());
    }
} 