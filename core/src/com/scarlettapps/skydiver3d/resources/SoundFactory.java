package com.scarlettapps.skydiver3d.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.scarlettapps.skydiver3d.Skydiver3D;

public final class SoundFactory {
	
	private static SoundFactory instance;
	
	private SoundFactory() {
		
	}

	public void play(String fileName) {
		AssetFactory assetFactory = AssetFactory.getInstance();
		play(assetFactory.get(fileName, Sound.class));
	}
	
	public void play(Sound sound) {
		if (Skydiver3D.DEV_MODE) {
			Gdx.app.log(Skydiver3D.LOG, "Playing sound " + sound);
		}
		
		PreferenceFactory preferenceFactory = PreferenceFactory.getInstance();
		if (preferenceFactory.isSoundEnabled()) {
			sound.play(preferenceFactory.getVolume());
		}
	}
	
	public static SoundFactory getInstance() {
		if (instance == null) {
			instance = new SoundFactory();
		}
		return instance;
	}
}
