package com.javabash.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;

public class TerminalScreen implements Screen {
    final Main game;

    private Terminal terminal;
    /**Reference to {@code Terminal}'s virtual output buffer. */
    private char[] virtualOutput;
    /**The maximum number of characters that should be drawn in one row. */
    private int terminalWidth;
    private TerminalInputProcessor terminalInputProcessor;

    public TerminalScreen(final Main game) {
        this.game = game;

        // Set the buffer size to some abitrary large number.
        terminal = new Terminal(10000000);
        virtualOutput = terminal.getOutputBuffer();
        terminalWidth = 20;

        terminalInputProcessor = new TerminalInputProcessor(terminal);
        Gdx.input.setInputProcessor(terminalInputProcessor);
    }

    @Override
    public void render(float delta) {
        // Set screen background color
        ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);

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
                if (rowStr.length() == terminalWidth) {
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

    @Override
    public void resize(int width, int height) {}

    @Override
    public void hide() {}

    @Override
    public void show() {}

    @Override
    public void pause() {}

    @Override 
    public void resume() {}

    @Override
    public void dispose() {}
}