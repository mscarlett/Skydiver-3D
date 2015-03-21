// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.scarlettapps.skydiver3d.resources.AssetFactory.SoundType;
import com.scarlettapps.skydiver3d.resources.AssetFactory.TextureType;
import com.scarlettapps.skydiver3d.resources.FontFactory;
import com.scarlettapps.skydiver3d.resources.SoundFactory;

public class HelpScreen extends MenuScreen {

	public HelpScreen(Skydiver3D game) {
		super(game);
		
		FontFactory fontFactory = FontFactory.getInstance();
		
		TextButtonStyle textButtonStyle = skin.get(TextButtonStyle.class);
		BitmapFont font = fontFactory.generateFont(42);
		textButtonStyle.font = font;
		
		LabelStyle labelStyle = skin.get(LabelStyle.class);
		font = fontFactory.generateFont(20);
		labelStyle.font = font;
		font = fontFactory.generateFont(64);
		skin.add("Title font", font, BitmapFont.class);
		
		Label title = new Label("Help", skin, "Title font", Color.WHITE);
		title.setAlignment(Align.center);
		table.add(title).spaceBottom(10).fill().center();
		table.row();
		
		Label label = new Label("Tilt your phone sideways to drift left or right", skin);
		label.setAlignment(Align.center);
		table.add(label).center();
		table.row();
		
		Image phone = new Image(new Texture(TextureType.PHONE_ROTATE));
		table.add(phone).spaceBottom(10).center();
		table.row();
		
		label = new Label("Earn points by collecting rings and stars", skin);
		label.setAlignment(Align.center);
		table.add(label);
		table.row();
		
		Table group = new Table();
		Image goldRing = new Image(new Texture(TextureType.RING));
		int s = 75;
		goldRing.setSize(s,s);
		group.add(goldRing).size(s, s);
		Image star = new Image(new Texture(TextureType.STAR));
		star.setSize(s,s);
		group.add(star).size(s, s);
		
		table.add(group).spaceBottom(10);
		table.row();
		
		label = new Label("Open your parachute with perfect timing and\nland in the center of the target to get bonus points", skin);
		label.setAlignment(Align.center);
		table.add(label).center().fill();
		table.row();
		
		Image diving = new Image(new Texture(TextureType.DARTS));
		table.add(diving).spaceBottom(10).center();
		table.row();
		
		// register the button "resume game"
		TextButton backButton = new TextButton("Back to Main", skin);
		backButton.addListener(new ClickListener() {
			
			@Override
	        public void clicked(InputEvent event, float x, float y) {
				backToMainMenu();
	        }
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				SoundFactory.getInstance().play(SoundType.CLICK);
				backToMainMenu();
			}
		});
		table.add(backButton).fill().center();
	}
	
	private void backToMainMenu() {
		game.setScreen(game.mainMenuScreen);
	}

	@Override
	public void showScreen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hideScreen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pauseScreen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resumeScreen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disposeScreen() {
		// TODO Auto-generated method stub
		
	}

}
