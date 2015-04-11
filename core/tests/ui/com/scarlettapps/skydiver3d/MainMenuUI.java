package com.scarlettapps.skydiver3d;

import com.badlogic.gdx.graphics.FPSLogger;
import com.scarlettapps.skydiver3d.resources.AssetFactory;


public class MainMenuUI extends Skydiver3D {

	@Override
	public void create() {
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
		
		AssetFactory.getInstance().finishLoading();
		
		// Set screen to main menu
		setScreen(mainMenuScreen);
		
		// Initialize FPS logger if dev mode enabled
		if (DEV_MODE) {
			fpsLogger = new FPSLogger();
		}
	}
}
