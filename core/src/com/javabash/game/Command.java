package com.javabash.game;

public abstract class Command {
    protected String description;

    public Command(String description) {
        this.description = description;
    }

    public abstract void execute(String[] args);
}
