package com.javabash.game;

import java.util.ArrayList;

public class Grid {
    private final int width;
    private final int height;
    private GameObject[][] grid;
    /**A grid with the same dimensions as {@code grid}, that gives each cell an integer
     * value based on the number of nearby cells with a {@code Mine} game object.
     * Cells with a {@code Mine} are assigned the integer 9. */
    private int[][] dangerGrid;
    /**Used as dynamic memory for the recursive function, {@code isExitAdjacent}. */
    private ArrayList<ArrayList<Integer>> searchedCells;
    /**Percentage of grid cells that contain a {@code Mine}. */
    private double minePercentage;

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
        searchedCells = new ArrayList<ArrayList<Integer>>();
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
        minePercentage = percent;
    }

    /**Recursively check every cell that isn't {@code Mine} to see if {@code Exit}
     * can be found.
     * @param obj : The gameObject on the grid to check.
     * @return Returns 1 if an {@code Exit} object was found. (0 = none found) */
    public int isExitAdjacent(GameObject obj) {
        int[][] adjacentCells = {
            {obj.getRow() - 1, obj.getCol()}, {obj.getRow(), obj.getCol() + 1},
            {obj.getRow() + 1, obj.getRow()}, {obj.getRow(), obj.getCol() - 1}
        };

        ArrayList<Integer> temp = new ArrayList<Integer>();

        for (int[] cell : adjacentCells) {
            temp.add(cell[0]);
            temp.add(cell[1]);
            try {
                if (getCell(cell[1], cell[0]) instanceof Exit) {
                    return 1;
                } else if (!(getCell(cell[1], cell[0]) instanceof Mine)
                           && !searchedCells.contains(temp)) {
                    searchedCells.add(temp);
                    return isExitAdjacent(getCell(cell[1], cell[0]));
                }
            } catch (IndexOutOfBoundsException ex) {}
            temp.clear();
        }
        return 0;
    }

    /** Checks if the grid is possible to beat, and shuffles the mine placement if not.
     * <br><br>
     * Precondition : Grid has been loaded.
     * @return An array with the new robot and exit game objects after shuffle, respectively. */
    public GameObject[] shuffleUntilPossible(Robot robot, Exit exit) {
        int res = 0;
        do {
            searchedCells.clear();
            res = isExitAdjacent(robot);
            if (res == 0) {
                loadNewGrid();
                robot = new Robot(this, robot.getCol(), robot.getRow(), robot.getNumLives());
                exit = new Exit(this, exit.getCol(), exit.getRow());
                placeMines(minePercentage);
            }
            System.out.println(res);
        } while (res == 0);
        return new GameObject[] {robot, exit};
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
