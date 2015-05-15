// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;

/**
 * Class that represents a screen.
 * @author Michael Scarlett
 *
 * @param <T> the Game instance
 */
public abstract class DefaultScreen<T extends Game> implements Screen {
	
	// Virtual width and height parameters that allow relative positioning
	// of screen objects to maintain consistency for different screen sizes
	public final static int VIRTUAL_WIDTH = 960;
	public final static int VIRTUAL_HEIGHT = 640;
	
	// Real-world width and height of the current screen
	protected static int width = Gdx.graphics.getWidth();
	protected static int height = Gdx.graphics.getHeight();
	
	// Keep an instance of the game so that we can switch screens
	protected final T game;
	
	protected boolean initialize;
	
	// Whether or not the screen should be disposed when it is hidden
	private final boolean disposeOnHide;

	/**
	 * Create a screen which disposes itself when hidden
	 * @param game the instance of the game
	 */
	public DefaultScreen (T game) {
		this(game, true);
	}
	
	/**
	 * Create a screen
	 * @param game the instance of the game
	 * @param disposeOnHide whether the dispose() method should be called when the screen is hidden
	 */
	public DefaultScreen (T game, boolean disposeOnHide) {
		this(game, true, true);
	}
	
	public DefaultScreen (T game, boolean disposeOnHide, boolean initialize) {
		this.game = game;
		this.disposeOnHide = disposeOnHide;
		this.initialize = initialize;
	}
	
	public final void initialize() {
		if (initialize) {
			initializeScreen();
			initialize = false;
		}
	}
	
	protected abstract void initializeScreen();
	
	/**
	 * Resize the screen
	 */
	@Override
	public void resize(int width, int height) {
		DefaultScreen.width = width;
		DefaultScreen.height = height;
		
		resizeScreen(width, height);
		
		if (Skydiver3D.DEV_MODE) {
			Gdx.app.log(Skydiver3D.LOG, "Resizing screen " + getName() + " to (" + width + ", " + height + ")");
		}
	}
	
	/**
	 * Helper method to allow custom implementation of functions that should be
	 * called when showing this screen. By default this does nothing.
	 * @param width the new screen width
	 * @param height the new screen height
	 */
	protected void resizeScreen(int width, int height) {}

	/**
	 * Set the InputProcessor which handles user input for this screen.
	 */
	public final void setInputProcessor() {
		InputProcessor inputProcessor = getInputProcessor();
		Gdx.input.setInputProcessor(inputProcessor);
	}
	
	/**
	 * Get the InputProcessor which handles user input for this screen.
	 * @return the InputProcessor
	 */
	protected abstract InputProcessor getInputProcessor();
	
	
	/**
	 * Get the name of the screen
	 * @return the screen name
	 */
	protected String getName() {
        return getClass().getSimpleName();
    }
	
	/**
	 * Get the ratio between real-world width and virtual-world width
	 * @return the ratio
	 */
	public static float xScale() {
		return ((float)width)/VIRTUAL_WIDTH;
	}
	
	/**
	 * Get the ratio between real-world height and virtual-world height
	 * @return the ratio
	 */
	public static float yScale() {
		return ((float)height)/VIRTUAL_HEIGHT;
	}
	
	/**
	 * Show the screen and set the current InputProcessor to the screen's InputProcessor
	 */
	@Override
	public final void show() {
		if (Skydiver3D.DEV_MODE) {
			Gdx.app.log(Skydiver3D.LOG, "Showing screen: " + getName());
		}
		
		initialize();
		showScreen();
		setInputProcessor();
	}
	
	/**
	 * Helper method to allow custom implementation of functions that should be
	 * called when showing this screen. By default this does nothing.
	 */
	protected void showScreen() {}
	
	/**
	 * Pause the screen
	 */
	@Override
	public final void pause() {
		if (Skydiver3D.DEV_MODE) {
			Gdx.app.log(Skydiver3D.LOG, "Pausing screen: " + getName());
		}
		pauseScreen();
	}
	
	/**
	 * Helper method to allow custom implementation of functions that should be
	 * called when pausing this screen.  By default this does nothing.
	 */
	protected void pauseScreen() {}
	
	/**
	 * Resume the screen
	 */
	@Override
	public final void resume() {
		if (Skydiver3D.DEV_MODE) {
			Gdx.app.log(Skydiver3D.LOG, "Resuming screen: " + getName());
		}
		resumeScreen();
	}
	
	/**
	 * Helper method to allow custom implementation of functions that should be
	 * called when resuming this screen.  By default this does nothing.
	 */
	protected void resumeScreen() {}
	
	/**
	 * Hide the screen
	 */
	@Override
	public final void hide() {
		if (Skydiver3D.DEV_MODE) {
			Gdx.app.log(Skydiver3D.LOG, "Hiding screen: " + getName());
		}
		hideScreen();
		
		if (disposeOnHide) {
			dispose();
		}
	}
	
	/**
	 * Helper method to allow custom implementation of functions that should be
	 * called when hiding this screen. By default this does nothing.
	 */
	protected void hideScreen() {}
	
	/**
	 * Dispose the screen
	 */
	@Override
	public final void dispose() {
		if (Skydiver3D.DEV_MODE) {
			Gdx.app.log(Skydiver3D.LOG, "Disposing screen: " + getName());
		}
		disposeScreen();
	}
	
	/**
	 * Helper method to allow custom implementation of functions that should be
	 * called when disposing this screen. By default this does nothing.
	 */
	protected void disposeScreen() {}
	
	/**
	 * Transition from this screen to the next screen by using a fade effect
	 * @param next the next screen to show
	 * @param duration the duration of the transition
	 */
	protected void transitionScreen(DefaultScreen<?> next, float duration) {
		TransitionScreen transitionScreen = new TransitionScreen(game, this, next, duration);
		
		if (Skydiver3D.DEV_MODE) {
			Gdx.app.log(Skydiver3D.LOG, "Transitioning from " + getName() + " to " + next.getClass().getName());
		}
		game.setScreen(transitionScreen);
	}
	
	/**
	 * Get the real-world screen width
	 * @return the width
	 */
	public static int width() {
		return width;
	}
	
	/**
	 * Get the real-world screen height
	 * @return the height
	 */
	public static int height() {
		return height;
	}
}