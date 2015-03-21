// Copytight 2014 Michael Scarlett

package com.scarlettapps.skydiver3d.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.scarlettapps.skydiver3d.resources.AssetFactory.FontType;

public final class FontFactory {
	
	public enum FontStyle {
		REGULAR, BOLD, ITALIC
	}

	private static FontFactory instance = null;
	
	private FontFactory() {};
	
	public BitmapFont generateFont(int size) {
		return generateFont(size, false);
	}
	
	public BitmapFont generateFont(int size, boolean genMipMaps) {
		return generateFont(size, genMipMaps, TextureFilter.Nearest, TextureFilter.Nearest);
	}
	
	public BitmapFont generateFont(int size, boolean genMipMaps, TextureFilter minFilter, TextureFilter magFilter) {
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = size;
		parameter.genMipMaps = genMipMaps;
		parameter.minFilter = minFilter;
		parameter.magFilter = magFilter;
		return generateFont(parameter);
	}
	
	public BitmapFont generateFont(FreeTypeFontParameter parameter) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FontType.TUFFY));
		BitmapFont font = generator.generateFont(parameter);
		generator.dispose();
		return font;
	}
	
	public static FontFactory getInstance() {
		if (instance == null) {
			instance = new FontFactory();
		}
		return instance;
	}
}
