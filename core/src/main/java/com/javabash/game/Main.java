package com.javabash.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {
	private ShapeRenderer shape;
	
	@Override
	public void create () {
	shape = new ShapeRenderer();
	}

	@Override
	public void render () {
		// Set window background color
		ScreenUtils.clear(0, 0, 0, 1);

		Rect box = new Rect(0, 0, 25, 25, Color.RED);

		shape.begin(ShapeType.Filled);
		shape.setColor(box.color);
		shape.rect(box.x, box.y, box.width, box.height);
		shape.end();
	}
	
	@Override
	public void dispose () {
	}
}
