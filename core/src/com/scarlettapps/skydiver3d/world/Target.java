// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.world;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.scarlettapps.skydiver3d.resources.Graphics;
import com.scarlettapps.skydiver3d.world.gamestate.StatusManager.WorldState;
import com.scarlettapps.skydiver3d.worldview.Renderer;

public class Target extends GameObject {
	
	private ModelInstance instance;
	private BoundingBox bounds;
	
	private Environment environment;
	
	public Target() {
		super(false,true);
		
		Model model = Graphics.get("data/target_raft_5.g3db", Model.class);
		instance = new ModelInstance(model);
		instance.transform.setToTranslation(0,0,0);
		instance.transform.scale(10, 10, 10);
		instance.transform.rotate(Vector3.X, 90);
		bounds = new BoundingBox();
		instance.calculateBoundingBox(bounds);
		
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.7f, 0.7f, 0.7f, 1.0f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
	}
	
	public void render(ModelBatch modelBatch) {
		modelBatch.render(instance, environment);
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

}
