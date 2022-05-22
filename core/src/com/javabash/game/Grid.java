package com.javabash.game;

public class Grid {
    private final int width;
    private final int height;
    private GameObject[][] grid;

    /**The grid used to initialize the rooms, and store and
     * update the position of game objects such as the player's robot, the exit,
     * and all mines. It is recommended that you call {@code Grid.loadNewGrid()} after calling 
     * the constructor.
     * @param width : Number of columns (final; should not be changed after initialization)
     * @param heigth : Number of rows (final; should not be changed after initialization) */
    public Grid(final int width, final int height) {
        this.width = width;
        this.height = height; 
        grid = new GameObject[height][width];
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    /**Fills {@code grid} with {@code Room} objects. */
    public void loadNewGrid() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                new Room(this, col, row);
            }
        }
    }

    /**Gets the game object at ({@code col}, {@code row}). 
     * Note that (0, 0) is the top left corner of the grid. */
    public GameObject getCell(int col, int row) throws IndexOutOfBoundsException {
        return grid[row][col];
    }

    /**Loads {@code obj} onto the grid at ({@code col}, {@code row}).
     * This method will overwrite whatever game object is at ({@code col}, {@code row}). */
    public void loadObject(GameObject obj, int col, int row) throws IndexOutOfBoundsException {
        grid[row][col] = obj;
    }
}
