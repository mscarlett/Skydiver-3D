package com.scarlettapps.skydiver3d.worldstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.scarlettapps.skydiver3d.Skydiver3D;
import com.scarlettapps.skydiver3d.world.Collectible;
import com.scarlettapps.skydiver3d.world.Collectibles;
import com.scarlettapps.skydiver3d.world.Skydiver;
import com.scarlettapps.skydiver3d.world.World;

public class CheckIntersectListener implements StatusListener {
	
	private final World world;
	
	public CheckIntersectListener(World world) {
		this.world = world;
	}
	
	@Override
	public boolean update(float delta, Status status) {
		if (status.worldState() == WorldState.SKYDIVING) {
		    checkIntersect(status);
		}
		return false;
	}
	
	private void checkIntersect(Status status) {		
		Vector3 skydiverPosition = status.position();
		Vector3 skydiverVelocity = status.velocity();
		
		Collectibles collectibles = world.getCollectibles();
		Skydiver skydiver = world.getSkydiver();
		if (collectibles.checkIntersect(skydiverPosition.z)) {
			Collectible closest = collectibles.getClosest();
			if (closest != null && skydiver.intersects(closest)) {
				if (closest.isDangerous()) {
					world.playBoom();
				} else {
				    world.playBell();
				}
				collectibles.removeClosest();
				status.setCollected(true);
				if (Skydiver3D.DEV_MODE) {
					Gdx.app.log(Skydiver3D.LOG, "Collected collectible: "
							+ closest.getClass().getSimpleName());
				}
				float a = Skydiver.MIN_TERMINAL_SPEED;
				float b = Skydiver.MAX_TERMINAL_SPEED;
				float speedFactor = (-skydiverVelocity.z-a)/(b-a);
				status.addToScore(closest.getPoints()*(1f+speedFactor));
			}
		}
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
	}
}
