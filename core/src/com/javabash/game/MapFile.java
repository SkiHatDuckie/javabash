package com.javabash.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
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
        ScreenUtils.clear(0.11f, 0.1f, 0.2f, 1);

        game.batch.begin();

        renderVirtualTerminal();

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

    /**Draw the game grid to screen, with {@code Mine}  */
}
