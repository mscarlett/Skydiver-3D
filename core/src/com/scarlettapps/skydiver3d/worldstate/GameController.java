// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.worldstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.scarlettapps.skydiver3d.Skydiver3D;
import com.scarlettapps.skydiver3d.resources.PreferenceFactory;

public abstract class GameController implements InputProcessor {
	
	protected static float sensitivity = PreferenceFactory.getInstance().getSensitivity();
	
	protected float ax;
	protected float ay;
	protected boolean faster;
	protected boolean justTouched;

	protected boolean sticky;
	
	public void reset() {
		if (Skydiver3D.DEV_MODE) {
			Gdx.app.log(Skydiver3D.LOG, "Resetting GameController");
		}
		
		ax = 0;
		ay = 0;
		faster = false;
		justTouched = false;
		sticky = false;
	}
	
	public final float getAx() {
		return ax*sensitivity;
	}
	
	public final float getAy() {
		return ay*sensitivity;
	}
	
	public final boolean getFaster() {
		return faster || sticky;
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
		GameController controller;
		switch (Gdx.app.getType()) {
			case Android:
				controller = new AndroidGameController();
				break;
			case Desktop:
				controller = new DesktopGameController();
				break;
			default:
				throw new GdxRuntimeException("Unknown app type");
		}
		controller.reset();
		return controller;
	}

	public abstract void update(float delta);
	
	public static void setSensitivity(float sensitivity) {
		GameController.sensitivity = sensitivity;
	}

	public boolean justTouched() {
		return justTouched;
	}
}
