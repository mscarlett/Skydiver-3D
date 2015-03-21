package com.scarlettapps.skydiver3d.worldview;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.scarlettapps.skydiver3d.worldstate.StatusManager;

class LandingStateController implements WorldViewController {
	
	private final WorldView worldView;
	
	public LandingStateController(WorldView worldView) {
		this.worldView = worldView;
	}

	@Override
	public void update(float delta) {
		PerspectiveCamera cam = worldView.getCam();
		StatusManager statusManager = worldView.getStatusManager();
		cam.position.x = statusManager.position().x + 0.5f;
		cam.position.y = statusManager.position().y;
		cam.position.z = statusManager.position().z + WorldView.CAM_OFFSET;
		cam.update();
	}

	@Override
	public void render(float delta) {
		worldView.drawTerrain();
		worldView.drawTargetAndSkydiver();
		worldView.getStatusView().drawHud();
	}

	@Override
	public void initialize() {
		
	}
	
}