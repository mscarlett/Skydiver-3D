// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.scarlettapps.skydiver3d.resources.Graphics;

public class LoadingScreen extends MenuScreen {

	private static final float LOADING_MIN = 0;
	private static final float LOADING_MAX = 100;
	private static final float STEP_SIZE = 10;
	private static final float ANIMATE_DURATION = 0.5f;
	
	private ProgressBar progressBar;
	
	public LoadingScreen(SkyDiver3D game) {
		super(game);
		
		Label label = new Label("Loading", skin);
		label.setWidth(DefaultScreen.VIRTUAL_WIDTH/2);
		label.setAlignment(Align.center);
		table.add(label).width(DefaultScreen.VIRTUAL_WIDTH/2);
		table.row();
		
		Sprite sprite = new Sprite(new Texture(Gdx.files.internal("skin/stripes.png")));
		Drawable background = new SpriteDrawable(sprite);
		Drawable knob = new SpriteDrawable(sprite);
		ProgressBarStyle style = new ProgressBarStyle(background, knob);
		progressBar = new ProgressBar(LOADING_MIN, LOADING_MAX, STEP_SIZE, false, style);
		progressBar.setAnimateDuration(ANIMATE_DURATION);
		progressBar.setWidth(DefaultScreen.VIRTUAL_WIDTH/2);
		progressBar.setHeight(DefaultScreen.VIRTUAL_HEIGHT/10);
		table.add(progressBar).width(DefaultScreen.VIRTUAL_WIDTH/2).height(DefaultScreen.VIRTUAL_HEIGHT/10);
	}
	
	@Override
	public void render(float delta) {
		if (game.playingScreen.isLoaded()) {
			startGame();
		} else {
			float progress = LOADING_MAX*Graphics.getProgress()+LOADING_MIN;
			progressBar.setValue(progress);
			super.render(delta);
			Graphics.update(50);
		}
	}
	
	private void startGame() {
		game.setScreen(game.playingScreen);
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
