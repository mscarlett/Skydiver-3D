// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.world;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.scarlettapps.skydiver3d.resources.AssetFactory;
import com.scarlettapps.skydiver3d.resources.AssetFactory.ModelType;
import com.scarlettapps.skydiver3d.world.utils.AnimationController;
import com.scarlettapps.skydiver3d.worldstate.WorldState;
import com.scarlettapps.skydiver3d.worldview.Renderer;

public class Plane extends GameObject {
	
	private ModelInstance instance;
	private Environment environment;
	private AnimationController controller;
	
	private float animationTime;
	private float pose;
	private float rps = 36f;
	
	public Plane() {
		super(false,true);
	}
	
	@Override
	public void initialize() {
		String filename = ModelType.PLANE;
		Model model = AssetFactory.getInstance().get(filename, Model.class);
		instance = new ModelInstance(model);
		instance.materials.get(0).set(
				new BlendingAttribute(GL20.GL_SRC_ALPHA,
						GL20.GL_ONE_MINUS_SRC_ALPHA, 1f));
		
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.1f, 0.1f, 0.1f, 1.0f));
        environment.add(new DirectionalLight().set(0.2f, 0.2f, 0.2f, -1f, -0.8f, -0.2f));
        environment.add(new DirectionalLight().set(0.2f, 0.2f, 0.2f, -1f, 0.8f, -0.2f));
        
        controller = new AnimationController(instance);
        controller.animate(instance.animations.get(0).id, -1, 1f, null,0.2f);
        
		animationTime = instance.animations.get(0).duration;
		
		setToTranslation(3.1f, -0.85f, Skydiver.STARTING_HEIGHT+1.2f);
		rotate(Vector3.X, 90);
		rotate(Vector3.Y, 90);
	}
	
	@Override
	public void updateObject(float delta) {
		pose = (pose + delta*rps)%1f;
		controller.update(delta,pose*animationTime);
	}
	
	public void render(ModelBatch modelBatch) {
		modelBatch.render(instance, environment);
	}
	
	public void setToTranslation(float x, float y, float z) {
		instance.transform.setToTranslation(x, y, z);
	}
	
	public void rotate(Vector3 axis, float angle) {
		instance.transform.rotate(axis, angle);
	}
	
	public void scl(float scale) {
		instance.transform.scl(scale);
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
