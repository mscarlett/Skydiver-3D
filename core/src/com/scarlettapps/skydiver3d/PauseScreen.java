// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.scarlettapps.skydiver3d.resources.FontFactory;
import com.scarlettapps.skydiver3d.resources.MusicFactory;
import com.scarlettapps.skydiver3d.resources.SoundFactory;
import com.scarlettapps.skydiver3d.resources.AssetFactory.SoundType;

public class PauseScreen extends MenuScreen { // PAUSE the game if pause button or menu button pressed

	public PauseScreen(SkyDiver3D game) {
		super(game, false);
		
		TextButtonStyle textButtonStyle = skin.get(TextButtonStyle.class);
		BitmapFont font = FontFactory.generateFont(42);
		textButtonStyle.font = font;
		
		LabelStyle labelStyle = skin.get(LabelStyle.class);
		font = FontFactory.generateFont(42);
		labelStyle.font = font;
		
		table.setColor(BACKGROUND_BRIGHTNESS, BACKGROUND_BRIGHTNESS, BACKGROUND_BRIGHTNESS, 1);

		table.add("Paused").spaceBottom(50);
		table.row();
		
		// register the button "resume game"
		TextButton resumeButton = new TextButton("Resume", skin);
		resumeButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				SoundFactory.playSound(SoundType.CLICK);
				resumeGame();
			}
		});
		table.add(resumeButton).size(300, 60).uniform().spaceBottom(10);
		table.row();

		// register the button "options"
		TextButton restartButton = new TextButton("Try Again", skin);
		restartButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				SoundFactory.playSound(SoundType.CLICK);
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
				SoundFactory.playSound(SoundType.CLICK);
				showOptions();
			}
		});
		table.add(optionsButton).uniform().fill().spaceBottom(10);
		table.row();
		
		// register the button "high scores"
		TextButton mainMenuButton = new TextButton("Main Menu", skin);
		mainMenuButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				SoundFactory.playSound(SoundType.CLICK);
				mainMenu();
			}
		});
		table.add(mainMenuButton).uniform().fill();
	}
	
	private TextureRegionDrawable screenShot;
	private static final float BACKGROUND_BRIGHTNESS = 0.3f;

	boolean takeScreenshot = true;
	
	@Override
	public void showScreen() {
		MusicFactory.stopSound();
		if (takeScreenshot) {
			screenShot = new TextureRegionDrawable(ScreenUtils.getFrameBufferTexture());
			table.setBackground(screenShot);
		} else {
			takeScreenshot = true;
		}
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
		takeScreenshot = false;
		game.setScreen(game.gameOptionsScreen);
	}
	
	private void mainMenu() {
		game.setScreen(game.mainMenuScreen);
	}

}
