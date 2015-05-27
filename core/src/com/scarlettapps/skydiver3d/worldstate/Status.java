package com.scarlettapps.skydiver3d.worldstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.scarlettapps.skydiver3d.Skydiver3D;
import com.scarlettapps.skydiver3d.world.Skydiver;

public class Status {

	public boolean switchState;
	
	public static final int MAX_LANDING_BONUS = 15000;
	public static final int MAX_PARACHUTING_BONUS = 5000;

	private final Vector3 skydiverPosition = new Vector3();
	private final Vector3 skydiverVelocity = new Vector3();
	
	private WorldState worldState;
	
	private int score;
	private int ringScore;
	private boolean justCollected;
	private float timeSinceCollected;
	private boolean isPaused;
	private boolean justOpenedParachute;
	private float skydivingTime;
	private boolean parachuteDeployed;
	private boolean jumpedOffAirplane;
	private int landingBonus;
	private int timeBonus;
	private int maxPoints;
	private float accuracy;
	private boolean collected;
	private boolean landing;
	private boolean parachuting;
	
	public void setState(WorldState state) {
		worldState = state;
	}

	public void reset() {
		if (Skydiver3D.DEV_MODE) {
			Gdx.app.log(Skydiver3D.LOG, "Resetting StatusManager");
		}
		
		score = 0;
		justCollected = false;
		timeSinceCollected = 0f;
		worldState = WorldState.INITIAL;
		isPaused = false;
		justOpenedParachute = false;
		skydivingTime = 0;
		parachuteDeployed = false;
		jumpedOffAirplane = false;
		displayScoreTime = 0;
		accuracy = 0;
		parachutingBonus = 0;
		collected = false;
		landing = false;
		parachuting = false;
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
		if (Skydiver3D.DEV_MODE) {
			Gdx.app.log(Skydiver3D.LOG, "Status setting jumpedOffAirplane to " + jumpedOffAirplane);
		}
		
		this.jumpedOffAirplane = jumpedOffAirplane;
	}

	public void setParachuteDeployed(boolean parachuteDeployed) {
		if (Skydiver3D.DEV_MODE) {
			Gdx.app.log(Skydiver3D.LOG, "Status setting parachuteDeployed to " + parachuteDeployed);
		}
		
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
		if (Skydiver3D.DEV_MODE) {
			Gdx.app.log(Skydiver3D.LOG, "Status setting justOpenedParachute to " + justOpenedParachute);
		}
		
		this.justOpenedParachute = justOpenedParachute;
	}

	private boolean calculatedParachuteBonus = false;
	
	public void setAccuracy(float accuracy) {
		if (Skydiver3D.DEV_MODE) {
			Gdx.app.log(Skydiver3D.LOG, "Status setting accuracy to " + accuracy);
		}
		
		this.accuracy = accuracy;
		if (!calculatedParachuteBonus) {
			calculateParachutingBonus();
			ringScore = score;
		}
	}

	public void setPaused(boolean isPaused) {
		if (Skydiver3D.DEV_MODE) {
			Gdx.app.log(Skydiver3D.LOG, "Status setting paused to " + isPaused);
		}
		
		this.isPaused = isPaused;
	}

	public int score() {
		return score;
	}

	public void addToScore(float points) {
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
		landingBonus = (int) Math.max(MAX_LANDING_BONUS - 10
				* (x * x + y * y + z * z), 0);
		score += landingBonus;
	}

	public void calculateTimeBonus() {
		timeBonus = (int) (10000 * ((4400 - 1000) / skydivingTime
				/ Skydiver.MIN_TERMINAL_SPEED - 1));
	}

	public void calculateMaxPoints() {
		final int l = MAX_LANDING_BONUS;
		final int p = 30000 + MAX_PARACHUTING_BONUS;
		maxPoints = l + p;
	}

	public int maxPoints() {
		return maxPoints;
	}

	public int rating() {
		return (int)(Math.min(5f * score / ((float) maxPoints), 5));
	}
	
	public void calculateParachutingBonus() {
		parachutingBonus = (int)(accuracy*MAX_PARACHUTING_BONUS);
		ringScore = score;
		score += parachutingBonus;
		calculatedParachuteBonus = true;
	}
	
	private int parachutingBonus;
	
	public int parachutingBonus() {
		return parachutingBonus;
	}
	
	public Score scoreSummary() {
		calculateMaxPoints();
		calculateLandingBonus();
		calculateParachutingBonus();
		return new Score(ringScore, parachutingBonus(), landingBonus(), rating());
	}

	public void setSticky() {
		stickyTime -= 3f;
	}
	
	private float stickyTime = 3f;
	
	public float getStickyTime() {
		return stickyTime;
	}
	
	public void setStickyTime(float time) {
		stickyTime = time;
	}

	public boolean isCompleted() {
		return getState() == WorldState.FINAL;
	}

	public void addTimeSinceCollected(float delta) {
		timeSinceCollected += delta;
	}

	public void addSkydivingTime(float delta) {
		skydivingTime += delta;
	}

	public void setParachuting(boolean b) {
		parachuting = b;
	}

	public boolean landing() {
		return landing;
	}

	public void setLanding(boolean b) {
		landing = b;
	}

	public boolean parachuting() {
		return parachuting;
	}
}
