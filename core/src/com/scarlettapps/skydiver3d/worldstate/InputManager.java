// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.worldstate;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.scarlettapps.skydiver3d.Skydiver3D;

public class InputManager {
	
	private final GameController gameController;
	private final Array<InputListener> listeners;
	private final List<InputListener> removedListeners;

	public InputManager(GameController gameController) {
		this.gameController = gameController;
		listeners = new Array<InputListener>();
		removedListeners = new LinkedList<InputListener>();
	}
	
	public void reset() {
		if (Skydiver3D.DEV_MODE) {
			Gdx.app.log(Skydiver3D.LOG, "Resetting InputManager");
		}
		
		for (InputListener listener: removedListeners) {
			listeners.add(listener);
		}
		for (InputListener listener: listeners) {
			listener.reset();
		}
	}
	
	public void addListener(InputListener listener) {
		listeners.add(listener);
	}
	
	public void update(float delta) {
		gameController.update(delta);
		
		for (InputListener listener : listeners) {
			if (listener.update(gameController, delta)) {
				listeners.removeValue(listener, true);
				removedListeners.add(listener);
			}
		}
	}

}
