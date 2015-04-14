package com.scarlettapps.skydiver3d.worldstate;

public class StickyListener implements InputListener {

	public StickyListener() {
		
	}
	
	@Override
	public boolean update(GameController gameController, float delta) {
		Status status = Status.getInstance();
		
		float stickyTime = status.getStickyTime();
		if (stickyTime >= 3f) {
			gameController.sticky = false;
		} else {
			gameController.sticky = true;
			stickyTime += delta;
			status.setStickyTime(stickyTime);
		}
		return false;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
	}
}
