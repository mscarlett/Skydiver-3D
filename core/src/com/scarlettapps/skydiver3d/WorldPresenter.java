// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.scarlettapps.skydiver3d.resources.Graphics;
import com.scarlettapps.skydiver3d.world.World;
import com.scarlettapps.skydiver3d.world.gamestate.GameController;
import com.scarlettapps.skydiver3d.world.gamestate.InputManager;
import com.scarlettapps.skydiver3d.world.gamestate.StatusManager;
import com.scarlettapps.skydiver3d.world.gamestate.StatusManager.WorldState;
import com.scarlettapps.skydiver3d.worldview.WorldView;


public class WorldPresenter extends DefaultScreen<SkyDiver3D> {
	
	private static final float MAX_DELTA = 0.1f;
	
	private World world;
	private WorldView worldView;
	private GameController gameController;
	private InputManager inputManager;
	private StatusManager statusManager;
	
	public WorldPresenter(SkyDiver3D game) {
		super(game, false);
		load();
	}
	
	private void load() {
		Graphics.load();
		Graphics.getAssets().finishLoading();
		gameController = GameController.newGameController();
		inputManager = new InputManager(gameController);
		statusManager = new StatusManager(inputManager);
		world = new World(inputManager, statusManager);
		worldView = new WorldView(world, statusManager);

		DefaultShader.defaultCullFace = 0; // disable cull face for all models
	}
	
	@Override
	public void render(float delta) {
		if (statusManager.isPaused()) {
			// If the world is paused then switch to pause screen
			game.setScreen(game.pauseScreen);
		} else if (statusManager.getState() == WorldState.FINAL) {
			// If the level has been completed then switch to level completed screen
			game.setScreen(game.levelCompletedScreen);
		} else {
			// Otherwise update and render world
			if (delta >= MAX_DELTA) delta = MAX_DELTA;
			updateWorld(delta);
			Gdx.gl.glClearColor(0.5f, 0.5f, 1.0f, 1.0f);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
			renderWorld(delta);
		}
	}
	
	protected void updateWorld(float delta) {
		world.update(delta);
		worldView.update(delta);
	}
	
	protected void renderWorld(float delta) {
		worldView.render(delta);
	}

	@Override
	protected InputProcessor getInputProcessor() {
		return gameController;
	}

	@Override
	public void disposeScreen() {
		Graphics.dispose();
	}

	@Override
	protected void showScreen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void pauseScreen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void resumeScreen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void hideScreen() {
		// TODO Auto-generated method stub
	}
	
	public void tryAgain() {
		world.reset();
		worldView.reset();
	}

	public void restartLevel() { //simply reinitialize objects for now
		gameController = GameController.newGameController();
		inputManager = new InputManager(gameController);
		statusManager = new StatusManager(inputManager);
		world = new World(inputManager, statusManager);
		worldView = new WorldView(world, statusManager);
	}
	
	public void nextLevel() {
		
	}

	public boolean isLoaded() {
		return Graphics.isLoaded();
	}

	public void renderScore() {
		worldView.renderScore();
	}
	
	public int getRating() {
		return statusManager.rating();
	}

}
