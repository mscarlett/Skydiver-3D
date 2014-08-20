// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.scarlettapps.skydiver3d.resources.AssetFactory.TextureType;

public class Star extends Collectible {
	
	private static final String STAR_TEXTURE_FILE = TextureType.STAR;
	private static final TextureRegion STAR_TEXTURE = new TextureRegion(new Texture(STAR_TEXTURE_FILE));
	private static final int POINTS = 1000;

	public Star(float width, float height, float x, float y, float z) {
		super(width, height, STAR_TEXTURE, x, y, z);
	}
	
	@Override
	public int getPoints() {
		return POINTS;
	}

}
