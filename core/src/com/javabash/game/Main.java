package com.javabash.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Main extends Game {
	/**Draws sprites onto the screen. */
	protected SpriteBatch batch;
	/**Draws primitive shapes onto the screen. */
	protected ShapeRenderer shape;
	/**Draws text onto the screen. */
	protected BitmapFont vt323Font;
	/**Initializes the terminal screen, which should persist until the app is closed. */
	protected TerminalScreen terminalScreen;
	/**Initializes and updates the postions of game objects. */
	protected Grid grid;
	/**Robot game object. */
	protected Robot robot;
	/**Exit game object. */
	protected Exit exit;
	
	public void create() {
		batch = new SpriteBatch();
		shape = new ShapeRenderer();
		vt323Font = generateFreetypeFont("fonts\\VT323-Regular.ttf", 24);

		grid = new Grid(20, 20);
		loadGameObjects();

		terminalScreen = new TerminalScreen(this);
		this.setScreen(terminalScreen);
	}

	public void render() {
		super.render();
	}
	
	public void dispose() {
		batch.dispose();
		vt323Font.dispose();
		terminalScreen.dispose();
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

	/**Loads all objects onto the grid. */
	private void loadGameObjects() {
		grid.loadNewGrid();
		robot = new Robot(grid, 19, 19, 3);
		exit = new Exit(grid, 0, 0);
		grid.placeMines(25);

		GameObject[] updatedObjects = grid.shuffleUntilPossible(robot, exit);
		robot = (Robot) updatedObjects[0];
		exit = (Exit) updatedObjects[1];
	}
}
