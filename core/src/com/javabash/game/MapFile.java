package com.javabash.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

public class MapFile extends File {
    /**Reference to {@code Terminal}'s virtual output buffer. */
    private char[] virtualOutput;
    /**The maximum number of characters that should be drawn in one row. */
    private int terminalWidth;
    /**Keeps track of which grid cells are flagged. */
    private boolean[][] isFlagged;

    public MapFile(final Main game, String name) {
        super(game, name, new Terminal(10000000));

        isFlagged = new boolean[game.grid.getHeight()][game.grid.getWidth()];

        // Set the buffer size to some abitrary large number.
        // {@code terminal} is initialized and stored in the {@File} parent class.
        virtualOutput = terminal.getOutputBuffer();
        terminalWidth = 45;

        terminal.addCommand("quit", new Command(": exits from file") {
            public void execute(String[] args) {
                game.setScreen(game.terminalScreen);
            }
        });
        terminal.addCommand("flag", new Command("[x] [y]: flag or unflag a room") {
            public void execute(String[] args) {
                int col = -1;
                int row = -1;
                try {
                    col = Integer.parseInt(args[0]);
                    row = Integer.parseInt(args[1]);
                } catch(IndexOutOfBoundsException | NumberFormatException ex) {
                    if (ex instanceof NumberFormatException) {
                        terminal.writeString(
                            "Error when using command \"flag\": args 'column' or 'row' do not represent " +
                            "integers.\n"
                        );
                    } else {
                        terminal.writeString(
                            "Error: Command \"flag\" is missing arguments \"column\" or \"row\".\n"
                        );
                    }
                }
                if (col > -1 && row > -1) {
                    try {
                        isFlagged[row][col] = !isFlagged[row][col];
                    } catch (IndexOutOfBoundsException ex) {
                        terminal.writeString(
                            "Error when using command \"flag\": args 'column' or 'row' are out of bounds.\n"
                        );
                    }
                }
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new TerminalInputProcessor(terminal));
    }

    @Override
    public void render(float delta) {
        // Set screen background color
        ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);

        renderGrid();
        game.batch.begin();
        renderVirtualTerminal();
        renderGridText();
        game.batch.end();
    }

    /**Draw the virtual terminal's output to the screen. */
    private void renderVirtualTerminal() {
        float fontHeight = game.vt323Font.getLineHeight();

        ArrayList<String> rows = new ArrayList<String>();
        String rowStr = "";

        for (int i = 0; i < terminal.getLastCharacterIndex() + 1; i++) {
            if (virtualOutput[i] == '\n' || rowStr.length() == terminalWidth) {
                rows.add(rowStr);
                rowStr = "";
                if (virtualOutput[i] != '\n') {
                    rowStr += virtualOutput[i];
                }
            } else {
                rowStr += virtualOutput[i];
            }
        }
        if (!rowStr.isEmpty()) {
            rows.add(rowStr);
        }

        for (int row = rows.size() - 1; row >= 0; row--) {
            game.vt323Font.draw(game.batch, rows.get(row), 10, 30 + fontHeight * (rows.size() - 1 - row));
        }
    }

    /**Draws the game grid to screen, with game objects hidden from the player. */
    private void renderGrid() {
        Color[] dangerColors = {
            new Color(0.7f, 0.2f, 0.7f, 0.5f), new Color(0, 0.3f, 0.8f, 0.5f), new Color(0, 0.5f, 0.8f, 0.5f),
            new Color(0.2f, 0.9f, 0.6f, 0.5f), new Color(0.5f, 0.8f, 0.3f, 0.5f), 
            new Color(0.9f, 0.75f, 0, 0.5f), new Color(0.8f, 0.6f, 0.2f, 0.5f), new Color(0.8f, 0, 0, 0.5f), 
            new Color(0.8f, 0, 0.4f, 0.5f), new Color(1, 0.1f, 0, 1)
        };

        int[][] dangerGrid = game.grid.getDangerGrid();
        Rectangle cellRect = new Rectangle(0, 0, 20, 20);
        boolean[][] prevPositions = game.robot.getPrevPositions();

        for (int col = 0; col < game.grid.getWidth(); col++) {
            game.shape.begin(ShapeType.Line);
            game.shape.setColor(Color.LIGHT_GRAY);
            game.shape.line(
                488 + (cellRect.width * col), 
                Gdx.graphics.getHeight() - 48, 
                488 + (cellRect.width * col), 
                Gdx.graphics.getHeight() - 28
            );
            game.shape.end();
        }
        for (int row = 0; row < game.grid.getHeight(); row++) {
            for (int col = 0; col < game.grid.getWidth(); col++) {
                game.shape.begin(ShapeType.Filled);
                if (prevPositions[row][col]) {
                    game.shape.setColor(dangerColors[dangerGrid[row][col]]);
                } else {
                    game.shape.setColor(new Color(0.4f, 0.4f, 0.4f, 0.5f));
                }
                game.shape.rect(
                    488 + (cellRect.width * col), 
                    Gdx.graphics.getHeight() - 48 - cellRect.height - (cellRect.height * row), 
                    cellRect.width,
                    cellRect.height 
                );
                game.shape.end();
            }
        }
    }

    /**Draws any text onto the grid. */
    private void renderGridText() {
        Rectangle cellRect = new Rectangle(0, 0, 20, 20);
        for (int row = 0; row < game.grid.getHeight(); row++) {
            game.vt323Font.draw(
                game.batch, 
                row + "", 
                468, 
                Gdx.graphics.getHeight() - 50.0f - (cellRect.height * row)
            );
            for (int col = 0; col < game.grid.getWidth(); col++) {
                game.vt323Font.draw(
                    game.batch, 
                    col + "", 
                    488 + (cellRect.width * col), 
                    Gdx.graphics.getHeight() - 32.0f
                );
                if (isFlagged[row][col]) {
                    game.vt323Font.draw(
                        game.batch, 
                        "X", 
                        490 + (cellRect.width * col), 
                        Gdx.graphics.getHeight() - 50.0f - (cellRect.height * row)
                    );
                }
            }
        }
    }
}
