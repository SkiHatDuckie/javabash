package com.javabash.game;

public class GameObject {
    private int col;
    private int row;

    public GameObject(Grid grid, int col, int row) {
        this.col = col;
        this.row = row;
        grid.loadObject(this, col, row);
    }

    public int getGridX() { return col; }
    public int getGridY() { return row; }
}
