package com.scarlettapps.skydiver3d.worldview;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.scarlettapps.skydiver3d.world.Collectibles;
import com.scarlettapps.skydiver3d.world.Skydiver;
import com.scarlettapps.skydiver3d.world.World;
import com.scarlettapps.skydiver3d.worldstate.StatusManager;
import com.scarlettapps.skydiver3d.worldview.ui.StatusView;

class SkydivingStateController implements WorldViewController {
	
	private static final int COLLECTIBLES_OFFSET = 300;
	private final WorldView worldView;
	
	public SkydivingStateController(WorldView worldView) {
		this.worldView = worldView;
	}
	
	public void initialize() {
		PerspectiveCamera cam = worldView.getRenderer().getCam();
		StatusManager statusManager = worldView.getStatusManager();
		StatusView statusView = worldView.getStatusView();
		cam.position.set(0, 0.5f,statusManager.position().z + WorldView.CAM_OFFSET);
        cam.direction.set(0,0,-1);
        cam.near = 1f;
        cam.far = 9000f;
        cam.update();
        statusManager.position().x = (Skydiver.MIN_X+Skydiver.MAX_X)/2;
        statusManager.position().y = (Skydiver.MIN_Y+Skydiver.MAX_Y)/2;
        statusView.showSpeedIcon(true);
	}
	
	@Override
	public void update(float delta) {
		Renderer renderer = worldView.getRenderer();
		StatusManager statusManager = worldView.getStatusManager();
		PerspectiveCamera cam = renderer.getCam();
		World world = renderer.getWorld();
		cam.position.z = statusManager.position().z + WorldView.CAM_OFFSET;
		cam.up.set(Vector3.Y);
		cam.update();
		Collectibles collectibles = world.getCollectibles();
		collectibles.setToRender(cam.position.z, COLLECTIBLES_OFFSET);
		statusManager.addToSkydivingTime(delta);
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
		StatusManager statusManager = worldView.getStatusManager();
		PerspectiveCamera cam = renderer.getCam();
		StatusView statusView = worldView.getStatusView();
		
		if (statusManager.collected()) {
			float displayScoreTime = statusManager.displayScoreTime();
			if (displayScoreTime < 1) {
				Vector3 intersectPoint = statusManager.intersectPoint();
				if (displayScoreTime == 0) {
					intersectPoint.set(statusManager.position());
					cam.project(intersectPoint);
					statusManager.addToScore(1000);
				}
				statusView.drawCollected();
        	} else {
        		statusManager.setDisplayScoreTime(0);
        		statusManager.setCollected(false);
        	}
		}
		statusView.drawHud();
	}
	
}