package com.scarlettapps.skydiver3d.worldstate;

public class StickyListener implements InputListener {

	private StatusManager status;
	
	public StickyListener(StatusManager status) {
		this.status = status;
	}
	
	@Override
	public boolean update(GameController gameController, float delta) {
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
}
