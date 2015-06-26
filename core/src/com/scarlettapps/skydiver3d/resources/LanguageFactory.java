package com.scarlettapps.skydiver3d.resources;

import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;

public class LanguageFactory {

	private static final String BUNDLE_LOCATION = "bundle/Lang";
	
	private static LanguageFactory instance = null;
	
	private final I18NBundle lang;
	
	public final String LOADING;
	public final String PLAY;
	public final String HELP;
	public final String OPTIONS;
	public final String CREDITS;
	public final String BACK_TO_MAIN;
	public final String BACK_TO_GAME;
	public final String SOUND_EFFECTS;
	public final String MUSIC;
	public final String VOLUME;
	public final String MOTION_SENSITIVITY;
	public final String HELP_TEXT_1;
	public final String HELP_TEXT_2;
	public final String HELP_TEXT_3;
	public final String TAP_SCREEN_TO_JUMP_OFF_PLANE;
	public final String TAP_SCREEN_TO_OPEN_PARACHUTE;
	public final String GET_READY_TO_LAND;
	public final String POINTS;
	public final String SPEED;
	public final String ALTITUDE;
	public final String GET_READY;
	public final String PAUSED;
	public final String RESUME;
	public final String TRY_AGAIN;
	public final String NEXT_LEVEL;
	public final String MAIN_MENU;
	public final String RING_SCORE;
	public final String PARACHUTING_SCORE;
	public final String LANDING_SCORE;
	public final String TOTAL_SCORE;
	public final String QUIT;
	public final String SUCCESS_0;
	public final String SUCCESS_1;
	public final String SUCCESS_2;
	public final String SUCCESS_3;
	public final String SUCCESS_4;
	public final String SUCCESS_5;
	public final String SUCCESS_6;
	public final String SUCCESS_7;
	public final String SUCCESS_8;
	public final String SUCCESS_9;
	public final String SUCCESS_10;

	private LanguageFactory() {
		I18NBundle.setSimpleFormatter(true);
		lang = I18NBundle.createBundle(Gdx.files.local(BUNDLE_LOCATION));
		
		LOADING = lang.get("loading");
		PLAY = lang.get("play");
		HELP = lang.get("help");
		OPTIONS = lang.get("options");
		CREDITS = lang.get("credits");
		BACK_TO_MAIN = lang.get("backToMain");
		BACK_TO_GAME = lang.get("backToGame");
		SOUND_EFFECTS = lang.get("soundEffects");
		MUSIC = lang.get("music");
		VOLUME = lang.get("volume");
		MOTION_SENSITIVITY = lang.get("motionSensitivity");
		HELP_TEXT_1 = lang.get("helpText1");
		HELP_TEXT_2 = lang.get("helpText2");
		HELP_TEXT_3 = lang.get("helpText3");
		TAP_SCREEN_TO_JUMP_OFF_PLANE = lang.get("tapScreenToJumpOffPlane");
		TAP_SCREEN_TO_OPEN_PARACHUTE = lang.get("tapScreenToOpenParachute");
		GET_READY_TO_LAND = lang.get("getReadyToLand");
		POINTS = lang.get("points");
		SPEED = lang.get("speed");
		ALTITUDE = lang.get("altitude");
		GET_READY = lang.get("getReady");
		PAUSED = lang.get("paused");
		RESUME = lang.get("resume");
		TRY_AGAIN = lang.get("tryAgain");
		NEXT_LEVEL = lang.get("nextLevel");
		MAIN_MENU = lang.get("mainMenu");
		RING_SCORE = lang.get("ringScore");
		PARACHUTING_SCORE = lang.get("parachutingScore");
		LANDING_SCORE = lang.get("landingScore");
		TOTAL_SCORE = lang.get("totalScore");
		QUIT = lang.get("quit");
		SUCCESS_0 = lang.get("success0");
		SUCCESS_1 = lang.get("success1");
		SUCCESS_2 = lang.get("success2");
		SUCCESS_3 = lang.get("success3");
		SUCCESS_4 = lang.get("success4");
		SUCCESS_5 = lang.get("success5");
		SUCCESS_6 = lang.get("success6");
		SUCCESS_7 = lang.get("success7");
		SUCCESS_8 = lang.get("success8");
		SUCCESS_9 = lang.get("success9");
		SUCCESS_10 = lang.get("success10");
	}
	
	public String get(String key) {
		return lang.get(key);
	}
	
	public String format(String key, Object... args) {
		return lang.format(key, args);
	}
	
	public static LanguageFactory getInstance() {
		if (instance == null) {
			instance = new LanguageFactory();
		}
		return instance;
	}
}
