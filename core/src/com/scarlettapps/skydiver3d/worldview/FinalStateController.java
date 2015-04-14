package com.scarlettapps.skydiver3d.worldview;

import com.badlogic.gdx.Gdx;
import com.scarlettapps.skydiver3d.Skydiver3D;
import com.scarlettapps.skydiver3d.worldstate.Status;
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
		Renderer renderer = worldView.getRenderer();
		renderer.drawTerrain();
		renderer.drawTargetAndSkydiver();
	}

	@Override
	public void initialize() {
		Status status = Status.getInstance();
		StatusView statusView = worldView.getStatusView();
		status.calculateTimeBonus();
		status.calculateLandingBonus();
		if (Skydiver3D.DEV_MODE) {
			Gdx.app.log(Skydiver3D.LOG, "Loanded at position " + status.position());
		}
		statusView.hidePause();
	}
	
}