// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.scarlettapps.skydiver3d.resources.AssetFactory;
import com.scarlettapps.skydiver3d.resources.FontFactory;

/**
 * This is the screen which is shown initially as the main menu is loading.
 * @author Michael Scarlett
 *
 */
public class SplashScreen extends MenuScreen {

	private static final float LOADING_MIN = 0;
	private static final float LOADING_MAX = 1f;
	private static final float STEP_SIZE = 0.05f;
	private static final float ANIMATE_DURATION = 0.5f;
	
	private ProgressBar progressBar;
	
	/**
	 * Initialize the SplashScreen by loading graphics
	 * @param game the instance of the Skydiver3D game
	 */
	public SplashScreen(Skydiver3D game) {
		super(game);
	}
	
	@Override
	public void initializeScreen() {
		super.initializeScreen();
		
		// Renders background so that screen doesn't appear blank
		super.render(0);
		
        FontFactory fontFactory = FontFactory.getInstance();
		
		// Style for text label
		LabelStyle labelStyle = skin.get(LabelStyle.class);
		BitmapFont font = fontFactory.generateFont(42);
		labelStyle.font = font;
		
		// Text which displays "Loading"
		Label label = new Label("Loading", skin);
		label.setWidth(DefaultScreen.VIRTUAL_WIDTH/2);
		label.setAlignment(Align.center);
		table.add(label).width(DefaultScreen.VIRTUAL_WIDTH/2);
		table.row();

		// Loading progress bar
		progressBar = createProgressBar();
		progressBar.setAnimateDuration(ANIMATE_DURATION);
		progressBar.setWidth(DefaultScreen.VIRTUAL_WIDTH/2);
		progressBar.setHeight(DefaultScreen.VIRTUAL_HEIGHT/10);
		table.add(progressBar).width(DefaultScreen.VIRTUAL_WIDTH/2).height(DefaultScreen.VIRTUAL_HEIGHT/10);
	}
	
	/**
	 * Create the progress bar
	 * @return the progress bar
	 */
	private ProgressBar createProgressBar() {
		Drawable knob = createKnob();
        Drawable background = createLoadingBar();
		ProgressBarStyle style = new ProgressBarStyle(background, knob);
		style.knobBefore = knob;
		return new ProgressBar(LOADING_MIN, LOADING_MAX, STEP_SIZE, false, style);
	}
	
	/**
	 * Create the knob
	 * @return the knob
	 */
	private Drawable createKnob() {
		return new TiledDrawable(new TextureRegion(new Texture("skin/stripes.png")));
	}
	
	/**
	 * Create the loading bar
	 * @return the loading bar
	 */
	private Drawable createLoadingBar() {
		Pixmap pixmap = new Pixmap(VIRTUAL_WIDTH/2, DefaultScreen.VIRTUAL_HEIGHT/21, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		pixmap.setColor(Color.LIGHT_GRAY);
		return new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
	}
	
	/**
	 * Render the screen by displaying loading progress
	 */
	@Override
	public void render(float delta) {
		progressBar.setValue(getProgress());
		super.render(delta);
		
		if (game.mainMenuScreen.isLoaded()) {
			showMainMenu();
		} else {	
			AssetFactory.getInstance().update(50);
		}
	}
	
	/**
	 * Get the current loading progress as a ratio of total loaded/total assets to load
	 * @return the loading progress
	 */
	private float getProgress() {
		return LOADING_MAX*(AssetFactory.getInstance().getLoadedAssets()/5f)+LOADING_MIN;
	}
	
	/**
	 * Switch to the main menu screen
	 */
	private void showMainMenu() {
		game.mainMenuScreen.initialize();
		game.setScreen(game.mainMenuScreen);
	}

}
