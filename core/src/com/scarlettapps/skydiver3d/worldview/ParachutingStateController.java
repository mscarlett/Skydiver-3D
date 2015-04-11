package com.scarlettapps.skydiver3d.worldview;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.scarlettapps.skydiver3d.world.Skydiver;
import com.scarlettapps.skydiver3d.world.Target;
import com.scarlettapps.skydiver3d.world.World;
import com.scarlettapps.skydiver3d.worldstate.StatusManager;
import com.scarlettapps.skydiver3d.worldview.ui.AccuracyMeter;
import com.scarlettapps.skydiver3d.worldview.ui.StatusView;

class ParachutingStateController implements WorldViewController {
	
	private boolean switchCam;
	private final WorldView worldView;
	
	public ParachutingStateController(WorldView worldView) {
		this.worldView = worldView;
	}
	
	@Override
	public void update(float delta) {
		World world = worldView.getWorld();
		StatusManager statusManager = worldView.getStatusManager();
		StatusView statusView = worldView.getStatusView();
		PerspectiveCamera cam = worldView.getRenderer().getCam();
		Skydiver skydiver = world.getSkydiver();
		
		AccuracyMeter accuracyMeter = statusView.getAccuracyMeter();
		boolean touched = statusManager.justOpenedParachute();
		if (touched) {
			accuracyMeter.stop();
			statusManager.setAccuracy(accuracyMeter.getAccuracy());
			switchCam = touched;
		}
		if (switchCam) {
			cam.position.x = statusManager.position().x + 0.5f;
			cam.position.y = statusManager.position().y;
			cam.position.z = statusManager.position().z + WorldView.CAM_OFFSET;
			cam.direction.set(0,0,-1);
			cam.up.set(Vector3.Y);
			cam.near = 0.1f;
		} else {
			cam.position.x = statusManager.position().x;
			cam.position.y = statusManager.position().y+4f;
			cam.position.z = statusManager.position().z-2f;
			cam.lookAt(statusManager.position().x, statusManager.position().y, statusManager.position().z+2f);
			cam.up.set(Vector3.Z);
		}
		cam.update();
		accuracyMeter.act(delta);
		skydiver.setParachuting(true);
	}

	@Override
	public void render(float delta) {
		Renderer renderer = worldView.getRenderer();
		renderer.drawTerrain();
		renderer.drawTargetAndSkydiver();
		StatusView statusView = worldView.getStatusView();
		statusView.drawParachuteCaption();
		statusView.drawHud();
	}

	@Override
	public void initialize() {
		switchCam = false;
		StatusView statusView = worldView.getStatusView();
		statusView.showSpeedIcon(false);
	}
}