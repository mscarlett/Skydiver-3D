// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.GdxRuntimeException;

public abstract class DefaultScreen<T extends Game> implements Screen {
	
	public final static int VIRTUAL_WIDTH = 960;
	public final static int VIRTUAL_HEIGHT = 640;
	
	protected static int width = Gdx.graphics.getWidth();
	protected static int height = Gdx.graphics.getHeight();
	
	protected final T game;
	
	private final boolean disposeOnHide;

	public DefaultScreen (T game) {
		this(game, true);
	}
	
	public DefaultScreen (T game, boolean disposeOnHide) {
		this.game = game;
		this.disposeOnHide = disposeOnHide;
	}
	
	@Override
	public void resize(int width, int height) {
		DefaultScreen.width = width;
		DefaultScreen.height = height;
		
		if (SkyDiver3D.DEV_MODE) {
			Gdx.app.log(SkyDiver3D.LOG, "Resizing screen " + getName() + " to (" + width + ", " + height + ")");
		}
	}

	public final void setInputProcessor() {
		InputProcessor inputProcessor = getInputProcessor();
		Gdx.input.setInputProcessor(inputProcessor);
	}
	
	protected abstract InputProcessor getInputProcessor();
	
	protected String getName() {
        return getClass().getSimpleName();
    }
	
	public static float xScale() {
		return ((float)width)/VIRTUAL_WIDTH;
	}
	
	public static float yScale() {
		return ((float)height)/VIRTUAL_HEIGHT;
	}
	
	@Override
	public final void show() {
		if (SkyDiver3D.DEV_MODE) {
			Gdx.app.log(SkyDiver3D.LOG, "Showing screen: " + getName());
		}
		showScreen();
		setInputProcessor();
	}
	
	protected abstract void showScreen();
	
	@Override
	public final void pause() {
		if (SkyDiver3D.DEV_MODE) {
			Gdx.app.log(SkyDiver3D.LOG, "Pausing screen: " + getName());
		}
		pauseScreen();
	}
	
	protected abstract void pauseScreen();
	
	@Override
	public final void resume() {
		if (SkyDiver3D.DEV_MODE) {
			Gdx.app.log(SkyDiver3D.LOG, "Resuming screen: " + getName());
		}
		resumeScreen();
	}
	
	protected abstract void resumeScreen();
	
	@Override
	public final void hide() {
		if (SkyDiver3D.DEV_MODE) {
			Gdx.app.log(SkyDiver3D.LOG, "Hiding screen: " + getName());
		}
		hideScreen();
		
		if (disposeOnHide) {
			dispose();
		}
	}
	
	protected abstract void hideScreen();
	
	@Override
	public final void dispose() {
		if (SkyDiver3D.DEV_MODE) {
			Gdx.app.log(SkyDiver3D.LOG, "Disposing screen: " + getName());
		}
		disposeScreen();
	}
	
	protected abstract void disposeScreen();
	
	protected void transitionScreen(Screen next, float duration) {
		TransitionScreen transitionScreen = new TransitionScreen(game, this, next, duration);
		
		if (SkyDiver3D.DEV_MODE) {
			Gdx.app.log(SkyDiver3D.LOG, "Transitioning from " + getName() + " to " + next.getClass().getSimpleName());
		}
		game.setScreen(transitionScreen);
	}
	
	public static int width() {
		return width;
	}
	
	public static int height() {
		return height;
	}
}