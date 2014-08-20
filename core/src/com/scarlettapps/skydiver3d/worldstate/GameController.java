// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.worldstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.scarlettapps.skydiver3d.resources.PreferenceFactory;

public abstract class GameController implements InputProcessor {
	
	protected static float sensitivity = PreferenceFactory.getSensitivity();
	
	protected Vector2 touchPosition;
	protected float ax = 0;
	protected float ay = 0;
	protected boolean faster = false;
	protected boolean justTouched;

	protected boolean sticky = false;
	
	public final float getAx() {
		return ax*sensitivity;
	}
	
	public final float getAy() {
		return ay*sensitivity;
	}
	
	public final boolean getFaster() {
		return faster || sticky;
	}
	
	public final Vector2 getTouchPosition() {
		return touchPosition;
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		justTouched = true;
		return false;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		justTouched = false;
		return false; //check if the menu buttons were pressed
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	public static final GameController newGameController() {
		switch (Gdx.app.getType()) {
			case Android:
				return new AndroidGameController();
			case Desktop:
				return new DesktopGameController();
			default:
				throw new GdxRuntimeException("Unknown app type");
		}
	}

	public abstract void update(float delta);
	
	public static void setSensitivity(float sensitivity) {
		GameController.sensitivity = sensitivity;
	}

	public boolean justTouched() {
		return justTouched;
	}
}
