package com.javabash.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

public class MapFile extends File {
    private Terminal terminal;
    /**Reference to {@code Terminal}'s virtual output buffer. */
    private char[] virtualOutput;
    /**The maximum number of characters that should be drawn in one row. */
    private int terminalWidth;

    public MapFile(final Main game, String name) {
        super(game, name);

        // Set the buffer size to some abitrary large number.
        terminal = new Terminal(10000000);
        virtualOutput = terminal.getOutputBuffer();
        terminalWidth = 40;

        terminal.addCommand("quit", new Command() {
            public void execute(String[] args) {
                game.setScreen(game.terminalScreen);
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new TerminalInputProcessor(terminal));
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);

        renderGrid();
        game.batch.begin();
        renderVirtualTerminalRow();
        renderGridText();
        game.batch.end();
    }

    /**This method will only draw the latest row of output, as to avoid covering
     * up the map. */
    private void renderVirtualTerminalRow() {
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
    
        game.vt323Font.draw(game.batch, rows.get(rows.size() - 1), 10, 30);
    }

    /**Draws the game grid to screen, with game objects hidden from the player. */
    private void renderGrid() {
        Color[] dangerColors = {
            new Color(0.7f, 0.2f, 0.7f, 0.5f), new Color(0, 0.3f, 0.8f, 0.5f), new Color(0, 0.5f, 0.8f, 0.5f),
            new Color(0.2f, 0.9f, 0.6f, 0.5f), new Color(0.5f, 0.8f, 0.3f, 0.5f), new Color(0.9f, 0.75f, 0, 0.5f),
            new Color(0.8f, 0.6f, 0.2f, 0.5f), new Color(0.8f, 0, 0, 0.5f), new Color(0.8f, 0, 0.4f, 0.5f),
            new Color(1, 0.1f, 0, 1)
        };

        int[][] dangerGrid = game.grid.getDangerGrid();
        Rectangle cellRect = new Rectangle(0, 0, 20, 20);
        boolean[][] prevPositions = game.robot.getPrevPositions();

        for (int col = 0; col < game.grid.getWidth(); col++) {
            game.shape.begin(ShapeType.Line);
            game.shape.setColor(Color.LIGHT_GRAY);
            game.shape.line(
                48 + (cellRect.width * col), 
                Gdx.graphics.getHeight() - 48, 
                48 + (cellRect.width * col), 
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
                    48 + (cellRect.width * col), 
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
        for (int col = 0; col < game.grid.getWidth(); col++) {
            game.vt323Font.draw(
                game.batch, 
                col + "", 
                48 + (cellRect.width * col), 
                Gdx.graphics.getHeight() - 32.0f
            );
        }
        for (int row = 0; row < game.grid.getHeight(); row++) {
            game.vt323Font.draw(
                game.batch, 
                row + "", 
                28, 
                Gdx.graphics.getHeight() - 50.0f - (cellRect.height * row)
            );
        }
    }
}
