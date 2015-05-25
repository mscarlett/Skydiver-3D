package com.scarlettapps.skydiver3d.worldview;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.scarlettapps.skydiver3d.world.Collectibles;
import com.scarlettapps.skydiver3d.world.Skydiver;
import com.scarlettapps.skydiver3d.world.World;
import com.scarlettapps.skydiver3d.worldstate.Status;
import com.scarlettapps.skydiver3d.worldstate.StatusManager;
import com.scarlettapps.skydiver3d.worldview.ui.StatusView;

class SkydivingStateView implements WorldStateView {
	
	private static final int COLLECTIBLES_OFFSET = 300;
	private final WorldView worldView;
	private final Status status;
	
	public SkydivingStateView(WorldView worldView, Status status) {
		this.worldView = worldView;
		this.status = status;
	}
	
	public void initialize() {
		PerspectiveCamera cam = worldView.getRenderer().getCam();
		StatusView statusView = worldView.getStatusView();
		cam.position.set(0, 0.5f,status.position().z + WorldView.CAM_OFFSET);
        cam.direction.set(0,0,-1);
        cam.near = 1f;
        cam.far = 9000f;
        cam.update();
        status.position().x = (Skydiver.MIN_X+Skydiver.MAX_X)/2;
        status.position().y = (Skydiver.MIN_Y+Skydiver.MAX_Y)/2;
        statusView.showSpeedIcon(true);
	}
	
	@Override
	public void update(float delta) {
		Renderer renderer = worldView.getRenderer();
		PerspectiveCamera cam = renderer.getCam();
		World world = renderer.getWorld();
		cam.position.z = status.position().z + WorldView.CAM_OFFSET;
		cam.up.set(Vector3.Y);
		cam.update();
		Collectibles collectibles = world.getCollectibles();
		collectibles.setToRender(cam.position.z, COLLECTIBLES_OFFSET);
		status.addToSkydivingTime(delta);
	}

	@Override
	public void render(float delta) {
		renderSkydiving(delta);
	}
	
	private void renderSkydiving(float delta) {
		Renderer renderer = worldView.getRenderer();
		renderer.drawTerrain();
		renderer.drawTargetAndSkydiver();
		renderer.drawCollectibles();
		PerspectiveCamera cam = renderer.getCam();
		StatusView statusView = worldView.getStatusView();
		
		if (status.collected()) {
			float displayScoreTime = status.displayScoreTime();
			if (displayScoreTime < 1) {
				Vector3 intersectPoint = status.intersectPoint();
				if (displayScoreTime == 0) {
					intersectPoint.set(status.position());
					cam.project(intersectPoint);
					status.addToScore(1000);
				}
				statusView.drawCollected();
        	} else {
        		status.setDisplayScoreTime(0);
        		status.setCollected(false);
        	}
		}
		statusView.drawHud();
	}
	
}