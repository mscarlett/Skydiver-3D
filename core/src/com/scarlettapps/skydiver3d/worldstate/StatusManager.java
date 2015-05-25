// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.worldstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.scarlettapps.skydiver3d.Skydiver3D;
import com.scarlettapps.skydiver3d.world.Collectible;
import com.scarlettapps.skydiver3d.world.Collectibles;
import com.scarlettapps.skydiver3d.world.GameObject;
import com.scarlettapps.skydiver3d.world.Skydiver;
import com.scarlettapps.skydiver3d.world.World;

/**
 * The StatusManager stores the current game state and world state.
 */
public class StatusManager {

	private final Array<StatusListener> listeners;
	private final Status status;
	
	public StatusManager(InputManager inputManager, Status status) {
		listeners = new Array<StatusListener>();
		this.status = status;
		
		InputListener stickyListener = new StickyListener(status);
		inputManager.addListener(stickyListener);
		
		reset();
	}
	
	public void addListener(StatusListener listener) {
		listeners.add(listener);
	}

	public void update(float delta) {		
		status.switchState = checkSwitchState();
		
		for (StatusListener listener : listeners) {
			listener.update(delta, status);
		}
		
		if (status.justCollected()) {
			status.addTimeSinceCollected(delta);
		}
		
		if (status.collected() && status.timeSinceCollected() > status.displayScoreTime()) {
			status.setCollected(false);
		} else {
			status.addTimeSinceCollected(delta);
		}

		if (status.jumpedOffAirplane()) {
			status.addSkydivingTime(delta);
		}
	}

	private boolean checkSwitchState() {
		WorldState worldState = status.getState();
		
		if (status.position().z >= worldState.minAltitude) {
			return false;
		}

		if (Skydiver3D.DEV_MODE) {
			Gdx.app.log(Skydiver3D.LOG, "Switching state from " + worldState
					+ " to " + worldState.next);
		}

		status.setState(worldState.next);

		return true;
	}

	public void reset() {
		if (Skydiver3D.DEV_MODE) {
			Gdx.app.log(Skydiver3D.LOG, "Resetting StatusManager");
		}
		
		status.reset();
		status.switchState = false;
	}

	public boolean switchState() {
		return status.switchState;
	}

	public Status getStatus() {
		return status;
	}
}
