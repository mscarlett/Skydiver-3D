package com.scarlettapps.skydiver3d.worldview;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.scarlettapps.skydiver3d.world.Skydiver;
import com.scarlettapps.skydiver3d.world.Target;
import com.scarlettapps.skydiver3d.world.World;
import com.scarlettapps.skydiver3d.worldstate.StatusManager;

class InitialStateController implements WorldViewController {

	private float dx, dy, dz;
	private float totalTime;
	
	private Vector3 camOffset;
	
	private Vector3 tmp2 = new Vector3();
	
	private final WorldView worldView;
	
	public InitialStateController(WorldView worldView) {
		this.worldView = worldView;
	}

	@Override
	public void update(float delta) {
		World world = worldView.getWorld();
		StatusManager statusManager = worldView.getStatusManager();
		PerspectiveCamera cam = worldView.getCam();
		
		Skydiver skydiver = world.getSkydiver();

		if (skydiver.jumpedOffAirplane()) {
			totalTime += delta;
			dz -= Math.signum(dz)*delta*0.1f;
			dy -= Math.signum(dy)*delta*0.1f;
			dx -= Math.signum(dx)*delta*0.1f;
			camOffset.set(-0.3f*WorldView.CAM_OFFSET+dx,-0.1f*WorldView.CAM_OFFSET+dy,0.4f*WorldView.CAM_OFFSET+dz+skydiver.getPositionZ());
			tmp2.set(camOffset).sub(statusManager.position());
			tmp2.scl(totalTime/5f);
			camOffset.sub(tmp2);
	        cam.position.set(camOffset);
	        cam.lookAt(skydiver.getPositionX(),skydiver.getPositionY(),skydiver.getPositionZ()+0.2f*WorldView.CAM_OFFSET);
	        cam.up.set(Vector3.Z);
		} else {
	        cam.position.set(camOffset);
	        cam.lookAt(statusManager.position().x,statusManager.position().y,statusManager.position().z+0.2f*WorldView.CAM_OFFSET);
	        
		}
		cam.update();
	}
	
	@Override
	public void render(float delta) {	
		World world = worldView.getWorld();
		worldView.drawSkydiverAndPlane();
		worldView.getStatusView().drawJumpOffPlane(world.getSkydiver());
	}

	@Override
	public void initialize() {
		dx = 10.245517f;
		dy = 3.8138173f;
		dz = -1.71019554f;
		totalTime = 0;
		camOffset = new Vector3(-0.3f*WorldView.CAM_OFFSET+dx,-0.1f*WorldView.CAM_OFFSET+dy,Skydiver.STARTING_HEIGHT+0.4f*WorldView.CAM_OFFSET+dz);
		PerspectiveCamera cam = worldView.getCam();
        cam.direction.set(0,0,-1);
        cam.up.set(Vector3.Z);
        cam.near = 1f;
        cam.far = 9000f;
        cam.update();
        World world = worldView.getWorld();
        Target target = world.getTarget();
        target.setRender(false);
	}
	
}