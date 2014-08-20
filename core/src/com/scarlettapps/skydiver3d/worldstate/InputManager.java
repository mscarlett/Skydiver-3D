// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.worldstate;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class InputManager {
	
	private final GameController gameController;
	private final Array<InputListener> listeners;

	public InputManager(GameController gameController) {
		this.gameController = gameController;
		listeners = new Array<InputListener>();
	}
	
	public void addListener(InputListener listener) {
		listeners.add(listener);
	}
	
	public void update(float delta) {
		gameController.update(delta);
		
		for (InputListener listener : listeners) {
			if (listener.update(gameController, delta)) {
				listeners.removeValue(listener, true);
			}
		}
	}

	protected Vector2 getTouchPoint() {
		return gameController.getTouchPosition();
	}

}
