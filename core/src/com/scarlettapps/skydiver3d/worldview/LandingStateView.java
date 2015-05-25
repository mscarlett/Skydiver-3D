package com.scarlettapps.skydiver3d.worldview;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.scarlettapps.skydiver3d.worldstate.Status;

class LandingStateView implements WorldStateView {
	
	private final WorldView worldView;
	private final Status status;
	
	public LandingStateView(WorldView worldView, Status status) {
		this.worldView = worldView;
		this.status = status;
	}

	@Override
	public void update(float delta) {
		PerspectiveCamera cam = worldView.getRenderer().getCam();
		cam.position.x = status.position().x + 0.5f;
		cam.position.y = status.position().y;
		cam.position.z = status.position().z + WorldView.CAM_OFFSET;
		cam.update();
	}

	@Override
	public void render(float delta) { //TODO why does game crash
		Renderer renderer = worldView.getRenderer();
		renderer.drawTerrain();
		renderer.drawTargetAndSkydiver();
		worldView.getStatusView().drawHud();
	}

	@Override
	public void initialize() {
		
	}
	
}