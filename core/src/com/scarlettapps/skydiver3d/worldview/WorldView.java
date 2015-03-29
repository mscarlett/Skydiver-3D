// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.worldview;

import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.SortedIntList.Node;
import com.scarlettapps.skydiver3d.DefaultScreen;
import com.scarlettapps.skydiver3d.Skydiver3D;
import com.scarlettapps.skydiver3d.world.Collectible;
import com.scarlettapps.skydiver3d.world.Collectibles;
import com.scarlettapps.skydiver3d.world.Plane;
import com.scarlettapps.skydiver3d.world.Skydiver;
import com.scarlettapps.skydiver3d.world.Target;
import com.scarlettapps.skydiver3d.world.Terrain;
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
	
	private PerspectiveCamera cam;
	private DecalBatch decalBatch;
	private ModelBatch modelBatch;
	private WorldViewController controller;	
	private StatusView statusView;
		
	public WorldView(World world, StatusManager statusManager) {
		this.world = world;
		this.statusManager = statusManager;  
		
		DefaultShader.defaultCullFace = 0;
	}
	
	public void initialize() {
		cam = new PerspectiveCamera(67, DefaultScreen.width(), DefaultScreen.height());
        Skydiver.cam = cam;
        
        decalBatch = new DecalBatch(new CameraGroupStrategy(cam, new Comparator<Decal>(){
			@Override
			public int compare(Decal decal1, Decal decal2) {
				return (int)Math.signum(decal1.getZ()-decal2.getZ());
			}
        }));
        
		modelBatch = new ModelBatch();
		
		statusView = new StatusView(statusManager);
		
        switchState();
	}
	
	public void update(float delta) {
		if (statusManager.switchState()) {
			switchState();
		}
		controller.update(delta);
		statusView.update(delta);
	}
	
	public void render(float delta) {
		controller.render(delta);
		statusView.render(delta);
	}
	
	
	void drawTerrain() {
		Terrain terrain = world.getTerrain();
		terrain.render(cam);
	}
	
	void drawTargetAndSkydiver() {
		modelBatch.begin(cam);
		Target target = world.getTarget();
		Skydiver skydiver = world.getSkydiver();
		target.render(modelBatch);
		skydiver.render(modelBatch);
		modelBatch.end();
	}
	
	void drawSkydiverAndPlane() {
		modelBatch.begin(cam);
		Skydiver skydiver = world.getSkydiver();
		Plane plane = world.getPlane();
		plane.render(modelBatch);
		skydiver.render(modelBatch);
		modelBatch.end();
	}
	
	void drawCollectibles() {
		Collectibles collectibles = world.getCollectibles();
		//Array<Cloud> clouds = world.getClouds();
		for (Node<Collectible> node: collectibles) {
			decalBatch.add(node.value.getDecal());
		}
		//for (Cloud c: clouds) {
			//decalBatch.add(c.getDecal());
		//}
		decalBatch.flush();
		
	}
	
	public void switchState() {
		String oldName;
		
		if (Skydiver3D.DEV_MODE) {
			oldName = controller == null ? null : controller.getClass().getSimpleName();
		}
		
		switch(statusManager.getState()) {
			case FINAL:
				controller = new FinalStateController(this);
				break;
			case INITIAL:
				controller = new InitialStateController(this);
				break;
			case LANDING:
				controller = new LandingStateController(this);
				break;
			case PARACHUTING:
				controller = new ParachutingStateController(this);
				break;
			case SKYDIVING:
				controller = new SkydivingStateController(this);
				break;
			default:
				throw new GdxRuntimeException("Invalid World State");
		}
		
		if (Skydiver3D.DEV_MODE) {
			Gdx.app.log(Skydiver3D.LOG, "Switching controller from " + oldName + " to " + controller.getClass().getSimpleName());
		}
		
		controller.initialize();
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
	
	PerspectiveCamera getCam() {
		return cam;
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

}
