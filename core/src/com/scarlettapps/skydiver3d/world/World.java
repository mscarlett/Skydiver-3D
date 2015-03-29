// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.world;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.scarlettapps.skydiver3d.resources.AssetFactory;
import com.scarlettapps.skydiver3d.resources.AssetFactory.MusicType;
import com.scarlettapps.skydiver3d.resources.AssetFactory.SoundType;
import com.scarlettapps.skydiver3d.worldstate.StatusManager;

/**
 * The World is the representation of the game world of SkyDiving3D. It knows nothing about
 * how it will be displayed, nor does it know anything about how it will be controlled. It
 * only knows about the game objects and the current game state.
 */
public class World {
	
	private final StatusManager statusManager;
	private final Array<GameObject> objects;
	
	private Skydiver skydiver;
	private Target target;
	private Terrain terrain;
	private Collectibles collectibles;
	private Clouds clouds;
	private Sound bell;
	private Music wind;
	private Plane plane;
	
	public World(StatusManager statusManager) {
		this.statusManager = statusManager;
		
		skydiver = new Skydiver();
		plane = new Plane();
		collectibles = new Collectibles();
		clouds = new Clouds();
		terrain = new Terrain();
		target = new Target();
		
		objects = new Array<GameObject>();
		objects.add(skydiver);
		objects.add(terrain);
		objects.add(collectibles);
		objects.add(clouds);
		objects.add(plane);
		objects.add(target);
	}
	
	public void initialize() {
		AssetFactory assetFactory = AssetFactory.getInstance();
		
		bell = assetFactory.get(SoundType.BELL, Sound.class);
		wind = assetFactory.get(MusicType.WIND, Music.class);
		wind.setLooping(true);
		
		for (GameObject o: objects) {
			o.initialize();
		}
		
		statusManager.syncSkydiver(skydiver);
	}
	
	public void playWind() {
		wind.play();
	}
	
	public void update(float delta) {
		if (!statusManager.isPaused()) {
			updatePositions(delta);
			wind.setVolume(getWindVolume());
		}
		
		statusManager.update(delta, this);
	}
	
	private float getWindVolume() {
		return (0.2f-statusManager.velocity().z/Skydiver.MAX_TERMINAL_SPEED)/1.2f;
	}
	
	private void updatePositions(float delta) {
		skydiver.update(delta);
		collectibles.update(delta);
	}

	public void reset() {
		initialize();
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

	public Array<GameObject> getObjects() {
		return objects;
	}
	
}
