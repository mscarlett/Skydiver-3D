// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.utils.TimeUtils;
import com.scarlettapps.skydiver3d.resources.AssetFactory;

/**
 * Class that stores the current instance of the game and the current screen,
 * and provides a way to switch between screens.
 * 
 * @author Michael Scarlett
 *
 */
public class Skydiver3D extends Game {
	/** Constant variables for debugging */
	// Title of the game displayed in logging messages
	public static final String LOG = "Skydiver3D";
	// Whether or not the game should be run in dev mode, which enables
    // advanced debugging options for game logic and gui testing
	public static final boolean DEV_MODE = true;
	
	/** Screens displayed when browsing the main menu */
	// Splash screen for main menu that is displayed while loading
	SplashScreen splashScreen;
	// Main menu screen which allows navigation to all other screens
	MainMenuScreen mainMenuScreen;
	// Help screen for main menu
	HelpScreen helpScreen;
	// Options screen for changing the settings from the main menu
	OptionsScreen optionsScreen;
	// Credits screen
	CreditsScreen creditsScreen;
	
	/** Screens displayed when playing game */
	// Loading screen for game
	LoadingScreen loadingScreen;
	// Game screen which displays the current game state
	WorldPresenter playingScreen;
	// Pause screen for game
	PauseScreen pauseScreen;
	// Level completed screen which displays once the skydiver has landed
	LevelCompletedScreen levelCompletedScreen;
	// Options screen for changing the settings during a game
	GameOptionsScreen gameOptionsScreen;
	
	// FPS logger for debugging the frame rate
	FPSLogger fpsLogger;

	/**
	 * Instantiate game resources and screens, then display the initial screen
	 */
	@Override
	public void create() {
		long start;
		
		if (DEV_MODE) {
			// Monitor the time to init screens and other resources
			start = TimeUtils.nanoTime();
		}
		
		// Initialize resources
		AssetFactory.getInstance().load();
		
		// Initialize screens
		mainMenuScreen = new MainMenuScreen(this);
		playingScreen = new WorldPresenter(this);
		loadingScreen = new LoadingScreen(this);
		pauseScreen = new PauseScreen(this);
		levelCompletedScreen = new LevelCompletedScreen(this);
		helpScreen = new HelpScreen(this);
		optionsScreen = new OptionsScreen(this);
		creditsScreen = new CreditsScreen(this);
		splashScreen = new SplashScreen(this);
		gameOptionsScreen = new GameOptionsScreen(this);
		
		// Set screen to splash screen
		setScreen(splashScreen);
		
		if (DEV_MODE) {
			// Initialize FPS logger for monitoring frame rate
			fpsLogger = new FPSLogger();
		    
			double elapsed = TimeUtils.timeSinceNanos(start)/1e9;
			
			Gdx.app.log(LOG, "Time to init screens: " + elapsed + " s");
		}
	}
	
	/**
	 * Set the current screen
	 */
	@Override
	public void setScreen(Screen screen) {
		if (DEV_MODE) {
			Gdx.app.log(LOG, "Setting screen to " + screen.getClass().getSimpleName());
		}
		
		super.setScreen(screen);
	}
	
	/**
	 * Render the current screen
	 */
	@Override
	public void render() {
		// Calls the rendering method for the current screen
		super.render();
		
		// Display FPS if dev mode enabled
		if (DEV_MODE) {
			fpsLogger.log();
		}
	}
	
	/**
	 * Dispose of this game by disposing of all screens and assets
	 */
	@Override
	public void dispose() {
		// Dispose screens
		mainMenuScreen.dispose();
		playingScreen.dispose();
		pauseScreen.dispose();
		levelCompletedScreen.dispose();
		helpScreen.dispose();
		optionsScreen.dispose();
		creditsScreen.dispose();
		splashScreen.dispose();
		loadingScreen.dispose();
		gameOptionsScreen.dispose();
		
		// Dispose game resources
		AssetFactory.getInstance().dispose();
	}

	/**
	 * Exit the application
	 */
	public void exit() {
		Gdx.app.exit();
	}
}
