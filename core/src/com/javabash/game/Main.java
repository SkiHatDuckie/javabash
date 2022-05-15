package com.javabash.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
//import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {
	private OrthographicCamera camera;
	private SpriteBatch batch;

	private BitmapFont vt323Font;
	
	@Override
	public void create() {
		batch = new SpriteBatch();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 480, 480);

		vt323Font = generateFreetypeFont("fonts\\VT323-Regular.ttf", 24);
	}

	@Override
	public void render() {
		// Set window background color
		ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);

		// Update camera matrices
		camera.update();
		
		batch.setProjectionMatrix(camera.combined);

		// Any sprite that needs to be rendered should be done so using `batch`
		batch.begin();
		vt323Font.draw(batch, "Hello World!", 10.0f, 10.0f + vt323Font.getCapHeight());
		batch.end();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
	}

	// Generate and return a font from a .ttf file
	private BitmapFont generateFreetypeFont(String path, int size) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(path));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = size;

        BitmapFont font = generator.generateFont(parameter);

        generator.dispose();

		return font;
	}
}
