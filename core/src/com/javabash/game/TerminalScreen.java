package com.javabash.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;

public class TerminalScreen implements Screen {
    final Main game;

    public TerminalScreen(final Main game) {
        this.game = game;
    }

    @Override
    public void dispose() {}

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void render(float delta) {
        // Set screen background color
        ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);

        game.batch.begin();
        game.vt323Font.draw(game.batch, "Hello World!", 10, 10.0f + game.vt323Font.getCapHeight());
        game.batch.end();

        // Check for quit attempt
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			game.setScreen(null);
			dispose();
		}
    }

    @Override
    public void resize(int width, int height) {}

    @Override 
    public void resume() {}

    @Override
    public void show() {}
}
