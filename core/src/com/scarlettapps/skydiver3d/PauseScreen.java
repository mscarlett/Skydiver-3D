// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;

public class PauseScreen extends MenuScreen { // PAUSE the game if pause button or menu button pressed

	public PauseScreen(SkyDiver3D game) {
		super(game, false);
		
		table.setColor(BACKGROUND_BRIGHTNESS, BACKGROUND_BRIGHTNESS, BACKGROUND_BRIGHTNESS, 1);

		table.add("Paused").spaceBottom(50);
		table.row();
		
		// register the button "resume game"
		TextButton resumeButton = new TextButton("Resume", skin);
		resumeButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				resumeGame();
			}
		});
		table.add(resumeButton).size(300, 60).uniform().spaceBottom(10);
		table.row();

		// register the button "options"
		TextButton restartButton = new TextButton("Restart", skin);
		restartButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				restartGame();
			}

		});
		table.add(restartButton).uniform().fill().spaceBottom(10);
		table.row();

		// register the button "high scores"
		TextButton optionsButton = new TextButton("Options", skin);
		optionsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showOptions();
			}
		});
		table.add(optionsButton).uniform().fill().spaceBottom(10);
		table.row();
		
		// register the button "high scores"
		TextButton quitButton = new TextButton("Quit", skin);
		quitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				quitGame();
			}
		});
		table.add(quitButton).uniform().fill();
	}
	
	private TextureRegionDrawable screenShot;
	private static final float BACKGROUND_BRIGHTNESS = 0.3f;

	@Override
	public void showScreen() { //TODO does it work?
		//stage.clear();
		screenShot = new TextureRegionDrawable(ScreenUtils.getFrameBufferTexture());
		//screenShot.setColor(BACKGROUND_BRIGHTNESS, BACKGROUND_BRIGHTNESS, BACKGROUND_BRIGHTNESS, 1);
		table.setBackground(screenShot);
		//stage.addActor(table);
	}

	@Override
	public void hideScreen() {
		//stage.getRoot().removeActor(screenShot);
	}

	@Override
	public void pauseScreen() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resumeScreen() {
		// TODO Auto-generated method stub

	}

	@Override
	public void disposeScreen() {
		// TODO Auto-generated method stub

	}
	
	private void resumeGame() {
		game.setScreen(game.playingScreen);
	}
	
	private void restartGame() {
		game.playingScreen.restartLevel();
		game.setScreen(game.playingScreen);
	}
	
	private void showOptions() {
		
	}
	
	private void quitGame() {
		
	}

}
