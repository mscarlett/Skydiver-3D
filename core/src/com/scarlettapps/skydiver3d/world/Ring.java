// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.scarlettapps.skydiver3d.resources.AssetFactory;
import com.scarlettapps.skydiver3d.resources.AssetFactory.TextureType;

public class Ring extends Collectible { //TODO How do I pass the texture to an instance of this class?
	
	private static final int POINTS = 500;
	private static TextureRegion textureRegion = null;

	public Ring(float width, float height, float x, float y, float z) {
		super(width, height, getTextureRegion(), x, y, z);
	}
	
	@Override
	public int getPoints() {
		return POINTS;
	}
	
	private static TextureRegion getTextureRegion() {
		if (textureRegion == null) {
			textureRegion = new TextureRegion(AssetFactory.getInstance().get(TextureType.RING, Texture.class));
		}
		return textureRegion;
	}

}
