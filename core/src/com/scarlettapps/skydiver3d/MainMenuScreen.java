// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.scarlettapps.skydiver3d.resources.AssetFactory;
import com.scarlettapps.skydiver3d.resources.AssetFactory.MusicType;
import com.scarlettapps.skydiver3d.resources.AssetFactory.SoundType;
import com.scarlettapps.skydiver3d.resources.AssetFactory.TextureType;
import com.scarlettapps.skydiver3d.resources.FontFactory;
import com.scarlettapps.skydiver3d.resources.MusicFactory;
import com.scarlettapps.skydiver3d.resources.SoundFactory;

public class MainMenuScreen extends MenuScreen {

	// Physical dimensions of UI objects
	private static final float TITLE_WIDTH = DefaultScreen.VIRTUAL_WIDTH*(9/10f);
	private static final float TITLE_HEIGHT = DefaultScreen.VIRTUAL_HEIGHT*(9/10f)/2f;
	private static final float BUTTONS_WIDTH = DefaultScreen.VIRTUAL_WIDTH*(6/10f);
	private static final float BUTTONS_HEIGHT = DefaultScreen.VIRTUAL_HEIGHT/10;
	private static final float SPACE_BOTTOM = DefaultScreen.VIRTUAL_HEIGHT/64;
	
	// Transition time between this screen and the game screen
	private static final float TRANSITION_TIME = 0.2f;
	
	private final Texture title;

	public MainMenuScreen(Skydiver3D game) {
		super(game);
		
		AssetFactory assetFactory = AssetFactory.getInstance();
		final SoundFactory sound = SoundFactory.getInstance();
		
		// Add the title image
		title = assetFactory.get(TextureType.TITLE, Texture.class);
		title.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		Image titleImage = new Image(title);
		table.add(titleImage).size(TITLE_WIDTH,TITLE_HEIGHT).uniform().spaceBottom(SPACE_BOTTOM);
		table.row();
		
		//Table to hold buttons
		Table buttonTable = new Table(skin);
		final float buttonsPaddingSide = (TITLE_WIDTH-BUTTONS_WIDTH)/2;
		buttonTable.pad(0, buttonsPaddingSide, 0, buttonsPaddingSide);
		
		if (Skydiver3D.DEV_MODE) {
			buttonTable.debug();
        }
		
		FontFactory fontFactory = FontFactory.getInstance();
		
		// Add DistanceFieldFont to fonts and apply to styles
		TextButtonStyle textButtonStyle = skin.get(TextButtonStyle.class);
		BitmapFont font = fontFactory.generateFont(42);
		textButtonStyle.font = font;
		
		// register the button "start game"
		TextButton startGameButton = new TextButton("Play", skin);
		startGameButton.addListener(new ClickListener() {
			@Override
	        public void clicked(InputEvent event, float x, float y) {
				sound.play(SoundType.CLICK);
	            startGame();
	        }
		});
		buttonTable.add(startGameButton).size(BUTTONS_WIDTH, BUTTONS_HEIGHT).uniform().spaceBottom(SPACE_BOTTOM);
		buttonTable.row();
		
		// register the button "high scores"
		TextButton helpButton = new TextButton("Help", skin);
		helpButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				sound.play(SoundType.CLICK);
				showHelp();
			}
		});
		buttonTable.add(helpButton).uniform().fill().spaceBottom(SPACE_BOTTOM);
		buttonTable.row();

		// register the button "options"
		TextButton optionsButton = new TextButton("Options", skin);
		optionsButton.addListener(new ClickListener() {
			@Override
	        public void clicked(InputEvent event, float x, float y) {
				sound.play(SoundType.CLICK);
	            showOptions();
	        }

		});
		buttonTable.add(optionsButton).uniform().fill().spaceBottom(SPACE_BOTTOM);
		buttonTable.row();

		// register the button "high scores"
		TextButton highScoresButton = new TextButton("Credits", skin);
		highScoresButton.addListener(new ClickListener() {
			@Override
	        public void clicked(InputEvent event, float x, float y) {
				sound.play(SoundType.CLICK);
	            showCredits();
	        }
		});
		buttonTable.add(highScoresButton).uniform().fill();
		
		table.add(buttonTable);
	}

	/**
	 * Transition to the game loading screen if it is still being loaded, or the
	 * game screen if loading is finished
	 */
	private void startGame() {
		Screen next = game.playingScreen.isLoaded() ? game.playingScreen : game.loadingScreen;
		transitionScreen(next, TRANSITION_TIME);
	}
	
	/**
	 * Show the help screen
	 */
	private void showHelp() {
		game.setScreen(game.helpScreen);
	}
	
	/**
	 * Show the options screen
	 */
	private void showOptions() {
		game.setScreen(game.optionsScreen);
	}
	
	/**
	 * Show the credits screen
	 */
	private void showCredits() {
		game.setScreen(game.creditsScreen);
	}

	/**
	 * Show this screen
	 */
	@Override
	public void showScreen() {
		MusicFactory.getInstance().play(MusicType.MAIN_MENU);
	}

	// List of dependencies which need to be loaded
	private static final String[] dependencies = new String[]{TextureType.RING, TextureType.STAR, SoundType.CLICK, MusicType.MAIN_MENU};
	
	/**
	 * Check if this screen is loaded
	 * @return true if loaded, false otherwise
	 */
	public boolean isLoaded() {
		Array<String> names = AssetFactory.getInstance().getLoaded();
		outer:
		for (String d: dependencies) {
			for (String name: names) {
				if (d.equals(name)) {
					continue outer;
				}
			}
			return false;
		}
		return true;
	}

}
