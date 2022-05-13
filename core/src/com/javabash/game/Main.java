package com.javabash.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {
	private TextField textbox;
	private SpriteBatch batch;
	
	@Override
	public void create() {
		batch = new SpriteBatch();

		TextFieldStyle style = new TextFieldStyle();
		style.font = generateTtfFont("fonts\\VT323-Regular.ttf", 24);
		style.fontColor = Color.WHITE;

		textbox = new TextField("Hello World!", style);
		textbox.setX(100);
		textbox.setY(100);
	}

	@Override
	public void render() {
		// Set window background color
		ScreenUtils.clear(0, 0, 0, 1);

		batch.begin();
		textbox.draw(batch, 1);
		batch.end();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		
	}

	// Generate and return a font from a .ttf file
	private BitmapFont generateTtfFont(String path, int size) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(path));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = size;

        BitmapFont font = generator.generateFont(parameter);

        generator.dispose();

		return font;
	}
}
