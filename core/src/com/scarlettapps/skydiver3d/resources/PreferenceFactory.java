// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public final class PreferenceFactory {
	
	private static PreferenceFactory instance;

	private final Preferences preferences;
	
	private PreferenceFactory() {
		preferences = Gdx.app.getPreferences("Preferences");
	}

	public void setSoundEnabled(boolean enabled) {
		preferences.putBoolean("Sound", enabled);
		preferences.flush();
		
	}

	public void setMusicEnabled(boolean enabled) {
		preferences.putBoolean("Music", enabled);
		preferences.flush();
	}
	
	public boolean isSoundEnabled() {
		return preferences.getBoolean("Sound", true);
	}

	public float getVolume() {
		return preferences.getFloat("Volume", 0.5f);
	}
	
	public boolean isMusicEnabled() {
		return preferences.getBoolean("Music", true);
	}

	public void setVolume(float value) {
		preferences.putFloat("Volume", value);
		preferences.flush();
	}
	
	public float getSensitivity() {
		return preferences.getFloat("Sensitivity", 1f);
	}

	public void setSensitivity(float value) {
		preferences.putFloat("Sensitivity", value);
		preferences.flush();
	}
	
	public static PreferenceFactory getInstance() {
		if (instance == null) {
			instance = new PreferenceFactory();
		}
		return instance;
	}
}
