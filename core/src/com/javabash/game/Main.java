package com.javabash.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class Main extends Game {
	/**Draws sprites onto the screen.
	 * Set to protected so that other classes within the game package 
	 * can find these resources. */
	protected SpriteBatch batch;
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
		vt323Font = generateFreetypeFont("fonts\\VT323-Regular.ttf", 24);
		terminalScreen = new TerminalScreen(this);
		grid = new Grid(20, 20);

		loadGameObjects();

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
		robot = new Robot(grid, 19, 19);
		exit = new Exit(grid, 0, 0);

		// testing
		// System.out.println("[");
		// for (int row = 0; row < grid.getHeight(); row++) {
		// 	System.out.print("\t[");
		// 	for (int col = 0; col < grid.getWidth(); col++) {
		// 		if (grid.getCell(col, row) instanceof Robot) {
		// 			System.out.print("R,");
		// 		} else if (grid.getCell(col, row) instanceof Exit) {
		// 			System.out.print("E,");
		// 		} else {
		// 			System.out.print(".,");
		// 		}
		// 	}
		// 	System.out.println("]");
		// }
		// System.out.println("]");
	}
}
