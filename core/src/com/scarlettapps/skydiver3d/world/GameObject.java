// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.world;

import com.scarlettapps.skydiver3d.worldstate.Status;
import com.scarlettapps.skydiver3d.worldstate.WorldState;
import com.scarlettapps.skydiver3d.worldview.Renderer;

public abstract class GameObject {

	protected boolean update;
	protected boolean render;
	
	public GameObject(boolean update, boolean render) {
		this.update = update;
		this.render = render;
	}
	
	public final void update(float delta) {
		if (update) updateObject(delta);
	}
	
	protected abstract void updateObject(float delta);
	
	public final void render(Renderer renderer) {
		if (render) renderObject(renderer);
	}
	
	protected abstract void renderObject(Renderer renderer);
	
	public abstract void onWorldStateChanged(WorldState worldState);
	
	public abstract void initialize();
	
	public abstract void reset();
}
