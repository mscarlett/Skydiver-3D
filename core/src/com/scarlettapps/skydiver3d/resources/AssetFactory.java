// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.resources;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class AssetFactory {
	
	public static class TextureType {
		public static final String TITLE = "data/textures/title.png";
		public static final String RING = "data/textures/ring-big_2.png";
		public static final String STAR = "data/textures/star-big_2.png";
		public static final String SLIDER = "data/textures/slider2.png";
		public static final String SLIDERBAR = "data/textures/sliderbar.png";
		public static final String PHONE_ROTATE = "data/textures/phone_rotate.png";
		public static final String DARTS = "data/textures/darts.png";
		public static final String EMPTY_STAR = "data/textures/emptystar.png";
		public static final String GOLD_STAR = "data/textures/goldstar.png";
		public static final String PAUSE = "data/textures/pause.png";
		public static final String WATER_TERRAIN = "data/textures/lightwater.jpg";
		public static final String SAND_TERRAIN = "data/textures/sandtile.jpg";
		public static final String FOLIAGE_TERRAIN = "data/textures/foliage_tiled.png";
		public static final String GRASS_TERRAIN = "data/textures/grasstile.jpg";
		public static final String LIGHTNING = "data/textures/lightning.png";
	}
	
	public static class SoundType {
		public static final String BELL = "data/sounds/bell.ogg";
		public static final String CLICK = "data/sounds/click.ogg";
		public static final String LAUGH = "data/sounds/laugh.ogg";
	}
	
	public static class MusicType {
		public static final String WIND = "data/music/wind.ogg";
		public static final String MAIN_MENU = "data/music/local_forecast.ogg";
		public static final String SKYDIVING = "data/music/disco_house.ogg";
	}
	
	public static class FontType {
		public static final String TUFFY = "data/fonts/tuffy.ttf";
	}
	
	public static class ModelType {
		public static final String PLANE = "data/models/plane_new_6.g3db";
		public static final String SKYDIVER = "data/models/human_with_parachute_and_backpack_and_helmet_a8.g3db";
		public static final String TARGET = "data/models/target_raft_5.g3db";
	}
	
	private static AssetManager assets;
	
	public static <T> T get(String fileName, Class<T> type) {
		return assets.get(fileName, type);
	}

	public static void update(int millis) {
		assets.update(millis);
	}

	public static boolean isLoaded(String fileName, Class<?> type) {
		return assets.isLoaded(fileName, type);
	}
	
	public static AssetManager getAssets() {
		return assets;
	}
	
	public static float getProgress() {
		return assets.getProgress();
	}
	
	public static void dispose() {
		assets.dispose();
	}
	
	private static AssetFactory assetFactory;
	
	private AssetFactory() {
		assets = new AssetManager();
		
		// Assets needed for splash screen
		assets.load(TextureType.TITLE, Texture.class);
		assets.finishLoading();
		
		// Assets needed for main menu screen
		assets.load(TextureType.RING, Texture.class);
		assets.load(TextureType.STAR, Texture.class);
		assets.load(SoundType.CLICK, Sound.class);
		assets.load(TextureType.LIGHTNING, Texture.class);
		assets.load(MusicType.MAIN_MENU, Music.class);
		
		// Assets needed for game
		assets.load(ModelType.PLANE, Model.class);
		assets.load(ModelType.SKYDIVER, Model.class);
		assets.load(ModelType.TARGET, Model.class);
		assets.load(SoundType.BELL, Sound.class);
		assets.load(SoundType.LAUGH, Sound.class);
		assets.load(MusicType.WIND, Music.class);
		assets.load(TextureType.SLIDER, Texture.class);
		assets.load(TextureType.SLIDERBAR, Texture.class);
		assets.load(TextureType.PAUSE, Texture.class);
		assets.load(TextureType.GOLD_STAR, Texture.class);
		assets.load(TextureType.EMPTY_STAR, Texture.class);
		assets.load(TextureType.WATER_TERRAIN, Texture.class);
		assets.load(TextureType.SAND_TERRAIN, Texture.class);
		assets.load(TextureType.GRASS_TERRAIN, Texture.class);
		assets.load(TextureType.FOLIAGE_TERRAIN, Texture.class);
	}
	
	public Array<String> getLoaded() {
		return assets.getAssetNames();
	}
	
	public static AssetFactory newInstance() {
		if (assetFactory != null) {
			throw new GdxRuntimeException("Only one AssetFactory can be instantiated.");
		}
		assetFactory = new AssetFactory();
		return assetFactory;
	}

	public boolean isLoaded() {
		return getLoadedAssets() == 22;
	}
	
	public int getLoadedAssets() {
		return assets.getLoadedAssets();
	}
}
