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

	private boolean switchState;
	
	public StatusManager(InputManager inputManager) {
		InputListener stickyListener = new StickyListener();
		inputManager.addListener(stickyListener);
		reset();
	}

	public void update(float delta, World world) {
		Status status = Status.getInstance();
		
		if (status.justCollected()) {
			status.addTimeSinceCollected(delta);
		}

		checkIntersect(world);

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

	private void checkIntersect(World world) {
		Status status = Status.getInstance();
		Vector3 skydiverPosition = status.position();
		Vector3 skydiverVelocity = status.velocity();
		
		Collectibles collectibles = world.getCollectibles();
		Skydiver skydiver = world.getSkydiver();
		if (collectibles.checkIntersect(skydiverPosition.z)) {
			Collectible closest = collectibles.getClosest();
			if (closest != null && skydiver.intersects(closest)) {
				world.playBell();
				collectibles.removeClosest();
				status.setCollected(true);
				if (Skydiver3D.DEV_MODE) {
					Gdx.app.log(Skydiver3D.LOG, "Collected collectible: "
							+ closest.getClass().getSimpleName());
				}
				float a = Skydiver.MIN_TERMINAL_SPEED;
				float b = Skydiver.MAX_TERMINAL_SPEED;
				float speedFactor = (-skydiverVelocity.z-a)/(b-a);
				status.addToScore(closest.getPoints()*(1f+speedFactor));
			}
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
		Status.getInstance().reset();
		switchState = false;
	}

	public boolean switchState() {
		return switchState;
	}
}
