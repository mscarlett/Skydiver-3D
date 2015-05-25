package com.scarlettapps.skydiver3d.worldstate;

public interface StatusListener {

	public boolean update(float delta, Status status);
	
	public void reset();
}
