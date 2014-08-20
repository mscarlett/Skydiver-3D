// Copytight 2014 Michael Scarlett

package com.scarlettapps.skydiver3d.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.scarlettapps.skydiver3d.resources.AssetFactory.FontType;

public class FontFactory {
	
	public enum FontStyle {
		REGULAR, BOLD, ITALIC
	}
	
	private static final String FONT_FILE = FontType.TUFFY;

	public static BitmapFont generateFont(int size) {
		return generateFont(size, false);
	}
	
	public static BitmapFont generateFont(int size, boolean genMipMaps) {
		return generateFont(size, genMipMaps, TextureFilter.Nearest, TextureFilter.Nearest);
	}
	
	public static BitmapFont generateFont(int size, boolean genMipMaps, TextureFilter minFilter, TextureFilter magFilter) {
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = size;
		parameter.genMipMaps = genMipMaps;
		parameter.minFilter = minFilter;
		parameter.magFilter = magFilter;
		return generateFont(parameter);
	}
	
	public static BitmapFont generateFont(FreeTypeFontParameter parameter) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FONT_FILE));
		BitmapFont font = generator.generateFont(parameter);
		generator.dispose();
		return font;
	}
}
