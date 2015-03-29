// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.world;

import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.scarlettapps.skydiver3d.resources.AssetFactory;
import com.scarlettapps.skydiver3d.resources.AssetFactory.ModelType;
import com.scarlettapps.skydiver3d.worldstate.WorldState;
import com.scarlettapps.skydiver3d.worldview.Renderer;

public class Target extends GameObject {
	
	private ModelInstance instance;
	private BoundingBox bounds;
	
	private Environment environment;
	private Attribute attribute;
	
	public Target() {
		super(false,true);
	}
	
	@Override
	public void initialize() {
		Model model = AssetFactory.getInstance().get(ModelType.TARGET, Model.class);
		instance = new ModelInstance(model);
		instance.transform.setToTranslation(0,0,0);
		instance.transform.scale(10, 10, 10);
		instance.transform.rotate(Vector3.X, 90);
		bounds = new BoundingBox();
		instance.calculateBoundingBox(bounds);
		
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.7f, 0.7f, 0.7f, 1.0f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        
        attribute = new BlendingAttribute(0.5f);
        instance.materials.get(0).set(attribute);
	}
	
	public void render(ModelBatch modelBatch) {
		if (render) {
		    modelBatch.render(instance, environment);
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
