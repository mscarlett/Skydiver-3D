// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.world;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Star extends Collectible {
	
	private static final String STAR_TEXTURE = "data/star-big_2.png";
	private static final int POINTS = 1000;

	public Star(float width, float height, TextureRegion textureRegion, float x, float y, float z) {
		super(width, height, textureRegion, x, y, z);
		// TODO Auto-generated constructor stub
	}

}
