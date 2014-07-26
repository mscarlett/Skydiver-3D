// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.worldview;

import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.SortedIntList.Node;
import com.scarlettapps.skydiver3d.DefaultScreen;
import com.scarlettapps.skydiver3d.SkyDiver3D;
import com.scarlettapps.skydiver3d.world.Cloud;
import com.scarlettapps.skydiver3d.world.Collectible;
import com.scarlettapps.skydiver3d.world.Collectibles;
import com.scarlettapps.skydiver3d.world.Plane;
import com.scarlettapps.skydiver3d.world.Skydiver;
import com.scarlettapps.skydiver3d.world.Target;
import com.scarlettapps.skydiver3d.world.Terrain;
import com.scarlettapps.skydiver3d.world.World;
import com.scarlettapps.skydiver3d.world.gamestate.StatusManager;
import com.scarlettapps.skydiver3d.worldview.ui.AccuracyMeter;

/**
 * The WorldView displays the World on the screen. It also allows
 * the player to control the game by listening to input and updating objects.
 */

public class WorldView {
	
	private static final float CAM_OFFSET = 5f;
	
	private final World world;
	private final StatusManager statusManager;
	private final PerspectiveCamera cam;
	private final DecalBatch decalBatch;
	private final ModelBatch modelBatch;
	private WorldViewController controller;	
	private StatusView statusView;
		
	public WorldView(World world, StatusManager statusManager) { //create fade effect for decals
		this.world = world;
		this.statusManager = statusManager;
        
        cam = new PerspectiveCamera(67,DefaultScreen.width(),DefaultScreen.height());
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
	}
	
	public void render(float delta) {
		controller.render(delta);
	}
	
	
	private void drawTerrain() {
		Terrain terrain = world.getTerrain();
		Matrix4 mat = cam.combined;
		terrain.render(mat);
	}
	
	private void drawTargetAndSkydiver() {
		modelBatch.begin(cam);
		Target target = world.getTarget();
		Skydiver skydiver = world.getSkydiver();
		target.render(modelBatch);
		skydiver.render(modelBatch);
		modelBatch.end();
	}
	
	private void drawSkydiverAndPlane() {
		modelBatch.begin(cam);
		Skydiver skydiver = world.getSkydiver();
		Plane plane = world.getPlane();
		plane.render(modelBatch);
		skydiver.render(modelBatch);
		modelBatch.end();
	}
	
	private void drawCollectibles() {
		Collectibles collectibles = world.getCollectibles();
		Array<Cloud> clouds = world.getClouds();
		for (Node<Collectible> node: collectibles) {
			decalBatch.add(node.value.getDecal());
		}
		for (Cloud c: clouds) {
			decalBatch.add(c.getDecal());
		}
		decalBatch.flush();
		
	}
	
	public void switchState() {
		String oldName;
		
		if (SkyDiver3D.DEV_MODE) {
			oldName = controller == null ? null : controller.getClass().getSimpleName();
		}
		
		switch(statusManager.getState()) {
			case FINAL:
				controller = new FinalStateController();
				break;
			case INITIAL:
				controller = new InitialStateController();
				break;
			case LANDING:
				controller = new LandingStateController();
				break;
			case PARACHUTING:
				controller = new ParachutingStateController();
				break;
			case SKYDIVING:
				controller = new SkydivingStateController();
				break;
			default:
				throw new GdxRuntimeException("Invalid World State");
		}
		
		if (SkyDiver3D.DEV_MODE) {
			Gdx.app.log(SkyDiver3D.LOG, "Switching controller from " + oldName + " to " + controller.getClass().getSimpleName());
		}
		
		controller.initialize();
	}
	
	private static interface WorldViewController {
		public void initialize();
		public void update(float delta);
		public void render(float delta);
	}
	
	private class SkydivingStateController implements WorldViewController {
		
		private static final int COLLECTIBLES_OFFSET = 300;
		
		public void initialize() {
			cam.position.set(0, 0.5f,statusManager.position().z + CAM_OFFSET);
	        cam.direction.set(0,0,-1);
	        cam.update();
	        statusManager.position().x = (Skydiver.MIN_X+Skydiver.MAX_X)/2;
	        statusManager.position().y = (Skydiver.MIN_Y+Skydiver.MAX_Y)/2;
		}
		
		@Override
		public void update(float delta) {
			cam.position.z = statusManager.position().z + CAM_OFFSET;
			cam.up.set(Vector3.Y);
			cam.update();
			Collectibles collectibles = world.getCollectibles();
			collectibles.setToRender(cam.position.z, COLLECTIBLES_OFFSET);
			statusManager.addToSkydivingTime(delta);
		}

		@Override
		public void render(float delta) {
			renderSkydiving(delta);
		}
		
		private void renderSkydiving(float delta) {
			drawTerrain();
			drawTargetAndSkydiver();
			drawCollectibles();
			
			statusView.drawCollected(cam, delta);
			
			statusView.drawHud();
		}
		
	}
	
	private class ParachutingStateController implements WorldViewController {
		
		@Override
		public void update(float delta) {
			Skydiver skydiver = world.getSkydiver();
			AccuracyMeter accuracyMeter = statusView.getAccuracyMeter();
			boolean touched = statusManager.justOpenedParachute();
			if (touched) {
				accuracyMeter.stop();
				statusManager.setAccuracy(accuracyMeter.getAccuracy());
			}
			if (touched) {
				cam.position.x = statusManager.position().x + 0.5f;
				cam.position.y = statusManager.position().y;
				cam.position.z = statusManager.position().z + CAM_OFFSET;
				cam.direction.set(0,0,-1);
				cam.up.set(Vector3.Y);
				
			} else {
				cam.position.x = statusManager.position().x;
				cam.position.y = statusManager.position().y+4f;
				cam.position.z = statusManager.position().z-2f;
				cam.lookAt(statusManager.position().x, statusManager.position().y, statusManager.position().z+2f);
				cam.up.set(Vector3.Z);
			}
			cam.update();
			accuracyMeter.update(delta);
			skydiver.parachuting = true;
		}
		
		
		private String successString;

		@Override
		public void render(float delta) {
			drawTerrain();
			drawTargetAndSkydiver();
			statusView.drawParachuteCaption(world.getSkydiver(), delta);
			statusView.drawHud();
		}

		@Override
		public void initialize() {
			cam.near = 1f;
		}
	}
	
	private class LandingStateController implements WorldViewController {
		
		@Override
		public void update(float delta) {
			cam.position.x = statusManager.position().x + 0.5f;
			cam.position.y = statusManager.position().y;
			cam.position.z = statusManager.position().z + CAM_OFFSET;
			cam.update();
		}

		@Override
		public void render(float delta) {
			drawTerrain();
			drawTargetAndSkydiver();
			statusView.drawHud();
		}

		@Override
		public void initialize() {
			
		}
		
	}
	
	private class FinalStateController implements WorldViewController {

		@Override
		public void update(float delta) {
			
		}

		@Override
		public void render(float delta) {
			drawTerrain();
			drawTargetAndSkydiver();
		}

		@Override
		public void initialize() {
			statusManager.calculateTimeBonus();
			statusManager.calculateLandingBonus();
		}
		
	}
	
	private class InitialStateController implements WorldViewController {

		float dx = 10.245517f, dy = 3.8138173f, dz = -1.71019554f;
		float totalTime = 0;
		
		Color white = Color.WHITE.cpy();
		Color fontColor = white.cpy();
		Color tmp = new Color();
		
		float elapsedTime = 0.5f;
		
		Vector3 camOffset = new Vector3(-0.3f*CAM_OFFSET+dx,-0.1f*CAM_OFFSET+dy,Skydiver.STARTING_HEIGHT+0.4f*CAM_OFFSET+dz);
		Vector3 tmp2 = new Vector3();
		
		
		@Override
		public void update(float delta) {
			Skydiver skydiver = world.getSkydiver();

			if (skydiver.jumpedOffAirplane) {
				fontColor.set(Color.WHITE);
				totalTime += delta;
				dz -= Math.signum(dz)*delta*0.1f;
				dy -= Math.signum(dy)*delta*0.1f;
				dx -= Math.signum(dx)*delta*0.1f;
				camOffset.set(-0.3f*CAM_OFFSET+dx,-0.1f*CAM_OFFSET+dy,0.4f*CAM_OFFSET+dz+skydiver.getPositionZ());
				tmp2.set(camOffset).sub(statusManager.position());
				tmp2.scl(totalTime/5f);
				camOffset.sub(tmp2);
		        cam.position.set(camOffset);
		        cam.lookAt(skydiver.getPositionX(),skydiver.getPositionY(),skydiver.getPositionZ()+0.2f*CAM_OFFSET);
		        cam.up.set(Vector3.Z);
			} else {
		        cam.position.set(camOffset);
		        cam.lookAt(statusManager.position().x,statusManager.position().y,statusManager.position().z+0.2f*CAM_OFFSET);
		        elapsedTime = (elapsedTime + 0.1f*delta) % 1f;
				tmp.set(white);
				fontColor.set(white.lerp(Color.RED, 0.5f*Math.abs(elapsedTime-0.5f) % 1f));
				statusView.font.setColor(fontColor);
				white.set(tmp);
			}
			cam.update();
		}
		
		@Override
		public void render(float delta) {			
			drawSkydiverAndPlane();
			statusView.drawJumpOffPlane(world.getSkydiver());
		}

		@Override
		public void initialize() {
	        cam.direction.set(0,0,-1);
	        cam.up.set(Vector3.Z);
	        cam.near = 1f;
	        cam.far = 9000f;
	        cam.update();
		}
		
	}

	public void reset() {
		if (statusManager.jumpedOffAirplane()) {
			throw new GdxRuntimeException("Need to call reset() on statusManager first.");
		}
		switchState();
	}

	public void renderScore() {
		statusView.renderScore();
	}

}
