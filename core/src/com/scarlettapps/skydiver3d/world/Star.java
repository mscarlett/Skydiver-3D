// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.scarlettapps.skydiver3d.resources.AssetFactory;
import com.scarlettapps.skydiver3d.resources.AssetFactory.TextureType;

public class Star extends Collectible {
	
	private static TextureRegion textureRegion = null;
	private static final int POINTS = 1000;

	public Star(float width, float height, float x, float y, float z) {
		super(width, height, getTextureRegion(), x, y, z);
	}
	
	private static TextureRegion getTextureRegion() {
		if (textureRegion == null) {
			textureRegion = new TextureRegion(AssetFactory.getInstance().get(TextureType.STAR, Texture.class));
		}
		return textureRegion;
	}

	@Override
	public int getPoints() {
		return POINTS;
	}

}
