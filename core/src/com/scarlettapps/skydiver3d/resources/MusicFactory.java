package com.scarlettapps.skydiver3d.resources;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class MusicFactory {
	
	private static MusicFactory musicFactory;

	private Music current;
	//private Music ambient;
	
	private MusicFactory() {} //prevent this class from being instantiated without static method
	
	public void play(String fileName) {
		play(fileName, true);
	}
	
	public void play(String fileName, boolean isLooping) {
		play(AssetFactory.get(fileName, Music.class), isLooping);
	}
	
	public void play(Music music) {
		play(music, true);
	}
	
	public void play(Music music, boolean isLooping) {
		if (PreferenceFactory.isMusicEnabled()) {
			if (current != null) {
				if (current == music) {
					return;
				} else {
					stop();
				}
			}
			music.setLooping(isLooping);
			music.setVolume(PreferenceFactory.getVolume());
			music.play();
			current = music;
		}
	}
	
	public void pause() {
		if (current != null) {
			current.pause();
		}
	}
	
	public void resume() {
		if (current != null) {
			current.play();
		}
	}
	
	public void stop() {
		if (current != null) {
			current.stop();
			current = null;
		}
	}
	
	public void updateVolume(float volume) {
		if (current != null) {
			current.setVolume(volume);
		}
	}
	
	public static MusicFactory newInstance() {
		if (musicFactory != null) {
			throw new GdxRuntimeException("Only one MusicFactory can be instantiated.");
		}
		musicFactory = new MusicFactory();
		return musicFactory;
	}
	
	public static void fadeOut(Music music, float gamma) {
		music.setVolume(gamma);
	}

	public static void interpolate(Music first, Music second, float gamma) {
		first.setVolume(1-gamma);
		second.setVolume(gamma);
	}

	public static void stopSound() {
		musicFactory.stop();
	}
}
