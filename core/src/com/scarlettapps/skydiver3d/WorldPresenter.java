// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.scarlettapps.skydiver3d.resources.AssetFactory;
import com.scarlettapps.skydiver3d.resources.AssetFactory.MusicType;
import com.scarlettapps.skydiver3d.resources.MusicFactory;
import com.scarlettapps.skydiver3d.world.Level;
import com.scarlettapps.skydiver3d.world.World;
import com.scarlettapps.skydiver3d.worldstate.GameController;
import com.scarlettapps.skydiver3d.worldstate.InputManager;
import com.scarlettapps.skydiver3d.worldstate.Score;
import com.scarlettapps.skydiver3d.worldstate.SkydiverControls;
import com.scarlettapps.skydiver3d.worldstate.Status;
import com.scarlettapps.skydiver3d.worldstate.StatusManager;
import com.scarlettapps.skydiver3d.worldview.WorldView;

/**
 * Screen which displays the skydiving game
 * @author Michael Scarlett
 *
 */
public class WorldPresenter extends DefaultScreen<Skydiver3D> {
	
	// Maximum time difference in seconds between frames
	protected static final float MAX_DELTA = 0.1f;
	// Represents the current state of game objects
	protected World world;
	// Renders the game objects
	protected WorldView worldView;
	// Handles user input
	protected GameController gameController;
	// Handles listeners for user input
	protected InputManager inputManager;
	// Handles listeners for current game state
	protected StatusManager statusManager;
	// Status of the world
	protected Status status;
	
	/**
	 * Instantiate the screen with the game instance
	 * @param game the instance of this game
	 */
	public WorldPresenter(Skydiver3D game) {
		super(game, false);
	}
	
	public void initializeScreen() {
		if (Skydiver3D.DEV_MODE) {
			Gdx.app.log(Skydiver3D.LOG, "Initializing world");
		}
		
		status = new Status();
		gameController = GameController.newGameController();
		
		inputManager = new InputManager(gameController);
		statusManager = new StatusManager(inputManager, status);
		world = new World(inputManager, statusManager);
		worldView = new WorldView(world, statusManager);

		world.initialize();
		worldView.initialize();
	}
	
	/**
	 * Update the game state and render the current frame
	 * @param delta the time in seconds between frames
	 */
	@Override
	public void render(float delta) {		
		if (status.isPaused()) {
			// If the world is paused then switch to pause screen
			game.setScreen(game.pauseScreen);
		} else if (status.isCompleted()) {
			// If the level has been completed then switch to level completed screen
			game.setScreen(game.levelCompletedScreen);
		} else {
			// Otherwise update and render world
			if (delta >= MAX_DELTA) delta = MAX_DELTA;
			updateObjects(delta);
			Gdx.gl.glClearColor(0.5f, 0.5f, 1.0f, 1.0f);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
			renderObjects(delta);
		}
	}
	
	/**
	 * Update the world objects
	 * @param delta the time in seconds between frames
	 */
	protected void updateObjects(float delta) {
		world.update(delta);
		statusManager.update(delta);
		worldView.update(delta);
		inputManager.update(delta);
	}
	
	/**
	 * Render the world objects
	 * @param delta the time in seconds between frames
	 */
	protected void renderObjects(float delta) {
		worldView.render(delta);
	}

	/**
	 * Get the input processor which accepts user input
	 * @return the input processors from the world view and game controller
	 */
	@Override
	protected InputProcessor getInputProcessor() {
		InputMultiplexer input = new InputMultiplexer();
		input.addProcessor(worldView.getInputProcessor());
		input.addProcessor(gameController);
		return input;
	}
	
	/**
	 * Show the screen and initialize game resources
	 */
	@Override
	protected void showScreen() {
		// Switch music
        MusicFactory music = MusicFactory.getInstance();
		music.stop();		
		music.play(MusicType.WIND);
	}

	/**
	 * Restart the level
	 */
	public void restartLevel() {
		gameController.reset();
		inputManager.reset();
		statusManager.reset();
		world.reset();
		worldView.reset();
	}
	
	/**
	 * Go to the next level
	 */
	public void nextLevel() {
		status.nextLevel();
		Level difficulty = status.difficulty();
		world.setLevel(difficulty);
		restartLevel();
	}
	
	/**
	 * Get the game score summary
	 * @return the score
	 */
	public Score scoreSummary() {
		return status.scoreSummary();
	}

	/**
	 * Check whether or not game resources are loaded
	 * @return true if loaded, false otherwise
	 */
	public boolean isLoaded() {
		return AssetFactory.getInstance().isLoaded();
	}

	/**
	 * Unpause the game
	 * @param b whether or not the game should be paused
	 */
	public void setPaused(boolean b) {
		status.setPaused(b);
	}

}
