package com.scarlettapps.skydiver3d.worldview;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.scarlettapps.skydiver3d.world.Skydiver;
import com.scarlettapps.skydiver3d.world.World;
import com.scarlettapps.skydiver3d.worldstate.Status;
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
		Renderer renderer = worldView.getRenderer();
		World world = renderer.getWorld();
		Status status = Status.getInstance();
		StatusView statusView = worldView.getStatusView();
		PerspectiveCamera cam = renderer.getCam();
		
		AccuracyMeter accuracyMeter = statusView.getAccuracyMeter();
		boolean touched = status.justOpenedParachute();
		
		if (touched) {
			accuracyMeter.stop();
			status.setAccuracy(accuracyMeter.getAccuracy());
			switchCam = touched;
			cam.direction.set(0,0,-1);
			cam.up.set(Vector3.Y);
			cam.near = 0.1f;
		}
		if (switchCam) {
			cam.position.x = status.position().x + 0.5f;
			cam.position.y = status.position().y;
			cam.position.z = status.position().z + WorldView.CAM_OFFSET;
		} else {
			cam.position.x = status.position().x;
			cam.position.y = status.position().y+4f;
			cam.position.z = status.position().z-2f;
			
			accuracyMeter.act(delta);
		}
		cam.update();
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
		Renderer renderer = worldView.getRenderer();
		
		World world = renderer.getWorld();
		Skydiver skydiver = world.getSkydiver();
		skydiver.setParachuting(true);
		
		PerspectiveCamera cam = renderer.getCam();
		cam.up.set(Vector3.Z);
		
		Status status = Status.getInstance();		
		cam.position.x = status.position().x;
		cam.position.y = status.position().y+4f;
		cam.position.z = status.position().z-2f;
		cam.lookAt(status.position().x, status.position().y, status.position().z+2f);
	}
}