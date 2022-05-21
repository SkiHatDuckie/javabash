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
    TerminalInputProcessor terminalInputProcessor;

    private final Files files;

    public TerminalScreen(final Main game) {
        this.game = game;
        files = new Files(new File[] {
            new MapFile(this.game, "map")
        });

        // Set the buffer size to some abitrary large number.
        terminal = new Terminal(10000000);
        virtualOutput = terminal.getOutputBuffer();
        terminalWidth = 40;

        terminal.addCommand("echo", new Command() {
            public void execute(String[] args) {
                for (String arg : args) {
                    terminal.writeString(arg + " ");
                }
                terminal.writeChar('\n');
            }
        });
        terminal.addCommand("list", new Command() {
            public void execute(String[] args) {
                terminal.writeString("--- Files ---\n");
                for (File file : files.getFiles()) {
                    terminal.writeString(file.getName() + "\n");
                }
                terminal.writeString("-------------\n");
            }
        });
        terminal.addCommand("open", new Command() {
            public void execute(String[] args) {
                try {
                    File file = files.getFile(args[0]);
                    if (file == null) {
                        terminal.writeString(
                            "Error when using command \"open\": \"" + args[0] + "\" is not a file.\n"
                        );
                    } else {
                        game.setScreen(file);
                    }
                } catch (IndexOutOfBoundsException ex) {
                    terminal.writeString(
                        "Error: Command \"open\" is missing argument \"name\".\n"
                    );
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

    @Override
    public void resize(int width, int height) {}

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override 
    public void resume() {}

    @Override
    public void dispose() {
        for (File file : files.getFiles()) {
            file.dispose();
        }
    }
}
