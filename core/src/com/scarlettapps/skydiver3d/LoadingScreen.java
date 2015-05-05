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

public class LoadingScreen extends MenuScreen {

	private static final float LOADING_MIN = 0;
	private static final float LOADING_MAX = 1f;
	private static final float STEP_SIZE = 0.05f;
	private static final float ANIMATE_DURATION = 0.5f;
	
	private ProgressBar progressBar;
	
	public LoadingScreen(Skydiver3D game) {
		super(game);
		
		FontFactory fontFactory = FontFactory.getInstance();
		
		LabelStyle labelStyle = skin.get(LabelStyle.class);
		BitmapFont font = fontFactory.generateFont(42);
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
		AssetFactory assetFactory = AssetFactory.getInstance();
		if (game.playingScreen.isLoaded()) {
			startGame();
		} else {
			float progress = LOADING_MAX*assetFactory.getProgress()+LOADING_MIN;
			progressBar.setValue(progress);
			super.render(delta);
			assetFactory.update(50);
		}
	}
	
	protected void startGame() {
		game.setScreen(game.playingScreen);
	}

}
