// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.world;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;

public class Collectible {
	
	private Decal decal;
	boolean collected;
	
	public Collectible(TextureRegion textureRegion, float x, float y, float z) {
		decal = Decal.newDecal(textureRegion);
		decal.setPosition(x, y, z);
		collected = false;
	}

	public Collectible(float width, float height, TextureRegion textureRegion,
			float x, float y, float z) {
		decal = Decal.newDecal(width,height,textureRegion, true);
		decal.setPosition(x, y, z);
	}

	public Decal getDecal() {
		return decal;
	}
	
	public void rotate(float delta) {
		decal.rotateZ(delta);
	}

}
