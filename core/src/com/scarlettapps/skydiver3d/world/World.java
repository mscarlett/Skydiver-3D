// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.scarlettapps.skydiver3d.resources.Graphics;
import com.scarlettapps.skydiver3d.world.gamestate.GameController;
import com.scarlettapps.skydiver3d.world.gamestate.InputListener;
import com.scarlettapps.skydiver3d.world.gamestate.InputManager;
import com.scarlettapps.skydiver3d.world.gamestate.StatusManager;

/**
 * The World is the representation of the game world of SkyDiving3D. It knows nothing about
 * how it will be displayed, nor does it know anything about how it will be controlled. It
 * only knows about the game objects and the current game state.
 */
public class World {
	
	private final InputManager inputManager;
	private final StatusManager statusManager;
	private final Array<GameObject> objects;
	
	private final Skydiver skydiver;
	private final Target target;
	private final Terrain terrain;
	private final Collectibles collectibles;
	private final Clouds clouds;
	private final Sound bell;
	private final Music wind;
	private final Plane plane;
	
	public World(InputManager inputManager, StatusManager statusManager) {
		this.inputManager = inputManager;
		this.statusManager = statusManager;
		
		skydiver = new Skydiver();
		plane = new Plane();
		collectibles = new Collectibles();
		clouds = new Clouds();
		terrain = new Terrain();
		target = new Target();
		
		bell = Graphics.get("data/bell.ogg", Sound.class);
		wind = Graphics.get("data/wind.ogg", Music.class);
		wind.setLooping(true);
		
		inputManager.addListener(new SkydiverControls());
		statusManager.syncSkydiver(skydiver);
		
		objects = new Array<GameObject>();
		objects.add(skydiver);
		objects.add(terrain);
		objects.add(collectibles);
		objects.add(clouds);
		objects.add(plane);
		objects.add(target);
		
		reset();
	}
	
	public void playWind() {
		wind.play();
	}
	
	public void update(float delta) {
		inputManager.updateListeners(delta);
		
		if (!statusManager.isPaused()) {
			updatePositions(delta);
			wind.setVolume((0.2f-statusManager.velocity().z/Skydiver.MAX_TERMINAL_SPEED)/1.2f);
		}
		
		statusManager.update(delta, this);
	}
	
	private void updatePositions(float delta) {
		skydiver.update(delta);
		collectibles.update(delta);
	}

	public void reset() {
		statusManager.reset();
		skydiver.getPosition().z = Skydiver.STARTING_HEIGHT;
	}
	
	public void pause() {
		statusManager.setPaused(true);
	}
	
	public void resume() {
		statusManager.setPaused(false);
	}

	public boolean isPaused() {
		return statusManager.isPaused();
	}
	
	public Terrain getTerrain() {
		return terrain;
	}
	
	public Skydiver getSkydiver() {
		return skydiver;
	}
	
	public Plane getPlane() {
		return plane;
	}
	
	public Target getTarget() {
		return target;
	}

	public Collectibles getCollectibles() {
		return collectibles;
	}
	
	public Array<Cloud> getClouds() {
		return clouds.getClouds();
	}
	
	public void playBell() {
		bell.play();
	}
	
	private class SkydiverControls implements InputListener {
			
			private static final float PARACHUTING_TIME_LIMIT = 8f;
			private float elapsedTime = 0f;
			
			@Override
			public void update(GameController gameController, float delta) {
							
				switch (World.this.statusManager.worldState()) {
					case INITIAL:
						Skydiver skydiver = getSkydiver();
						if (Gdx.input.justTouched() && !skydiver.jumpedOffAirplane) {
							skydiver.jumpOffAirplane();
						}
						break;
					case SKYDIVING:
						skydiver = getSkydiver();
						if (gameController.getFaster()) {
							skydiver.addToVelocity(0, 0, -25 * delta);
						}
						skydiver.addToVelocity(delta*gameController.getAx(), delta*gameController.getAy(), 0);
						skydiver.skydiverAngle.x += 10 * delta*gameController.getAx();
						break;
					case PARACHUTING:
						if (!statusManager.justOpenedParachute()) {
							elapsedTime += delta;
							statusManager.setJustOpenedParachute(Gdx.input.justTouched() || elapsedTime > PARACHUTING_TIME_LIMIT);
						}
						skydiver = getSkydiver();
						statusManager.velocity().x = 0;
						statusManager.velocity().y = 0;
						statusManager.position().x = Math.signum(statusManager.position().x)*(Math.abs(statusManager.position().x)-0.5f*delta);
						statusManager.position().y = Math.signum(statusManager.position().y)*(Math.abs(statusManager.position().y)-0.5f*delta);
						break;
					case LANDING:
						skydiver = getSkydiver();
						float accuracy = statusManager.getAccuracy();
						float error = 3-2*accuracy;
						skydiver.addToVelocity(15*gameController.getAx()*error*delta,15*gameController.getAy()*error*delta,0);
						//statusManager.velocity().z = -30f*(1.7f-accuracy);
						statusManager.velocity().z = -30f;
						statusManager.velocity().x += 3*(Math.signum(statusManager.velocity().x) == 0 ? Math.random() : Math.signum(statusManager.velocity().x))*Math.abs(Math.random()*delta);
						statusManager.velocity().y += 3*(Math.signum(statusManager.velocity().y) == 0 ? Math.random() : Math.signum(statusManager.velocity().y))*Math.abs(Math.random()*delta);
						skydiver.landing = true;
						break;
					case FINAL:
						skydiver = getSkydiver();
						statusManager.velocity().set(0,0,0);
						skydiver.finalState = true;
						break;
				}
			}
	}

	public Array<GameObject> getObjects() {
		return objects;
	}
	
}
