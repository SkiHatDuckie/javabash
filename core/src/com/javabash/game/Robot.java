package com.javabash.game;

public class Robot extends GameObject {
    /**Converts a {@code Direction} constant into a grid coordinates modifier. */
    private static final int[][] coordModifier = new int[][] {
        {0, -1}, {1, 0}, {0, 1}, {-1, 0}
    };

    private final int[] startPos;
    private boolean[][] prevPositions;

    /**Robot game object.
     * @param grid : The grid to place the game object in.
     * @param col : X position on grid.
     * @param row : Y position on grid. */
    public Robot(Grid grid, int col, int row) {
        super(grid, col, row);

        startPos = new int[] {col, row};
        prevPositions = new boolean[grid.getHeight()][grid.getWidth()];
        prevPositions[startPos[1]][startPos[0]] = true;
    }

    public boolean[][] getPrevPositions() { return prevPositions; }

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

            if (neighborCell instanceof Room) {
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
