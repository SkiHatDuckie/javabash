package com.javabash.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class Main extends Game {
	/**Draws sprites onto the game screen.
	 * Set to protected so that other classes within the game package 
	 * can find these resources. */
	protected SpriteBatch batch;
	/**Draws text onto the game screen. */
	protected BitmapFont vt323Font;
	
	public void create() {
		batch = new SpriteBatch();
		vt323Font = generateFreetypeFont("fonts\\VT323-Regular.ttf", 24);

		this.setScreen(new TerminalScreen(this));
	}

	public void render() {
		super.render();
	}
	
	public void dispose() {
		batch.dispose();
		vt323Font.dispose();
	}

	/**Generates and returns a font from a .ttf file. */
	private BitmapFont generateFreetypeFont(String path, int size) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(path));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = size;

        BitmapFont font = generator.generateFont(parameter);

        generator.dispose();

		return font;
	}
}
