package com.scarlettapps.skydiver3d.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.scarlettapps.skydiver3d.Skydiver3D;

public final class MusicFactory {
	
	private static MusicFactory instance;

	private Music current;
	//private Music ambient;
	
	private MusicFactory() {} //prevent this class from being instantiated without static method
	
	public void play(String fileName) {
		play(fileName, true);
	}
	
	public void play(String fileName, boolean isLooping) {
		AssetFactory assetFactory = AssetFactory.getInstance();
		play(assetFactory.get(fileName, Music.class), isLooping);
	}
	
	public void play(Music music) {
		play(music, true);
	}
	
	public void play(Music music, boolean isLooping) {
		if (Skydiver3D.DEV_MODE) {
			Gdx.app.log(Skydiver3D.LOG, "Playing music " + music);
		}
		
		PreferenceFactory preferenceFactory = PreferenceFactory.getInstance();
		if (preferenceFactory.isMusicEnabled()) {
			if (current != null) {
				if (current == music) {
					return;
				} else {
					stop();
				}
			}
			music.setLooping(isLooping);
			music.setVolume(preferenceFactory.getVolume());
			music.play();
			current = music;
		}
	}
	
	public void pause() {
		if (current != null) {
			if (Skydiver3D.DEV_MODE) {
				Gdx.app.log(Skydiver3D.LOG, "Pausing music " + current);
			}
			current.pause();
		}
	}
	
	public void resume() {
		if (current != null) {
			if (Skydiver3D.DEV_MODE) {
				Gdx.app.log(Skydiver3D.LOG, "Resuming music " + current);
			}
			current.play();
		}
	}
	
	public void stop() {
		if (current != null) {
			if (Skydiver3D.DEV_MODE) {
				Gdx.app.log(Skydiver3D.LOG, "Stopping music " + current);
			}
			current.stop();
			current = null;
		}
	}
	
	public void updateVolume(float volume) {
		if (current != null) {
			current.setVolume(volume);
		}
	}
	
	public void fadeOut(Music music, float gamma) {
		music.setVolume(gamma);
	}

	public void interpolate(Music first, Music second, float gamma) {
		first.setVolume(1-gamma);
		second.setVolume(gamma);
	}
	
	public static MusicFactory getInstance() {
		if (instance == null) {
			instance = new MusicFactory();
		}
		return instance;
	}
}
