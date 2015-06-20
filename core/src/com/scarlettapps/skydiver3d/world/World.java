// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.scarlettapps.skydiver3d.Skydiver3D;
import com.scarlettapps.skydiver3d.resources.AssetFactory.MusicType;
import com.scarlettapps.skydiver3d.resources.AssetFactory.SoundType;
import com.scarlettapps.skydiver3d.resources.MusicFactory;
import com.scarlettapps.skydiver3d.resources.SoundFactory;
import com.scarlettapps.skydiver3d.worldstate.CheckIntersectListener;
import com.scarlettapps.skydiver3d.worldstate.InputListener;
import com.scarlettapps.skydiver3d.worldstate.InputManager;
import com.scarlettapps.skydiver3d.worldstate.SkydiverControls;
import com.scarlettapps.skydiver3d.worldstate.Status;
import com.scarlettapps.skydiver3d.worldstate.StatusListener;
import com.scarlettapps.skydiver3d.worldstate.StatusManager;
import com.scarlettapps.skydiver3d.worldstate.StickyListener;
import com.scarlettapps.skydiver3d.worldstate.SwitchStateListener;
import com.scarlettapps.skydiver3d.worldstate.WorldState;

/**
 * The World is the representation of the game world of SkyDiving3D. It knows nothing about
 * how it will be displayed, nor does it know anything about how it will be controlled. It
 * only knows about the game objects and the current game state.
 */
public class World {
	
	private final Array<GameObject> objects;
	
	private Skydiver skydiver;
	private Target target;
	private Terrain terrain;
	private Collectibles collectibles;
	private Clouds clouds;
	private Sky sky;
	
	private Status status;
	
	public World(InputManager inputManager, StatusManager statusManager) {
		status = statusManager.getStatus();
		
		skydiver = new Skydiver(status);
		collectibles = new Collectibles();
		clouds = new Clouds();
		terrain = new Terrain();
		target = new Target();
		sky = new Sky(status);
		
		objects = new Array<GameObject>();
		objects.add(skydiver);
		objects.add(terrain);
		objects.add(collectibles);
		objects.add(clouds);
		objects.add(target);
		objects.add(sky);
		
		
		SkydiverControls skydiverControls = new SkydiverControls(this, status);
		inputManager.addListener(skydiverControls);
		statusManager.addListener(new CheckIntersectListener(this));
		statusManager.addListener(new SwitchStateListener(this));
	}
	
	
	
	public void initialize() {
		for (GameObject o: objects) {
			o.initialize();
		}
		
		for (GameObject o: objects) {
			o.onWorldStateChanged(WorldState.INITIAL);
		}
	}
	
	public void playWind() {
		MusicFactory music = MusicFactory.getInstance();
		music.play(MusicType.WIND);
	}
	
	public void update(float delta) {
		if (!status.isPaused()) {
			updatePositions(delta);
			MusicFactory music = MusicFactory.getInstance();
			music.setVolume(getWindVolume());
		}
	}
	
	private float getWindVolume() {
		return (0.2f-status.velocity().z/Skydiver.MAX_TERMINAL_SPEED)/1.2f;
	}
	
	private void updatePositions(float delta) {
		sky.update(delta);
		skydiver.update(delta);
		collectibles.update(delta);
	}

	public void reset() {
		if (Skydiver3D.DEV_MODE) {
			Gdx.app.log(Skydiver3D.LOG, "Resetting World");
		}
		
		for (GameObject o: objects) {
			o.reset();
		}
	}
	
	public void pause() {
		status.setPaused(true);
	}
	
	public void resume() {
		status.setPaused(false);
	}

	public boolean isPaused() {
		return status.isPaused();
	}
	
	public Terrain getTerrain() {
		return terrain;
	}
	
	public Skydiver getSkydiver() {
		return skydiver;
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
		SoundFactory sound = SoundFactory.getInstance();
		sound.play(SoundType.BELL);
	}

	public Array<GameObject> getObjects() {
		return objects;
	}

	public Sky getSky() {
	    return sky;
	}
	
}
