package com.javabash.game;

public class Grid {
    private final int width;
    private final int height;
    private GameObject[][] grid;
    /**A grid with the same dimensions as {@code grid}, that gives each cell an integer
     * value based on the number of nearby cells with a {@code Mine} game object.
     * Cells with a {@code Mine} are assigned the integer 9. */
    private int[][] dangerGrid;

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
        dangerGrid = new int[height][width];
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int[][] getDangerGrid() { return dangerGrid; }

    /**Fills {@code grid} with {@code Room} objects. */
    public void loadNewGrid() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                new Room(this, col, row);
                dangerGrid[row][col] = 0;
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
        if (obj instanceof Mine || grid[row][col] instanceof Mine) {
            updateDangerGrid();
        }
        grid[row][col] = obj;
    }

    /**Turns a percentage of cells on the grid that are {@code Room}s into {@code Mine}s.
     * This method removes all {@code Mine}s that were previously on the grid before adding new ones.
     * @param percent : The percentage of cells that should be switched to mines.
     * (If there are 100 cells, and {@code percent} is 20, 20 {@code Mine}s will be added) */
    public void placeMines(double percent) {
        int numMines = (int) (width * height * (percent / 100));
        GameObject cell;
        int col;
        int row;
        for (int n = 0; n < numMines; n++) {
            do {
                col = (int) (Math.random() * width);
                row = (int) (Math.random() * height);
                cell = getCell(col, row); 
            } while (!(cell instanceof Room));
            new Mine(this, col, row);
        }
    }

    /** This method also checks to see if it is possible for the robot to get to the exit.
    * <br><br>
    * Precondition : A {@code Robot} and an {@code Exit} have been added to the grid. */
    public boolean isPossible(Robot robot, Exit exit) {
        // TODO: This method is not complete
        return false;
    }

    /**Updates the values of {@code dangerGrid} when a {@code Mine} is added to or removed
     * from the grid. */
    private void updateDangerGrid() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                dangerGrid[row][col] = 0;

                if (grid[row][col] instanceof Mine) {
                    dangerGrid[row][col] = 9;
                } else {
                    int[][] nearbyCells = {
                        {row - 1, col}, {row - 1, col + 1}, {row, col + 1}, {row + 1, col + 1},
                        {row + 1, col}, {row + 1, col - 1}, {row, col - 1}, {row - 1, col - 1}
                    };
                    for (int[] cell : nearbyCells) {
                        try {
                            if (grid[cell[0]][cell[1]] instanceof Mine) {
                                dangerGrid[row][col]++;
                            }
                        } catch (IndexOutOfBoundsException ex) {}  // Ignore the exception and continue.
                    }
                }
            }
        }
    }
}
