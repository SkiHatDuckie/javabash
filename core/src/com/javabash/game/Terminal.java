package com.javabash.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Terminal {
    private char[] virtualOutput;
    /**Index in {@code virtualOutput} to write virtual input to. */
    private int inputIdx;
    /**Index in {@code virtualOutput} to read the next command from. */
    private int commandIdx;
    private String commandPromptPrefix;
    private HashMap<String, Command> commandTable;

    /**Creates a new virtual terminal. 
     * @param bufSize : Size of the output buffer */
    public Terminal(int bufSize) {
        virtualOutput = new char[bufSize];
        inputIdx = 0;
        commandIdx = 0;
        commandPromptPrefix = "> ";
        commandTable = new HashMap<String, Command>();

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

    /**Writes a String {@code str} into {@code virtualOutput} at {@code inputIdx}. */
    public void writeString(String str) {
        for (char ch : str.toCharArray()) {
            writeChar(ch);
        }
    }

    /**Adds a new command to {@code commandTable}.
     * @param name : Name of command (used to execute command in virtual terminal)
     * @param command : It is the user's responsibility to handle the arguments given to Command.execute() */
    public void addCommand(String name, Command command) {
        commandTable.put(name, command);
    }

    /**Gets the user input in {@code virtualOutput}, parses it into a command,
     * and then executes the command if it exists. 
     * <br><br>
     * An error message will be written into {@code virtualOutput} if the name of
     * the parsed command does not exist. */
    public void executeUserInput() {
        String userInput = getUserInput();
        inputIdx = getLastCharacterIndex() + 1;

        List<String> command = parse(userInput);
        if (isValidName(command.get(0))) {
            commandTable.get(command.get(0))
                .execute(command.subList(1, command.size()).toArray(String[]::new));
            writeCommandPromptPrefix();
        } else {
            writeNameNotFoundError(command.get(0));
            writeCommandPromptPrefix();
        }
        commandIdx = getLastCharacterIndex() + 1;
    }

    /**Removes the character at index {@code inputIdx - 1} in {@code virtualOutput}.
     * All characters after the removed character will be moved one index back.
     * (index 2 -> 1, 3 -> 2...)
     * <br><br>
     * Nothing is changed if {@code inputIdx} == {@code commandIdx}. */
    public void backspace() {
        if (!(inputIdx == commandIdx)) {
            virtualOutput[inputIdx - 1] = '\u0000';
            for (int i = inputIdx; i <= getLastCharacterIndex() + 1; i++) {
                virtualOutput[i - 1] = virtualOutput[i];
            }
            inputIdx--;
        }
    }

    /**Grabs all characters in {@code virtualOutput} in range {@code commandIdx}
     * to the index of the last character, both inclusive.
     * All escape characters (i.e. '\n', '\t') are ignored. */
    private String getUserInput() {
        String input = "";
        for (int i = commandIdx; i <= getLastCharacterIndex(); i++) {
            if (Character.isLetterOrDigit(virtualOutput[i])
                || Character.isSpaceChar(virtualOutput[i])) {
                input += virtualOutput[i];
            }
        }
        return input;
    }

    /**Returns the command name and any arguments as a list of strings.
     * The first value in the list is the command name, followed
     * by the arguments.
     * <br><br>
     * This method does not check for whether the command exists or not. */
    private List<String> parse(String input) {
        String[] items = input.split(" ");
        List<String> args = new ArrayList<String>();

        for (String item : items) {
            if (!item.equals("")) {
                args.add(item);
            }
        }
        return args;
    }

    /**Returns true if {@code name} is a key in {@code commandTable}. */
    private boolean isValidName(String name) {
        return commandTable.containsKey(name);
    }

    /**Writes the command prompt prefix to {@code virtualOutput}. */
    private void writeCommandPromptPrefix() {
        writeString(commandPromptPrefix);
    }

    /**Writes an error to {@code virtualOutput} when a command name cannot
    be found. */
    private void writeNameNotFoundError(String name) {
        String errorMsg = "Error: \"" + name + "\" is not a command.\n";
        writeString(errorMsg);
    }
}
