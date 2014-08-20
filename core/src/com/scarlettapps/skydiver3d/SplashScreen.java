// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.scarlettapps.skydiver3d.resources.AssetFactory;
import com.scarlettapps.skydiver3d.resources.FontFactory;

public class SplashScreen extends MenuScreen {

	private static final float LOADING_MIN = 0;
	private static final float LOADING_MAX = 1f;
	private static final float STEP_SIZE = 0.05f;
	private static final float ANIMATE_DURATION = 0.5f;
	
	private ProgressBar progressBar;
	
	public SplashScreen(SkyDiver3D game) {
		super(game);
		
		LabelStyle labelStyle = skin.get(LabelStyle.class);
		BitmapFont font = FontFactory.generateFont(42);
		labelStyle.font = font;
		
		Label label = new Label("Loading", skin);
		label.setWidth(DefaultScreen.VIRTUAL_WIDTH/2);
		label.setAlignment(Align.center);
		table.add(label).width(DefaultScreen.VIRTUAL_WIDTH/2);
		table.row();

		TiledDrawable knob = new TiledDrawable(new TextureRegion(new Texture(Gdx.files.internal("skin/stripes.png"))));
		Pixmap pixmap = new Pixmap(500, 30, Format.RGBA8888);
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
	
	@Override
	public void render(float delta) {
		if (game.mainMenuScreen.isLoaded()) {
			showMainMenu();
		} else {
			float progress = LOADING_MAX*(game.assets.getLoadedAssets()/6f)+LOADING_MIN;
			progressBar.setValue(progress);
			super.render(delta);
			AssetFactory.update(50);
		}
	}
	
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
