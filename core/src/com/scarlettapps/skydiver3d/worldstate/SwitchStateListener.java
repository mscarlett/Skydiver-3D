package com.scarlettapps.skydiver3d.worldstate;

import com.badlogic.gdx.utils.Array;
import com.scarlettapps.skydiver3d.world.GameObject;
import com.scarlettapps.skydiver3d.world.World;

public class SwitchStateListener implements StatusListener {

	private World world;
	
	public SwitchStateListener(World world) {
		this.world = world;
	}
	
	@Override
	public boolean update(float delta, Status status) {
		if (status.switchState) {
			Array<GameObject> objects = world.getObjects();
			WorldState worldState = status.worldState();
			
			for (GameObject object : objects) {
				object.onWorldStateChanged(worldState);
			}
		}
		
		return false;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
	
}