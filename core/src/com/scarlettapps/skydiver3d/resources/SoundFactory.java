package com.scarlettapps.skydiver3d.resources;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class SoundFactory {
	
	private static SoundFactory soundFactory;
	
	private SoundFactory() {
		
	}

	public void play(String fileName) {
		play(AssetFactory.get(fileName, Sound.class));
	}
	
	public void play(Sound sound) {
		if (PreferenceFactory.isSoundEnabled()) {
			sound.play(PreferenceFactory.getVolume());
		}
	}
	
	public static void playSound(String fileName) {
		soundFactory.play(fileName);
	}
	
	public static SoundFactory newInstance() {
		if (soundFactory != null) {
			throw new GdxRuntimeException("Only one SoundFactory can be instantiated.");
		}
		soundFactory = new SoundFactory();
		return soundFactory;
	}
}
