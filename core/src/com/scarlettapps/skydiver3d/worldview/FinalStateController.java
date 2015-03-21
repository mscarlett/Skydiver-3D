package com.scarlettapps.skydiver3d.worldview;

import com.badlogic.gdx.Gdx;
import com.scarlettapps.skydiver3d.Skydiver3D;
import com.scarlettapps.skydiver3d.worldstate.StatusManager;
import com.scarlettapps.skydiver3d.worldview.ui.StatusView;

class FinalStateController implements WorldViewController {

	private final WorldView worldView;
	
	public FinalStateController(WorldView worldView) {
		this.worldView = worldView;
	}

	@Override
	public void update(float delta) {
		
	}

	@Override
	public void render(float delta) {
		worldView.drawTerrain();
		worldView.drawTargetAndSkydiver();
	}

	@Override
	public void initialize() {
		StatusManager statusManager = worldView.getStatusManager();
		StatusView statusView = worldView.getStatusView();
		statusManager.calculateTimeBonus();
		statusManager.calculateLandingBonus();
		if (Skydiver3D.DEV_MODE) {
			Gdx.app.log(Skydiver3D.LOG, "Loanded at position " + statusManager.position());
		}
		statusView.hidePause();
	}
	
}