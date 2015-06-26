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
import com.scarlettapps.skydiver3d.resources.LanguageFactory;
import com.scarlettapps.skydiver3d.resources.MusicFactory;
import com.scarlettapps.skydiver3d.resources.SoundFactory;
import com.scarlettapps.skydiver3d.resources.AssetFactory.SoundType;

public class PauseScreen extends MenuScreen { // PAUSE the game if pause button or menu button pressed

	private static final float BACKGROUND_BRIGHTNESS = 0.3f;
	
	private TextureRegionDrawable screenShot;
	private boolean takeScreenshot = true;
	
	public PauseScreen(Skydiver3D game) {
		super(game, false);
	}
	
	@Override
	public void initializeScreen() {
		super.initializeScreen();
		
        FontFactory fontFactory = FontFactory.getInstance();
        LanguageFactory lang = LanguageFactory.getInstance();
		
		TextButtonStyle textButtonStyle = skin.get(TextButtonStyle.class);
		BitmapFont font = fontFactory.generateFont(42);
		textButtonStyle.font = font;
		
		LabelStyle labelStyle = skin.get(LabelStyle.class);
		font = fontFactory.generateFont(42);
		labelStyle.font = font;
		
		table.setColor(BACKGROUND_BRIGHTNESS, BACKGROUND_BRIGHTNESS, BACKGROUND_BRIGHTNESS, 1);

		table.add(lang.PAUSED).spaceBottom(50);
		table.row();
		
		// register the button "resume game"
		TextButton resumeButton = new TextButton(lang.RESUME, skin);
		resumeButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				SoundFactory.getInstance().play(SoundType.CLICK);
				resumeGame();
			}
		});
		table.add(resumeButton).size(300, 60).uniform().spaceBottom(10);
		table.row();

		// register the button "options"
		TextButton restartButton = new TextButton(lang.TRY_AGAIN, skin);
		restartButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				SoundFactory.getInstance().play(SoundType.CLICK);
				restartGame();
			}

		});
		table.add(restartButton).uniform().fill().spaceBottom(10);
		table.row();

		// register the button "high scores"
		TextButton optionsButton = new TextButton(lang.OPTIONS, skin);
		optionsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				SoundFactory.getInstance().play(SoundType.CLICK);
				showOptions();
			}
		});
		table.add(optionsButton).uniform().fill().spaceBottom(10);
		table.row();
		
		// register the button "high scores"
		TextButton mainMenuButton = new TextButton(lang.MAIN_MENU, skin);
		mainMenuButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				SoundFactory.getInstance().play(SoundType.CLICK);
				mainMenu();
			}
		});
		table.add(mainMenuButton).uniform().fill();
	}
	
	@Override
	public void showScreen() {
		MusicFactory.getInstance().stop();
		if (takeScreenshot) {
			screenShot = new TextureRegionDrawable(ScreenUtils.getFrameBufferTexture());
			table.setBackground(screenShot);
		} else {
			takeScreenshot = true;
		}
	}
	
	private void resumeGame() {
		game.playingScreen.setPaused(false);
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
