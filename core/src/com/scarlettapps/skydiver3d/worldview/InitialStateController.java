package com.scarlettapps.skydiver3d.worldview;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.scarlettapps.skydiver3d.world.Skydiver;
import com.scarlettapps.skydiver3d.world.World;
import com.scarlettapps.skydiver3d.worldstate.Status;
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
		Renderer renderer = worldView.getRenderer();
		World world = renderer.getWorld();
		//StatusManager statusManager = worldView.getStatusManager();
		PerspectiveCamera cam = renderer.getCam();
		
		Skydiver skydiver = world.getSkydiver();

		if (skydiver.jumpedOffAirplane()) {
			totalTime += delta;
			
			dz -= delta*2f;
			dy -= delta*3f;
			dx -= delta*1f;
			
			float newX = -0.3f*WorldView.CAM_OFFSET+dx;
			float newY = -0.1f*WorldView.CAM_OFFSET+dy;
			float newZ = 0.4f*WorldView.CAM_OFFSET+dz+skydiver.getPositionZ();
			camOffset.set(newX, newY, newZ);
			
			Status status = Status.getInstance();
			
			tmp2.set(camOffset).sub(status.position());
			tmp2.scl(totalTime/5f);
			camOffset.sub(tmp2);
			
	        cam.position.set(camOffset);
	        cam.lookAt(skydiver.getPositionX()+dx,skydiver.getPositionY(),skydiver.getPositionZ()+0.3f*WorldView.CAM_OFFSET);
	        cam.up.set(Vector3.Z);
		} else {
			Status status = Status.getInstance();
	        cam.position.set(camOffset);
	        cam.lookAt(status.position().x+dx,status.position().y,status.position().z+0.3f*WorldView.CAM_OFFSET);  
		}
		cam.update();
	}
	
	@Override
	public void render(float delta) {	
		Renderer renderer = worldView.getRenderer();
		renderer.drawSky();
		renderer.drawSkydiverAndPlane();
		worldView.getStatusView().drawJumpOffPlane();
	}

	@Override
	public void initialize() {
		dx = 2.2f;
		dy = -0.2f;
		dz = -0.21019554f;
		totalTime = 0;
		
		float camOffsetX = -0.3f*WorldView.CAM_OFFSET+dx;
		float camOffsetY = -0.1f*WorldView.CAM_OFFSET+dy;
		float camOffsetZ = Skydiver.STARTING_HEIGHT+0.4f*WorldView.CAM_OFFSET+dz;
		camOffset = new Vector3(camOffsetX, camOffsetY, camOffsetZ);
		
		PerspectiveCamera cam = worldView.getRenderer().getCam();
        cam.direction.set(0,0,-1);
        cam.up.set(Vector3.Z);
        cam.near = 0.1f;
        cam.far = 100f;
        cam.update();
	}
	
}