// Copytight 2014 Michael Scarlett

package com.scarlettapps.skydiver3d.resources;

import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.scarlettapps.skydiver3d.resources.AssetFactory.FontType;

public final class FontFactory {
	
	public static final String RUSSIAN_CHARACTERS =
			"АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ"
            + "абвгдеёжзийклмнопрстуфхцчшщъыьэюя"
            + "1234567890.,:;_¡!¿?\"'+-*/()[]={}%"
            + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "abcdefghijklmnopqrstuvwxyz";
	
	public static final String JAPANESE_CHARACTERS =
			"あいうえおかきくけこがぎぐげごさしすせそざじずぜぞた"
			+ "ちつてとだぢづでどなにぬねのはひふへほばびぶべぼ"
			+ "ぱぴぷぺぽまみむめもやゆよらりるれろわをんっゝ゛゜"
			+ "アイウエオカキクケコガギグゲゴサシスセソザジズゼゾ"
			+ "タチツテトダヂヅデドナニヌネノハヒフヘホバビブベボパ"
			+ "ピプペポマミムメモヤユヨラリルレロワヲンッーヽ"
			+ "1234567890.,:;_¡!¿?\"'+-*/()[]={}%"
			+ "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "abcdefghijklmnopqrstuvwxyz";
	
	public static final String CHINESE_CHARACTERS = ""
			+ "1234567890.,:;_¡!¿?\"'+-*/()[]={}%"
			+ "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "abcdefghijklmnopqrstuvwxyz";

	private static FontFactory instance = null;
	
	private final FreeTypeFontGenerator generator;
	private final String characters;
	
	private FontFactory() {
		String fontType;
        String lang = Locale.getDefault().getLanguage();
		
		if (lang.equals("ru")) {
			fontType = FontType.ARIMO;
			characters = RUSSIAN_CHARACTERS;
		} /*else if (lang.equals("ja")) {
			fontType = FontType.NOTOSANS;
			characters = JAPANESE_CHARACTERS;
		} else if (lang.equals("zh")) {
			fontType = FontType.NOTOSANS;
			characters = CHINESE_CHARACTERS;
		}*/ else {
			fontType = FontType.TUFFY;
			characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		}
		
		generator = new FreeTypeFontGenerator(Gdx.files.internal(fontType));
	}
	
	public BitmapFont generateFont(int size) {
		return generateFont(size, Color.WHITE);
	}
	
	public BitmapFont generateFont(int size, Color c) {
		return generateFont(size, c, false);
	}
	
	public BitmapFont generateFont(int size, Color c, boolean genMipMaps) {
		return generateFont(size, c, genMipMaps, TextureFilter.Nearest, TextureFilter.Nearest);
	}
	
	public BitmapFont generateFont(int size, Color c, boolean genMipMaps, TextureFilter minFilter, TextureFilter magFilter) {
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.color = c;
		parameter.size = size;
		parameter.characters = characters;
		parameter.genMipMaps = genMipMaps;
		parameter.minFilter = minFilter;
		parameter.magFilter = magFilter;
		return generateFont(parameter);
	}
	
	public BitmapFont generateFont(FreeTypeFontParameter parameter) {
		return generator.generateFont(parameter);
	}
	
	public void dispose() {
		generator.dispose();
	}
	
	public static FontFactory getInstance() {
		if (instance == null) {
			instance = new FontFactory();
		}
		return instance;
	}
}
