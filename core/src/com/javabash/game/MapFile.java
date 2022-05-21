package com.javabash.game;

import com.badlogic.gdx.utils.ScreenUtils;

public class MapFile extends File {
    public MapFile(final Main game, String name) {
        super(game, name);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.11f, 0.1f, 0.2f, 1);
    }
}
