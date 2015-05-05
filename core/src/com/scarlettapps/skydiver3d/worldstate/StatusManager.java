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
	private final World world;
	
	private boolean switchState;
	
	public StatusManager(InputManager inputManager, World world) {
		InputListener stickyListener = new StickyListener();
		inputManager.addListener(stickyListener);
		listeners = new Array<StatusListener>();
		listeners.add(new CheckIntersectListener(world));
		
		this.world = world;
		
		reset();
	}
	
	public void addListener(StatusListener listener) {
		listeners.add(listener);
	}

	public void update(float delta) {
		for (StatusListener listener : listeners) {
			listener.update(delta);
		}
		
		Status status = Status.getInstance();
		
		if (status.justCollected()) {
			status.addTimeSinceCollected(delta);
		}

		if (switchState = checkSwitchState()) {
			Array<GameObject> objects = world.getObjects();
			WorldState worldState = status.worldState();
			
			for (GameObject object : objects) {
				object.onWorldStateChanged(worldState);
			}
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
		Status status = Status.getInstance();
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
		
		Status.getInstance().reset();
		switchState = false;
	}

	public boolean switchState() {
		return switchState;
	}
}
