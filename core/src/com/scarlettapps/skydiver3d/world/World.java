// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.scarlettapps.skydiver3d.Skydiver3D;
import com.scarlettapps.skydiver3d.resources.AssetFactory;
import com.scarlettapps.skydiver3d.resources.MusicFactory;
import com.scarlettapps.skydiver3d.resources.SoundFactory;
import com.scarlettapps.skydiver3d.resources.AssetFactory.MusicType;
import com.scarlettapps.skydiver3d.resources.AssetFactory.SoundType;
import com.scarlettapps.skydiver3d.worldstate.Status;
import com.scarlettapps.skydiver3d.worldstate.StatusManager;

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
	private Plane plane;
	
	public World() {		
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
		for (GameObject o: objects) {
			o.initialize();
		}
	}
	
	public void playWind() {
		MusicFactory music = MusicFactory.getInstance();
		music.play(MusicType.WIND);
	}
	
	public void update(float delta) {
		if (!Status.getInstance().isPaused()) {
			updatePositions(delta);
			MusicFactory music = MusicFactory.getInstance();
			music.setVolume(getWindVolume());
		}
	}
	
	private float getWindVolume() {
		return (0.2f-Status.getInstance().velocity().z/Skydiver.MAX_TERMINAL_SPEED)/1.2f;
	}
	
	private void updatePositions(float delta) {
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
		Status.getInstance().setPaused(true);
	}
	
	public void resume() {
		Status.getInstance().setPaused(false);
	}

	public boolean isPaused() {
		return Status.getInstance().isPaused();
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
		SoundFactory sound = SoundFactory.getInstance();
		sound.play(SoundType.BELL);
	}

	public Array<GameObject> getObjects() {
		return objects;
	}
	
}
