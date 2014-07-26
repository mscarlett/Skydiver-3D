// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.utils.ObjectSet;

public class Graphics {
	
	private static final String TITLE_FILE_NAME = "data/title.png";
	//private static final String RING_FILE_NAME = "data/ring-big_2.png";
	//private static final String STAR_FILE_NAME = "data/star-big_2.png";
	//private static final String FONT_ONE = "data/comic_sans_modified_4.fnt";
	//private static final String FONT_TWO = "data/comic_sans_modified_5.fnt";
	
	public static Texture title = new Texture(Gdx.files.internal(TITLE_FILE_NAME));
	
	private static final AssetManager assets = new AssetManager();
	
	private static final ObjectSet<AssetDescriptor<?>> toLoad = toLoad();
	
	public static void load() {
		for (AssetDescriptor<?> desc : toLoad) {
			assets.load(desc.fileName, desc.type);
		}
	}
	
	public static void load(String fileName, Class<?> type) {
		assets.load(fileName, type);
	}
	
	public static <T> T get(String fileName, Class<T> type) {
		return assets.get(fileName, type);
	}

	public static void update(int millis) {
		assets.update(millis);
	}

	public static boolean isLoaded() {
		for (AssetDescriptor<?> desc: toLoad) {
			if (assets.isLoaded(desc.fileName, desc.type)) {
				toLoad.remove(desc);
			}
		}
		return toLoad.size == 0;
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
	
	private static ObjectSet<AssetDescriptor<?>> toLoad() {
		ObjectSet<AssetDescriptor<?>> toLoad = new ObjectSet<AssetDescriptor<?>>();
		toLoad.add(new AssetDescriptor<Texture>("data/ring-big_2.png", Texture.class));
		toLoad.add(new AssetDescriptor<Texture>("data/star-big_2.png", Texture.class));
		toLoad.add(new AssetDescriptor<Model>("data/plane_new_6.g3db", Model.class));
		toLoad.add(new AssetDescriptor<Model>("data/human_with_parachute_and_backpack_and_helmet_a8.g3db", Model.class));
		toLoad.add(new AssetDescriptor<Model>("data/target_raft_5.g3db", Model.class));
		toLoad.add(new AssetDescriptor<Sound>("data/bell.ogg", Sound.class));
		toLoad.add(new AssetDescriptor<Music>("data/wind.ogg", Music.class));
		toLoad.add(new AssetDescriptor<BitmapFont>("data/comic_sans_modified_4.fnt", BitmapFont.class));
		toLoad.add(new AssetDescriptor<BitmapFont>("data/comic_sans_modified_5.fnt", BitmapFont.class));
		toLoad.add(new AssetDescriptor<Texture>("data/slider2.png", Texture.class));
		toLoad.add(new AssetDescriptor<Texture>("data/sliderbar.png", Texture.class));
		toLoad.add(new AssetDescriptor<Texture>("data/pause2.png", Texture.class));
		toLoad.add(new AssetDescriptor<Texture>("data/goldstar.png", Texture.class));
		toLoad.add(new AssetDescriptor<Texture>("data/emptystar.png", Texture.class));
		toLoad.add(new AssetDescriptor<Texture>("data/lightwater.jpg", Texture.class));
		toLoad.add(new AssetDescriptor<Texture>("data/sandtile.jpg", Texture.class));
		toLoad.add(new AssetDescriptor<Texture>("data/foliage_tiled.png", Texture.class));
		toLoad.add(new AssetDescriptor<Texture>("data/grasstile.jpg", Texture.class));
		return toLoad;
	}
	
	public static void dispose() {
		assets.dispose();
	}
}
