// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.scarlettapps.skydiver3d.resources.AssetFactory;
import com.scarlettapps.skydiver3d.resources.MusicFactory;
import com.scarlettapps.skydiver3d.resources.PreferenceFactory;
import com.scarlettapps.skydiver3d.resources.SoundFactory;

public class SkyDiver3D extends Game {
	public static final String LOG = "Skydiver3D";
	public static final boolean DEV_MODE = false;
	
	MainMenuScreen mainMenuScreen;
	WorldPresenter playingScreen;
	LoadingScreen loadingScreen;
	PauseScreen pauseScreen;
	LevelCompletedScreen levelCompletedScreen;
	HelpScreen helpScreen;
	OptionsScreen optionsScreen;
	AchievementsScreen achievementsScreen;
	SplashScreen splashScreen;
	GameOptionsScreen gameOptionsScreen;
	
	AssetFactory assets;
	PreferenceFactory preferences;
	MusicFactory music;
	SoundFactory sound;
	
	private FPSLogger fpsLogger;

	@Override
	public void create() {
		// Initialize resources
		assets = AssetFactory.newInstance();
		preferences = PreferenceFactory.newInstance();
		music = MusicFactory.newInstance();
		sound = SoundFactory.newInstance();
		
		// Initialize screens
		mainMenuScreen = new MainMenuScreen(this);
		playingScreen = new WorldPresenter(this);
		loadingScreen = new LoadingScreen(this);
		pauseScreen = new PauseScreen(this);
		levelCompletedScreen = new LevelCompletedScreen(this);
		helpScreen = new HelpScreen(this);
		optionsScreen = new OptionsScreen(this);
		achievementsScreen = new AchievementsScreen(this);
		splashScreen = new SplashScreen(this);
		gameOptionsScreen = new GameOptionsScreen(this);
		
		// Set screen to main menu
		setScreen(splashScreen);
		
		// Initialize FPS logger if dev mode enabled
		if (DEV_MODE) {
			fpsLogger = new FPSLogger();
		}
	}
	
	@Override
	public void setScreen(Screen screen) {
		super.setScreen(screen);
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
		splashScreen.dispose();
		loadingScreen.dispose();
		gameOptionsScreen.dispose();
	}

	public void exit() {
		Gdx.app.exit();
	}
}
