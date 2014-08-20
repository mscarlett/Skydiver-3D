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

public class MainMenuScreen extends MenuScreen {

	private static final float TITLE_WIDTH = DefaultScreen.VIRTUAL_WIDTH*(9/10f);
	private static final float TITLE_HEIGHT = DefaultScreen.VIRTUAL_HEIGHT*(9/10f)/2f;
	private static final float BUTTONS_WIDTH = DefaultScreen.VIRTUAL_WIDTH*(6/10f);
	private static final float BUTTONS_HEIGHT = 65;
	private static final float SPACE_BOTTOM = 10;
	
	private final Texture title;

	public MainMenuScreen(SkyDiver3D game) {
		super(game);
		
		// Add the title image
		title = AssetFactory.get(TextureType.TITLE, Texture.class);
		title.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		Image titleImage = new Image(title);
		table.add(titleImage).size(TITLE_WIDTH,TITLE_HEIGHT).uniform().spaceBottom(SPACE_BOTTOM);
		table.row();
		
		//Table to hold buttons
		Table buttonTable = new Table(skin);
		final float buttonsPaddingSide = (TITLE_WIDTH-BUTTONS_WIDTH)/2;
		buttonTable.pad(0, buttonsPaddingSide, 0, buttonsPaddingSide);
		
		if (SkyDiver3D.DEV_MODE) {
			buttonTable.debug();
        }
		
		// Add DistanceFieldFont to fonts and apply to styles
		//skin.add("DistanceFieldFont", new DistanceFieldFont(1/8f, stage.getBatch()), BitmapFont.class);
		TextButtonStyle textButtonStyle = skin.get(TextButtonStyle.class);
		BitmapFont font = FontFactory.generateFont(42);
		textButtonStyle.font = font;
		
		// register the button "start game"
		TextButton startGameButton = new TextButton("Play", skin);
		startGameButton.addListener(new ClickListener() {
			@Override
	        public void clicked(InputEvent event, float x, float y) {
				MainMenuScreen.this.game.sound.play(SoundType.CLICK);
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
				MainMenuScreen.this.game.sound.play(SoundType.CLICK);
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
				MainMenuScreen.this.game.sound.play(SoundType.CLICK);
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
				MainMenuScreen.this.game.sound.play(SoundType.CLICK);
	            showAchievements();
	        }
		});
		buttonTable.add(highScoresButton).uniform().fill();
		
		table.add(buttonTable);
	}

	private void startGame() {
		final float transitionTime = 0.2f;
		Screen next = game.playingScreen.isLoaded() ? game.playingScreen : game.loadingScreen;
		transitionScreen(next, transitionTime);
	}
	
	private void showHelp() {
		game.setScreen(game.helpScreen);
	}
	
	private void showOptions() {
		game.setScreen(game.optionsScreen);
	}
	
	private void showAchievements() {
		game.setScreen(game.achievementsScreen);
	}

	@Override
	public void showScreen() {
		game.music.play(MusicType.MAIN_MENU);
	}

	@Override
	public void hideScreen() {
		//game.music.play(null);
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
		//title.dispose();
	}

	String[] dependencies = new String[]{TextureType.RING, TextureType.STAR, SoundType.CLICK, MusicType.MAIN_MENU, TextureType.LIGHTNING};
	
	public boolean isLoaded() {
		Array<String> names = game.assets.getLoaded();
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
