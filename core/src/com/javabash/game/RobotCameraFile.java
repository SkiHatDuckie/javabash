package com.javabash.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.ScreenUtils;

public class RobotCameraFile extends File {
    /**Reference to {@code Terminal}'s virtual output buffer. */
    private char[] virtualOutput;
    /**The maximum number of characters that should be drawn in one row. */
    private int terminalWidth;

    public RobotCameraFile(final Main game, String name) {
        super(game, name, new Terminal(10000000));

        // Set the buffer size to some abitrary large number.
        // {@code terminal} is initialized and stored in the {@File} parent class.
        virtualOutput = terminal.getOutputBuffer();
        terminalWidth = 40;

        terminal.addCommand("quit", new Command(": exits from file") {
            public void execute(String[] args) {
                game.setScreen(game.terminalScreen);
            }
        });
        terminal.addCommand("up", new Command(": move robot up") {
            public void execute(String[] args) {
                int result = game.robot.move(Direction.UP);
                processMove(result);
            }
        });
        terminal.addCommand("right", new Command(": move robot right") {
            public void execute(String[] args) {
                int result = game.robot.move(Direction.RIGHT);
                processMove(result);
            }
        });
        terminal.addCommand("down", new Command(": move robot down") {
            public void execute(String[] args) {
                int result = game.robot.move(Direction.DOWN);
                processMove(result);
            }
        });
        terminal.addCommand("left", new Command(": move robot left") {
            public void execute(String[] args) {
                int result = game.robot.move(Direction.LEFT);
                processMove(result);
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

        renderRoom();
        game.batch.begin();
        renderVirtualTerminal();
        game.batch.end();
    }

    /**Process the returned result of result of calling {@code game.robot.move()} */
    private void processMove(int result) {
        if (result == 1) {
            terminal.writeString("You found the exit! Close the game to restart.\n");
        } else if (result == -2) {
            game.robot.decrementNumLives();
            if (game.robot.getNumLives() > 0) {
                terminal.writeString(
                    "You hit a mine! You have " + game.robot.getNumLives() + " lives remaining.\n"
                );
            } else {
                terminal.writeString("Game over! You lost of your lives. Close the game to restart.\n");
            }
        }
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

    /**Draws the current room the robot is in. */
    private void renderRoom() {
        Color[] dangerColors = {
            new Color(0.7f, 0.2f, 0.7f, 0.5f), new Color(0, 0.3f, 0.8f, 0.5f), new Color(0, 0.5f, 0.8f, 0.5f),
            new Color(0.2f, 0.9f, 0.6f, 0.5f), new Color(0.5f, 0.8f, 0.3f, 0.5f), 
            new Color(0.9f, 0.75f, 0, 0.5f), new Color(0.8f, 0.6f, 0.2f, 0.5f), new Color(0.8f, 0, 0, 0.5f), 
            new Color(0.8f, 0, 0.4f, 0.5f), new Color(1, 0.1f, 0, 1)
        };

        game.shape.begin(ShapeType.Filled);
        game.shape.setColor(dangerColors[game.grid.getDangerGrid()[game.robot.getRow()][game.robot.getCol()]]);
        game.shape.rect(150, 150, 150, 150);
        game.shape.end();
    }
}
