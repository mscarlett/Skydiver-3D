// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.scarlettapps.skydiver3d.resources.Graphics;
import com.scarlettapps.skydiver3d.resources.Prefs;

public class SkyDiver3D extends Game {
	public static final String LOG = "Skydiving3D";
	public static final boolean DEV_MODE = true;
	
	MainMenuScreen mainMenuScreen;
	WorldPresenter playingScreen;
	LoadingScreen loadingScreen;
	PauseScreen pauseScreen;
	LevelCompletedScreen levelCompletedScreen;
	HelpScreen helpScreen;
	OptionsScreen optionsScreen;
	AchievementsScreen achievementsScreen;
	
	Graphics assets;
	Prefs preferences;
	
	private FPSLogger fpsLogger;

	@Override
	public void create() {
		// Initialize screens
		mainMenuScreen = new MainMenuScreen(this);
		playingScreen = new WorldPresenter(this);
		loadingScreen = new LoadingScreen(this);
		pauseScreen = new PauseScreen(this);
		levelCompletedScreen = new LevelCompletedScreen(this);
		helpScreen = new HelpScreen(this);
		optionsScreen = new OptionsScreen(this);
		achievementsScreen = new AchievementsScreen(this);
		
		// Initialize resources
		assets = new Graphics();
		preferences = new Prefs();
		
		// Set screen to main menu
		setScreen(mainMenuScreen);
		
		// Initialize FPS logger if dev mode enabled
		if (DEV_MODE) {
			fpsLogger = new FPSLogger();
		}
	}
	
	@Override
	public void setScreen(Screen screen) {
		super.setScreen(screen);
		
		// Switch input processor to that of new screen
		if (screen instanceof DefaultScreen<?>) {
			DefaultScreen<?> defaultScreen = (DefaultScreen<?>)screen;
			defaultScreen.setInputProcessor();
		}
	}
	
	@Override
	public void render() {
		// Render the current screen
		super.render();
		
		// Display FPS if dev mode enabled
		if (DEV_MODE) {
			fpsLogger.log();
		}
	}
	
	@Override
	public void dispose() {
		mainMenuScreen.dispose();
		playingScreen.dispose();
		pauseScreen.dispose();
		levelCompletedScreen.dispose();
		helpScreen.dispose();
		optionsScreen.dispose();
		achievementsScreen.dispose();
	}

	public void exit() {
		Gdx.app.exit();
	}
}
