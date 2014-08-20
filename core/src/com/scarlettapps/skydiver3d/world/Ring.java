// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.scarlettapps.skydiver3d.resources.AssetFactory.TextureType;

public class Ring extends Collectible { //TODO How do I pass the texture to an instance of this class?
	
	private static final String RING_TEXTURE_FILE = TextureType.RING;
	private static final TextureRegion RING_TEXTURE = new TextureRegion(new Texture(RING_TEXTURE_FILE));
	private static final int POINTS = 500;

	public Ring(float width, float height, float x, float y, float z) {
		super(width, height, RING_TEXTURE, x, y, z);
	}
	
	@Override
	public int getPoints() {
		return POINTS;
	}

}
