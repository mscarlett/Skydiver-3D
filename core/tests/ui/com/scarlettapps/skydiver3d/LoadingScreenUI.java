package com.scarlettapps.skydiver3d;

import com.badlogic.gdx.graphics.FPSLogger;
import com.scarlettapps.skydiver3d.resources.AssetFactory;

public class LoadingScreenUI extends Skydiver3D {

	@Override
	public void create() {
		// Initialize resources
		AssetFactory.getInstance().load();
		
		// Initialize screens
		mainMenuScreen = new MainMenuScreen(this);
		playingScreen = new WorldPresenter(this);
		loadingScreen = new LoadingScreen(this) {
			@Override
			protected void startGame() {
				// do nothing
			}
		};
		pauseScreen = new PauseScreen(this);
		levelCompletedScreen = new LevelCompletedScreen(this);
		helpScreen = new HelpScreen(this);
		optionsScreen = new OptionsScreen(this);
		creditsScreen = new CreditsScreen(this);
		splashScreen = new SplashScreen(this);
		gameOptionsScreen = new GameOptionsScreen(this);
		
		// Set screen to game
		setScreen(loadingScreen);
		
		// Initialize FPS logger if dev mode enabled
		if (DEV_MODE) {
			fpsLogger = new FPSLogger();
		}
	}
}
