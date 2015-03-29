package com.scarlettapps.skydiver3d.world;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class CloudFactory {

	public static Array<Cloud> generateClouds(int numClouds) {
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
