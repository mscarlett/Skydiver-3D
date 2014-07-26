// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.world.gamestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.scarlettapps.skydiver3d.SkyDiver3D;
import com.scarlettapps.skydiver3d.world.Collectible;
import com.scarlettapps.skydiver3d.world.Collectibles;
import com.scarlettapps.skydiver3d.world.GameObject;
import com.scarlettapps.skydiver3d.world.Skydiver;
import com.scarlettapps.skydiver3d.world.World;


/*
 * Class to keep track of achievements.
 */
public class StatusManager {
	
	public static final int MAX_LANDING_BONUS = 10000;
	
	public static enum WorldState {
		FINAL(-1, null), LANDING(10, FINAL), PARACHUTING(400, LANDING), SKYDIVING(1000, PARACHUTING), INITIAL(4400, SKYDIVING);
		
		public final int minAltitude;
		public final WorldState next;
		
		private WorldState(int minAltitude, WorldState next) {
			this.minAltitude = minAltitude;
			this.next = next;
		}
		
		@Override
		public String toString() {
			switch (this) {
				case INITIAL:
					return "Initial";
				case SKYDIVING:
					return "Skydiving";
				case PARACHUTING:
					return "Parachuting";
				case LANDING:
					return "Landing";
				case FINAL:
					return "Final";
				default:
					throw new GdxRuntimeException("Invalid World State");
			}
		}
	}

	private int score;
	private boolean justCollected;
	private float timeSinceCollected;
	private WorldState worldState;
	//private Vector2 touchPoint;
	//private Vector2 intersectPoint;
	private Vector3 skydiverPosition;
	private Vector3 skydiverVelocity;
	private boolean isPaused;
	private boolean justOpenedParachute;
	private float skydivingTime;
	private boolean parachuteDeployed;
	private boolean jumpedOffAirplane;
	private boolean switchState;
	private int landingBonus;
	private int timeBonus;
	private int maxPoints;
	private float accuracy;
	
	public StatusManager(InputManager inputManager) {
		//TODO add pause listener to input manager?
		
		reset();
	}
	
	public void update(float delta, World world) {
		if (justCollected) {
			timeSinceCollected += delta;
		}
		
		checkIntersect(world);
		
		if (switchState = checkSwitchState()) {
			Array<GameObject> objects = world.getObjects();
			for (GameObject object: objects) {
				object.onWorldStateChanged(worldState);
			}
		}
	}
	
	private void checkIntersect(World world) {
		Collectibles collectibles = world.getCollectibles();
		Skydiver skydiver = world.getSkydiver();
		if (collectibles.checkIntersect(skydiverPosition.z)) {
			Collectible closest = collectibles.getClosest();
			if (closest != null) {
				if (skydiver.intersects(closest)) {
				    world.playBell();
					collectibles.removeClosest();
					collected = true;
				}
			}
		}
	}
	
	private boolean checkSwitchState() {
		if (skydiverPosition.z >= worldState.minAltitude) {
			return false;
		}
		
		if (SkyDiver3D.DEV_MODE) {
			Gdx.app.log(SkyDiver3D.LOG, "Switching state from " + worldState + " to " + worldState.next);
		}
		
		setState(worldState.next);
		
		return true;
	}

	private void setState(WorldState state) {
		worldState = state;
	}
	
	public void reset() {
		score = 0;
		justCollected = false;
		timeSinceCollected = 0f;
		worldState = WorldState.INITIAL;
		isPaused = false;
		justOpenedParachute = false;
		skydivingTime = 0;
		parachuteDeployed = false;
		jumpedOffAirplane = false;
		switchState = true;
		displayScoreTime = 0;
		accuracy = 0;
	}

	public int getScore() {
		return score;
	}
	
	public boolean justCollected() {
		return justCollected;
	}
	
	public float timeSinceCollected() {
		return timeSinceCollected;
	}

	public boolean isPaused() {
		return isPaused;
	}

	public float getAccuracy() {
		return accuracy;
	}

	public WorldState getState() {
		return worldState;
	}

	public boolean justOpenedParachute() {
		return justOpenedParachute;
	}
	
	public void setJumpedOffAirplane(boolean jumpedOffAirplane) {
		this.jumpedOffAirplane = jumpedOffAirplane;
	}
	
	public void setParachuteDeployed(boolean parachuteDeployed) {
		this.parachuteDeployed = parachuteDeployed;
	}
	
	public boolean parachuteDeployed() {
		return parachuteDeployed;
	}
	
	public boolean jumpedOffAirplane() {
		return jumpedOffAirplane;
	}
	
	public float skydivingTime() {
		return skydivingTime;
	}
	
	public void addToSkydivingTime(float delta) {
		skydivingTime += delta;
	}
	
	public WorldState worldState() {
		return worldState;
	}
	
	public void setCollected(boolean collected) {
		this.collected = collected;
	}
	
	private boolean collected = false;
	
	public boolean collected() {
		return collected;
	}
	
	public Vector3 position() {
		return skydiverPosition;
	}
	
	public Vector3 velocity() {
		return skydiverVelocity;
	}

	public void setJustOpenedParachute(boolean justOpenedParachute) {
		this.justOpenedParachute = justOpenedParachute;
	}

	public void setAccuracy(float accuracy) {
		this.accuracy = accuracy;
	}

	public void setPaused(boolean isPaused) {
		this.isPaused = isPaused;
	}
	
	public boolean switchState() {
		return switchState;
	}

	public int score() {
		return score;
	}

	public void addToScore(int points) {
		score += points;
	}
	
	private Vector3 intersectPoint = new Vector3();

	public Vector3 intersectPoint() {
		return intersectPoint;
	}
	
	private float displayScoreTime;

	public float displayScoreTime() {
		return displayScoreTime;
	}
	
	public void setDisplayScoreTime(float displayScoreTime) {
		this.displayScoreTime = displayScoreTime;
	}
	
	public void addToDisplayScoreTime(float delta) {
		this.displayScoreTime += delta;
	}

	public int landingBonus() {
		return landingBonus;
	}
	
	public int timeBonus() {
		return timeBonus;
	}
	
	public void calculateLandingBonus() {
		final float x = skydiverPosition.x;
		final float y = skydiverPosition.y;
		final float z = skydiverPosition.z;
		landingBonus = (int)Math.max(MAX_LANDING_BONUS-10*(x*x+y*y+z*z), 0);
	}
	
	public void calculateTimeBonus() {
		timeBonus = (int) (10000*((4400-1000)/skydivingTime/Skydiver.MIN_TERMINAL_SPEED-1));
	}
	
	public void calculateMaxPoints() {
		final int t = (int) (10000*(Skydiver.MAX_TERMINAL_SPEED)/Skydiver.MIN_TERMINAL_SPEED-1);
		final int l = MAX_LANDING_BONUS;
		final int p = 10000;
		maxPoints = t + l + p;
	}
	
	public int maxPoints() {
		return maxPoints;
	}

	public void syncSkydiver(Skydiver skydiver) {
		skydiverPosition = skydiver.getPosition();
		skydiverVelocity = skydiver.getVelocity();
	}

	public int rating() {
		calculateMaxPoints();
		calculateLandingBonus();
		return 5*((int)Math.min(5f*landingBonus/((float)maxPoints),1));
	}
 }
