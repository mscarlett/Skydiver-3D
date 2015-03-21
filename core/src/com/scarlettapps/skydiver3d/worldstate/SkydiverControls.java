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
	private float elapsedTime = 0f;
	private World world;
	private StatusManager statusManager;
	
	public SkydiverControls(World world, StatusManager statusManager) {
		this.world = world;
		this.statusManager = statusManager;
	}
	
	@Override
	public boolean update(GameController gameController, float delta) {
					
		switch (statusManager.worldState()) {
			case INITIAL:
				Skydiver skydiver = world.getSkydiver();
				if (gameController.justTouched() && !skydiver.jumpedOffAirplane) {
					skydiver.jumpOffAirplane();
					statusManager.setJumpedOffAirplane(true);
				}
				break;
			case SKYDIVING:
				skydiver = world.getSkydiver();
				if (gameController.getFaster()) {
					skydiver.addToVelocity(0, 0, -25 * delta);
				}
				skydiver.addToVelocity(delta*gameController.getAx(), delta*gameController.getAy(), 0);
				skydiver.skydiverAngle.x += 10 * delta*gameController.getAx();
				break;
			case PARACHUTING:
				if (!statusManager.justOpenedParachute()) {
					elapsedTime += delta;
					statusManager.setJustOpenedParachute(Gdx.input.justTouched() || elapsedTime > PARACHUTING_TIME_LIMIT);
				}
				skydiver = world.getSkydiver();
				statusManager.velocity().x = 0;
				statusManager.velocity().y = 0;
				statusManager.position().x = Math.signum(statusManager.position().x)*(Math.abs(statusManager.position().x)-0.5f*delta);
				statusManager.position().y = Math.signum(statusManager.position().y)*(Math.abs(statusManager.position().y)-0.5f*delta);
				
				if (statusManager.justOpenedParachute()) {
					if (!statusManager.parachuteDeployed()) {
						skydiver.deployParachute();
						statusManager.setParachuteDeployed(true);
						
						
					}
					
					statusManager.velocity().z -= 15*Math.signum(statusManager.velocity().z+30)*delta;
				}
					break;
			case LANDING:
				skydiver = world.getSkydiver();
				float accuracy = statusManager.getAccuracy();
				float error = 3-2*accuracy;
				skydiver.addToVelocity(15*gameController.getAx()*error*delta,15*gameController.getAy()*error*delta,0);
				statusManager.velocity().z = -8f*(1.7f-accuracy)*(7*statusManager.position().z/1000f+1);
				statusManager.velocity().x += 3*(Math.signum(statusManager.velocity().x) == 0 ? Math.random() : Math.signum(statusManager.velocity().x))*Math.abs(Math.random()*delta);
				if (Gdx.app.getType() != ApplicationType.Android) {
					statusManager.velocity().y += 3*(Math.signum(statusManager.velocity().y) == 0 ? Math.random() : Math.signum(statusManager.velocity().y))*Math.abs(Math.random()*delta);
				}
				skydiver.landing = true;
				Vector3 pos = statusManager.position();
				float dist2 = pos.x*pos.x+pos.y*pos.y;
				if (dist2 < 2027) {
					if (pos.z < 9) {
						statusManager.velocity().z = 0;
						statusManager.setState(WorldState.FINAL);
					}
				} else if (dist2 > 2027 && dist2 < 3550) {
					if (pos.z < 20) {
						statusManager.velocity().z = 0;
						statusManager.setState(WorldState.FINAL);
					}
				} else {
					if (pos.z < 9) {
						Terrain terrain = world.getTerrain();
						float altitude = terrain.getAltitude(pos.x, pos.y);
						if (altitude > 0 || pos.z < -3) {
							if (altitude < 0) {
								world.getSkydiver().setRender(false);
							}
							statusManager.velocity().z = 0;
							statusManager.setState(WorldState.FINAL);
							SoundFactory.getInstance().play(SoundType.LAUGH);
						}
					}
				}
				break;
			case FINAL:
				skydiver = world.getSkydiver();
				statusManager.velocity().set(0,0,0);
				skydiver.finalState = true;
				break;
		}
		return false;
	}
}
