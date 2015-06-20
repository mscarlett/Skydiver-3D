// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.world;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.MathUtils;
import com.scarlettapps.skydiver3d.world.utils.PerlinNoise;

public class Cloud {	
	private static final int length = 256;
	private static final int width = 256;
	private static final Texture texture = genTexture();
	
	private final Decal decal;
	
	public Cloud(float x, float y, float z) {
		float length = MathUtils.random(2000,5000);
		decal = Decal.newDecal(length,length*(MathUtils.random()+0.5f),new TextureRegion(texture), true);
		
		decal.setPosition(x, y, z);
		decal.setBlending(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}

	private static Texture genTexture() {
		Pixmap pixmap = new Pixmap(length, width, Format.RGBA8888);
		pixmap.setColor(1f, 1f, 1f, 0f);
		pixmap.fill();
		PerlinNoise p = new PerlinNoise(MathUtils.random(1,1000));
		for (int i=0; i<length; i++) {
			for (int j=0; j<width; j++) {
				float dx = ((float)i-length/2)/length;
				float dy = ((float)j-width/2)/width;
				float noiseFactor = Math.min(0.5f+1.5f*p.turbulence2(i * 0.00273f,j * 0.00273f, 256f),0);
				float distanceFactor = Math.max(0.5f-(float)Math.sqrt(dx*dx+dy*dy),0);
				float a = MathUtils.clamp(((1f/8f*noiseFactor+distanceFactor)*distanceFactor),0,1);
				float l = MathUtils.clamp(distanceFactor+0.8f, 0, 1);
				pixmap.setColor(l, l, l, a);
				pixmap.drawPixel(i, j);
			}
		}
		Texture texture = new Texture(pixmap);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		return texture;
	}

	public Decal getDecal() {
		return decal;
	}

}
