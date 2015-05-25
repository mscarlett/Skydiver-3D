package com.scarlettapps.skydiver3d.worldstate;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.scarlettapps.skydiver3d.resources.AssetFactory.SoundType;
import com.scarlettapps.skydiver3d.resources.SoundFactory;
import com.scarlettapps.skydiver3d.world.Skydiver;
import com.scarlettapps.skydiver3d.world.Terrain;
import com.scarlettapps.skydiver3d.world.World;

public class SkydiverControls implements InputListener {

	private static final float PARACHUTING_TIME_LIMIT = 8f;
	private float elapsedTime;
	private final World world;
	private final Status status;
	
	public SkydiverControls(World world, Status status) {
		this.world = world;
		this.status = status;
		reset();
	}
	
	@Override
	public void reset() {
		elapsedTime = 0f;
	}
	
	@Override
	public boolean update(GameController gameController, float delta) {
		switch (status.worldState()) {
			case INITIAL:
				Skydiver skydiver = world.getSkydiver();
				if (gameController.justTouched() && !skydiver.jumpedOffAirplane()) {
					status.setJumpedOffAirplane(true);
				}
				break;
			case SKYDIVING:
				skydiver = world.getSkydiver();
				if (gameController.getFaster()) {
					skydiver.addToVelocity(0, 0, -25 * delta);
				}
				skydiver.addToVelocity(delta*gameController.getAx(), delta*gameController.getAy(), 0);
				skydiver.skydiverAngle().x += 10 * delta*gameController.getAx();
				break;
			case PARACHUTING:
				if (!status.parachuteDeployed()) {
					elapsedTime += delta;
					status.setJustOpenedParachute(Gdx.input.justTouched() || elapsedTime > PARACHUTING_TIME_LIMIT);
				}
				
				skydiver = world.getSkydiver();
				status.velocity().x = 0;
				status.velocity().y = 0;
				status.position().x = Math.signum(status.position().x)*(Math.abs(status.position().x)-0.5f*delta);
				status.position().y = Math.signum(status.position().y)*(Math.abs(status.position().y)-0.5f*delta);
				
				if (status.justOpenedParachute()) {
					if (!status.parachuteDeployed()) { //why is there if clause
						status.setParachuteDeployed(true);
					}
					
					status.velocity().z -= 15*Math.signum(status.velocity().z+30)*delta;
				}
				
				break;
			case LANDING:
				skydiver = world.getSkydiver();
				float accuracy = status.getAccuracy();
				float error = 3-2*accuracy;
				skydiver.addToVelocity(15*gameController.getAx()*error*delta,15*gameController.getAy()*error*delta,0);
				status.velocity().z = -8f*(1.7f-accuracy)*(7*status.position().z/1000f+1);
				status.velocity().x += 3*(Math.signum(status.velocity().x) == 0 ? Math.random() : Math.signum(status.velocity().x))*Math.abs(Math.random()*delta);
				if (Gdx.app.getType() != ApplicationType.Android) {
					status.velocity().y += 3*(Math.signum(status.velocity().y) == 0 ? Math.random() : Math.signum(status.velocity().y))*Math.abs(Math.random()*delta);
				}
				status.setLanding(true);
				Vector3 pos = status.position();
				float dist2 = pos.x*pos.x+pos.y*pos.y;
				if (dist2 < 2027) {
					if (pos.z < 9) {
						status.velocity().z = 0;
						status.setState(WorldState.FINAL);
					}
				} else if (dist2 > 2027 && dist2 < 3550) {
					if (pos.z < 20) {
						status.velocity().z = 0;
						status.setState(WorldState.FINAL);
					}
				} else {
					if (pos.z < 9) {
						Terrain terrain = world.getTerrain();
						float altitude = terrain.getAltitude(pos.x, pos.y);
						if (altitude > 0 || pos.z < -3) {
							if (altitude < 0) {
								world.getSkydiver().setRender(false);
							}
							status.velocity().z = 0;
							status.setState(WorldState.FINAL);
							SoundFactory.getInstance().play(SoundType.LAUGH);
						}
					}
				}
				break;
			case FINAL:
				skydiver = world.getSkydiver();
				status.velocity().set(0,0,0);
				skydiver.setFinalState(true);
				break;
		}
		return false;
	}
}
