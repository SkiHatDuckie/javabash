package com.javabash.game;

public class Room extends GameObject {
    /**Room game object.
     * @param grid : The grid to place the game object in.
     * @param col : X position on grid.
     * @param row : Y position on grid. */
    public Room(Grid grid, int col, int row) {
        super(grid, col, row);
    }
}
