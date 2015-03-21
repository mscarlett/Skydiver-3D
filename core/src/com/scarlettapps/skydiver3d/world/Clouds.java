// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.world;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.scarlettapps.skydiver3d.worldstate.WorldState;
import com.scarlettapps.skydiver3d.worldview.Renderer;

public class Clouds extends GameObject {
	
	private static final int NUM_CLOUDS = 25;
	
	private final Array<Cloud> clouds;
	
	public Clouds() {
		super(false,true);
		
		clouds = CloudFactory.generateClouds(NUM_CLOUDS);
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
	
	protected static class CloudFactory {
		
		private static Array<Cloud> generateClouds(int numClouds) {
			Array<Cloud> clouds = new Array<Cloud>();

			for (int i = 0; i < numClouds; i++) {
				float x = randX();
				float y = randY();
				float z = randZ();
				clouds.add(new Cloud(x, y, z));
			}
			
			return clouds;
		}
		
		private static float randX() {
			return (2*MathUtils.random(0,1)-1)*(100+MathUtils.random(0,1500));
		}

		private static float randY() {
			return (2*MathUtils.random(0,1)-1)*(100+MathUtils.random(0,1500));
		}

		private static float randZ() {
			return MathUtils.random(1000,Skydiver.STARTING_HEIGHT);
		}
	}

	public Array<Cloud> getClouds() {
		return clouds;
	}

	/*@Override
	protected void loadAssets() {
		// TODO Auto-generated method stub
		
	}*/

}
