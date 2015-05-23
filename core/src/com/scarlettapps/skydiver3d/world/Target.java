// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.scarlettapps.skydiver3d.worldstate.WorldState;
import com.scarlettapps.skydiver3d.worldview.Renderer;

public class Target extends GameObject {
	
	private Decal target;
	
	public Target() {
		super(false,true);
	}
	
	@Override
	public void initialize() { //TODO can this be drawn procedurally?
		target = Decal.newDecal(new TextureRegion(new Texture(Gdx.files.local("data/textures/target.gif"))));
		target.setPosition(0, 0, 0);
		target.setScale(1);
		target.lookAt(Vector3.Z, Vector3.Z);
		target.setBlending(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	@Override
	public void reset() {
		
	}
	
	public void render(DecalBatch decalBatch) {
		if (render) {
		    decalBatch.add(target);
		}
	}
	
	public int getPoints(float x, float y) {
		return 0;
	}

	@Override
	protected void updateObject(float delta) {
		// TODO Auto-generated method stub	
	}

	@Override
	protected void renderObject(Renderer renderer) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onWorldStateChanged(WorldState worldState) {
		// TODO Auto-generated method stub
	}
	
	public void setRender(boolean render) {
		this.render = render;
	}

}
