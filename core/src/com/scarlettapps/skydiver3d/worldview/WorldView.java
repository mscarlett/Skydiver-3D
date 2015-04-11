// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.worldview;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.scarlettapps.skydiver3d.world.World;
import com.scarlettapps.skydiver3d.worldstate.StatusManager;
import com.scarlettapps.skydiver3d.worldview.ui.StatusView;

/**
 * The WorldView displays the World on the screen. It also allows
 * the player to control the game by listening to input and updating objects.
 */

public class WorldView {
	
	static final float CAM_OFFSET = 5f;
	
	private final World world;
	private final StatusManager statusManager;
	private Renderer renderer;	
	private StatusView statusView;
		
	public WorldView(World world, StatusManager statusManager) {
		this.world = world;
		this.statusManager = statusManager;  
		
		DefaultShader.defaultCullFace = 0;
		
		renderer = new Renderer(world);
		statusView = new StatusView(statusManager);
	}
	
	public void initialize() {
		statusView.initialize();
        renderer.initialize();
        renderer.switchState(statusManager, this);
	}
	
	public void update(float delta) {
		if (statusManager.switchState()) {
			renderer.switchState(statusManager, this);
		}
		renderer.update(delta);
		statusView.update(delta);
	}
	
	public void render(float delta) {
		renderer.render(delta);
		statusView.render(delta);
	}

	public void reset() {
		if (statusManager.jumpedOffAirplane()) { //XXX another hack
			throw new GdxRuntimeException("Need to call reset() on statusManager first.");
		}
		initialize();
	}

	public InputProcessor getInputProcessor() {
		return statusView.getInputProcessor();
	}
	
	World getWorld() {
		return world;
	}
	
	StatusManager getStatusManager() {
		return statusManager;
	}
	
	StatusView getStatusView() {
		return statusView;
	}
	
	Renderer getRenderer() {
		return renderer;
	}

}
