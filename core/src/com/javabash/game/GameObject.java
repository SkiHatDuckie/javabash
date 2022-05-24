package com.javabash.game;

public class GameObject {
    private int col;
    private int row;
    private Grid grid;

    public GameObject(Grid grid, int col, int row) {
        this.col = col;
        this.row = row;
        this.grid = grid;
        this.grid.loadObject(this, col, row);
    }

    public int getCol() { return col; }
    public int getRow() { return row; }
    public Grid getGrid() { return grid; }

    public void updatePosition(int newCol, int newRow) {
        col = newCol;
        row = newRow;
        grid.loadObject(this, col, row);
    }
}
