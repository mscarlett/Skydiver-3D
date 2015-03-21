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
		TiledDrawable knob = new TiledDrawable(new TextureRegion(new Texture(Gdx.files.internal("skin/stripes.png"))));
		Pixmap pixmap = new Pixmap(VIRTUAL_WIDTH/2, DefaultScreen.VIRTUAL_HEIGHT/21, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		pixmap.setColor(Color.LIGHT_GRAY);
		Drawable background = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
		ProgressBarStyle style = new ProgressBarStyle(background, knob);
		style.knobBefore = knob;
		progressBar = new ProgressBar(LOADING_MIN, LOADING_MAX, STEP_SIZE, false, style);
		progressBar.setAnimateDuration(ANIMATE_DURATION);
		progressBar.setWidth(DefaultScreen.VIRTUAL_WIDTH/2);
		progressBar.setHeight(DefaultScreen.VIRTUAL_HEIGHT/10);
		table.add(progressBar).width(DefaultScreen.VIRTUAL_WIDTH/2).height(DefaultScreen.VIRTUAL_HEIGHT/10);
	}
	
	/**
	 * Render the screen by displaying loagind progress
	 */
	@Override
	public void render(float delta) {
		if (game.mainMenuScreen.isLoaded()) {
			showMainMenu();
		} else {
			AssetFactory assets = AssetFactory.getInstance();
			float progress = LOADING_MAX*(assets.getLoadedAssets()/5f)+LOADING_MIN;
			progressBar.setValue(progress);
			super.render(delta);
			assets.update(50);
		}
	}
	
	/**
	 * Switch to the main menu screen
	 */
	private void showMainMenu() {
		game.setScreen(game.mainMenuScreen);
	}

	@Override
	protected void showScreen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void pauseScreen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void resumeScreen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void hideScreen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void disposeScreen() {
		// TODO Auto-generated method stub
		
	}

}
