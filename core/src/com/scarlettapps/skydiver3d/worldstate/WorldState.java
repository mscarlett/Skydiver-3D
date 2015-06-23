package com.scarlettapps.skydiver3d.worldstate;

import com.badlogic.gdx.utils.GdxRuntimeException;

public enum WorldState implements AbstractWorldState {
	FINAL(-21, null),
    LANDING(-20, FINAL),
    PARACHUTING(400, LANDING),
    SKYDIVING(1000, PARACHUTING),
    INITIAL(4400, SKYDIVING);

	public final int minAltitude;
	public final WorldState next;

	private WorldState(int minAltitude, WorldState next) {
		this.minAltitude = minAltitude;
		this.next = next;
	}

	@Override
	public void onWorldStateBegin(StatusManager statusManager) {
	 
	}
	
	@Override
	public void onWorldStateEnd(StatusManager statusManager) {
	
	}
	
	@Override
	public boolean switchState(int altitude) {
		return altitude < minAltitude;
	}
	
	@Override
	public boolean isCompleted() {
		return this == WorldState.FINAL;
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