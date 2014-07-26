// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.world;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Ring extends Collectible { //TODO How do I pass the texture to an instance of this class?
	
	private static final String RING_TEXTURE = "data/ring-big_2.png";
	private static final int POINTS = 1000;

	public Ring(float width, float height, TextureRegion textureRegion, float x, float y, float z) {
		super(width, height, textureRegion, x, y, z);
		// TODO Auto-generated constructor stub
	}
	
	public static int getPoints() {
		return POINTS;
	}

}
