package com.javabash.game;

import com.badlogic.gdx.Screen;

public class File implements Screen {
    final Main game;
    private String name;
    protected Terminal terminal;

    public File(final Main game, String name, Terminal terminal) {
        this.game = game;
        this.name = name;
        this.terminal = terminal;
    }

    public String getName() { return name; }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {}

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}
}
