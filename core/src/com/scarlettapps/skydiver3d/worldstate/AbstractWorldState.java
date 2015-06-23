package com.scarlettapps.skydiver3d.worldstate;

public interface AbstractWorldState {
	public void onWorldStateBegin(StatusManager statusManager);
	public void onWorldStateEnd(StatusManager statusManager);
	public boolean switchState(int altitude);
	public boolean isCompleted();
}
