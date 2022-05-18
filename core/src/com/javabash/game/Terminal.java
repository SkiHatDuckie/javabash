package com.javabash.game;

public class Terminal {
    private char[] virtualOutput;
    /**Index in {@code virtualOutput} to write virtual input to. */
    private int inputIdx;
    /**Index in {@code virtualOutput} to read the next command from. */
    private int commandIdx;
    private String commandPromptPrefix;

    /**Creates a new virtual terminal. 
     * @param bufSize : Size of the output buffer */
    public Terminal(int bufSize) {
        virtualOutput = new char[bufSize];
        inputIdx = 0;
        commandIdx = 0;

        commandPromptPrefix = "> ";
        writeCommandPromptPrefix();
    }

    public char[] getOutputBuffer() { return virtualOutput; }
    public int getInputIndex() { return inputIdx; }
    public int getCommandIndex() { return commandIdx; }

    /**Returns the index of last non-empty character in {@code virtualOutput}. */
    public int getLastCharacterIndex() {
        int idx = inputIdx;
        // '\u0000' is the null/default char when you first create a char array.
        // We will consider the first occurence as an "empty" index.
        while (virtualOutput[idx] != '\u0000') {
            idx++;
        }

        return idx - 1;
    }

    /**Moves {@code inputIdx} to a given index {@code idx}.
     * <br><br>
     * Precondition: {@code Terminal.getCommandIndex} <= {@code idx} <= {@code Terminal.getLastCharacterIndex} + 1.
     * @param idx : New index in {@code virtualOutput} */
    public void setInputIndex(int idx) {
        inputIdx = idx;
    }

    /**Writes a new character {@code ch} into {@code virtualOutput} at {@code inputIdx}. */
    public void writeChar(char ch) {
        virtualOutput[inputIdx] = ch;
        inputIdx++;
    }

    /**Grabs all characters in {@code virtualOutput} in range {@code commandIdx}
     * to the index of the last character, both inclusive.
     * All escape characters (i.e. '\n', '\t') are ignored.
     * The collected range is then parsed and processed accordingly. */
    public void executeCommand() {
        // TODO: Method is not finished
        String command = "";
        for (int i = commandIdx; i <= getLastCharacterIndex(); i++) {
            if (Character.isLetterOrDigit(virtualOutput[i])
                || Character.isSpaceChar(virtualOutput[i])) {
                command += virtualOutput[i];
            }
        }
        commandIdx = getLastCharacterIndex() + 1;
        inputIdx = getLastCharacterIndex() + 1;
        writeCommandPromptPrefix();
    }

    /**Writes the command prompt prefix to virtual output. */
    private void writeCommandPromptPrefix() {
        for (char ch : commandPromptPrefix.toCharArray()) {
            virtualOutput[inputIdx] = ch;
            inputIdx++;
            commandIdx++;
        }
    }
}
