// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class PreferenceFactory {
	
	private static PreferenceFactory preferenceFactory;

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
	
	public boolean isSoundEnabledValue() {
		return preferences.getBoolean("Sound", true);
	}

	public float getVolumeValue() {
		return preferences.getFloat("Volume", 0.5f);
	}
	
	public static PreferenceFactory newInstance() {
		if (preferenceFactory != null) {
			throw new GdxRuntimeException("Only one PreferenceFactory can be instantiated.");
		}
		preferenceFactory = new PreferenceFactory();
		return preferenceFactory;
	}

	public boolean getMusicEnabled() {
		return preferences.getBoolean("Music", true);
	}

	public void setVolume(float value) {
		preferences.putFloat("Volume", value);
		preferences.flush();
	}
	
	public float getSensitivityValue() {
		return preferences.getFloat("Sensitivity", 1f);
	}

	public void setSensitivity(float value) {
		preferences.putFloat("Sensitivity", value);
	}
	
	public static boolean isMusicEnabled() {
		return preferenceFactory.getMusicEnabled();
	}

	public static boolean isSoundEnabled() {
		return preferenceFactory.isSoundEnabledValue();
	}

	public static float getVolume() {
		return preferenceFactory.getVolumeValue();
	}
	
	public static float getSensitivity() {
		return preferenceFactory.getSensitivityValue();
	}
}
