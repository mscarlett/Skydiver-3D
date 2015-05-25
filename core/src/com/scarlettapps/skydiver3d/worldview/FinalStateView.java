package com.scarlettapps.skydiver3d.worldview;

import com.badlogic.gdx.Gdx;
import com.scarlettapps.skydiver3d.Skydiver3D;
import com.scarlettapps.skydiver3d.worldstate.Status;
import com.scarlettapps.skydiver3d.worldstate.StatusManager;
import com.scarlettapps.skydiver3d.worldview.ui.StatusView;

class FinalStateView implements WorldStateView {

	private final WorldView worldView;
	private final Status status;
	
	public FinalStateView(WorldView worldView, Status status) {
		this.worldView = worldView;
		this.status = status;
	}

	@Override
	public void update(float delta) {
		
	}

	@Override
	public void render(float delta) {
		Renderer renderer = worldView.getRenderer();
		renderer.drawTerrain();
		renderer.drawTargetAndSkydiver();
	}

	@Override
	public void initialize() {
		StatusView statusView = worldView.getStatusView();
		status.calculateTimeBonus();
		status.calculateLandingBonus();
		if (Skydiver3D.DEV_MODE) {
			Gdx.app.log(Skydiver3D.LOG, "Loanded at position " + status.position());
		}
		statusView.hidePause();
	}
	
}