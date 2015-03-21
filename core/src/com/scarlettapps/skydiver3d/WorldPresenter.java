// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.scarlettapps.skydiver3d.resources.AssetFactory;
import com.scarlettapps.skydiver3d.resources.AssetFactory.MusicType;
import com.scarlettapps.skydiver3d.resources.MusicFactory;
import com.scarlettapps.skydiver3d.world.World;
import com.scarlettapps.skydiver3d.worldstate.GameController;
import com.scarlettapps.skydiver3d.worldstate.InputManager;
import com.scarlettapps.skydiver3d.worldstate.Score;
import com.scarlettapps.skydiver3d.worldstate.SkydiverControls;
import com.scarlettapps.skydiver3d.worldstate.StatusManager;
import com.scarlettapps.skydiver3d.worldstate.WorldState;
import com.scarlettapps.skydiver3d.worldview.WorldView;


public class WorldPresenter extends DefaultScreen<Skydiver3D> { //TODO bug in which tapping screen while parachuting changes cam position
	
	private static final float MAX_DELTA = 0.1f;
	
	private World world;
	private WorldView worldView;
	private GameController gameController;
	private InputManager inputManager;
	private StatusManager statusManager;
	
	/**
	 * 
	 * @param game the instance of this game
	 */
	public WorldPresenter(Skydiver3D game) {
		super(game, false);
		gameController = GameController.newGameController();
		inputManager = new InputManager(gameController);
		statusManager = new StatusManager(inputManager);

		DefaultShader.defaultCullFace = 0;
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
		inputManager.update(delta);
	}
	
	protected void renderWorld(float delta) {
		worldView.render(delta);
	}

	@Override
	protected InputProcessor getInputProcessor() {
		InputMultiplexer input = new InputMultiplexer();
		input.addProcessor(worldView.getInputProcessor());
		input.addProcessor(gameController);
		return input;
	}

	@Override
	public void disposeScreen() {
		AssetFactory.getInstance().dispose();
	}

	boolean load = true;
	
	@Override
	protected void showScreen() {
		MusicFactory music = MusicFactory.getInstance();
		
		music.stop();
		
		if (load) {
			world = new World(statusManager);
			worldView = new WorldView(world, statusManager);

			SkydiverControls skydiverControls = new SkydiverControls(world,
					statusManager);
			inputManager.addListener(skydiverControls);

			load = false;
		}
		
		music.play(MusicType.WIND);
		
		statusManager.setPaused(false);
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
		//FIXME avoid recreating everything
		gameController = GameController.newGameController();
		inputManager = new InputManager(gameController);
		statusManager = new StatusManager(inputManager);
		world = new World(statusManager);
		worldView = new WorldView(world, statusManager);
		SkydiverControls skydiverControls = new SkydiverControls(world, statusManager);
		inputManager.addListener(skydiverControls);
	}
	
	public void nextLevel() {
		restartLevel(); //XXX temporary hack until this is implemented
	}
	
	public Score scoreSummary() {
		return statusManager.scoreSummary();
	}

	public boolean isLoaded() {
		return AssetFactory.getInstance().isLoaded();
	}

}
