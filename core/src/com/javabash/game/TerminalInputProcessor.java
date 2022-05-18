package com.javabash.game;

import com.badlogic.gdx.InputAdapter;

public class TerminalInputProcessor extends InputAdapter {
    private Terminal terminal;

    public TerminalInputProcessor(Terminal terminal) {
        this.terminal = terminal;
    }

    public boolean keyTyped(char character) {
        if (character == '\n') {
            terminal.setInputIndex(terminal.getLastCharacterIndex() + 1);
            terminal.writeChar(character);
            terminal.executeCommand();
        } else {
            terminal.writeChar(character);
        }

        return true;
    }
}
