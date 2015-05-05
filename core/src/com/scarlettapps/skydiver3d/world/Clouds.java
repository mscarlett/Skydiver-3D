// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.world;

import com.badlogic.gdx.utils.Array;
import com.scarlettapps.skydiver3d.worldstate.WorldState;
import com.scarlettapps.skydiver3d.worldview.Renderer;

public class Clouds extends GameObject {
	
	private static final int NUM_CLOUDS = 25;
	
	private final Array<Cloud> clouds;
	
	public Clouds() {
		super(false,true);
		
		clouds = new Array<Cloud>();
	}
	
	@Override
	public void initialize() {
		clouds.clear();
		clouds.addAll(CloudFactory.generateClouds(NUM_CLOUDS));
	}
	
	@Override
	public void reset() {
		
	}

	@Override
	protected void updateObject(float delta) {
		
	}

	@Override
	protected void renderObject(Renderer renderer) {
		/*for (Cloud cloud: clouds) {
			//renderer.decalBatch.add(cloud.getDecal());
		}*/
	}

	@Override
	public void onWorldStateChanged(WorldState worldState) {
		/*switch (worldState) {
		case Initial:
			render = false;
			break;
		case Skydiving:
			render = true;
			break;
		default:
			render = false;
			break;
	}*/
	}

	public Array<Cloud> getClouds() {
		return clouds;
	}

	/*@Override
	protected void loadAssets() {
		// TODO Auto-generated method stub
		
	}*/

}
