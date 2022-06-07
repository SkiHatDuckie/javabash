package com.javabash.game;

public class Robot extends GameObject {
    /**Converts a {@code Direction} constant into a grid coordinates modifier. */
    private static final int[][] coordModifier = new int[][] {
        {0, -1}, {1, 0}, {0, 1}, {-1, 0}
    };

    private final int[] startPos;
    private boolean[][] prevPositions;
    private int numLives;

    /**Robot game object.
     * @param grid : The grid to place the game object in.
     * @param col : X position on grid.
     * @param row : Y position on grid.
     * @param numLives : The total number of lives the player has. */
    public Robot(Grid grid, int col, int row, int numLives) {
        super(grid, col, row);

        startPos = new int[] {col, row};
        prevPositions = new boolean[grid.getHeight()][grid.getWidth()];

        prevPositions[startPos[1]][startPos[0]] = true;
        for (int i = 0; i < coordModifier.length; i++) {
            try {
                prevPositions[startPos[1] + coordModifier[i][1]][startPos[0] + coordModifier[i][0]] = true;
            } catch (IndexOutOfBoundsException ex) {}  // Ignore exception
        }

        this.numLives = numLives;
    }

    public boolean[][] getPrevPositions() { return prevPositions; }
    public int getNumLives() { return numLives; };

    /**Attempts to move the robot to the cell in the specified direction.
     * <br><br>
     * Precondition : {@code direction} represents a valid direction.
     * @param direction : The relative direction of the cell to move to on a 2d array.
     * @return An integer representing the result of calling this method. <br><br>
     * 0 = Robot moved to cell without issue. <br><br>
     * -1= Unable to move to specified cell. <br><br>
     * -2= Robot moved to cell containing {@code Mine}. <br><br>
     * 1 = Robot moved to cell containing {@code Exit}. */
    public int move(int direction) {
        try {
            GameObject neighborCell = getNeighborCell(direction);
            if (numLives <= 0) {
                return -1;
            } else if (neighborCell instanceof Room) {
                makeMove(direction);
                return 0;
            } else if (neighborCell instanceof Exit) {
                return 1;
            } else {
                dieIGuess();
                return -2;
            }
        } catch (IndexOutOfBoundsException ex) {
            return -1;
        }
    }

    public void decrementNumLives() {
        numLives--;
    }

    /**Gets the game object occupying the neighboring cell in direction {@code direction}. */
    private GameObject getNeighborCell(int direction) throws IndexOutOfBoundsException {
        return getGrid().getCell(
            getCol() + coordModifier[direction][0], 
            getRow() + coordModifier[direction][1]
        );
    }

    /**Moves the robot to the cell in the specified direction, and updates 
     * the grid accordingly. */
    private void makeMove(int direction) {
        int[] prevPos = {getCol(), getRow()};
        updatePosition(
            getCol() + coordModifier[direction][0], 
            getRow() + coordModifier[direction][1]
        );
        prevPositions[getRow()][getCol()] = true;
        new Room(getGrid(), prevPos[0], prevPos[1]);
    }

    /**Moves the robot back to the initial cell on the grid. */
    private void dieIGuess() {
        new Room(getGrid(), getCol(), getRow());
        updatePosition(startPos[0], startPos[1]);
    }
}
