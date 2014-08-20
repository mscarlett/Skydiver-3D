// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.scarlettapps.skydiver3d.resources.FontFactory;

public class LevelSelectScreen extends MenuScreen {

	public LevelSelectScreen(SkyDiver3D game) {
		super(game);
		
		TextButtonStyle textButtonStyle = skin.get(TextButtonStyle.class);
		BitmapFont font = FontFactory.generateFont(42);
		textButtonStyle.font = font;
		
		LabelStyle labelStyle = skin.get(LabelStyle.class);
		font = FontFactory.generateFont(20);
		labelStyle.font = font;
		font = FontFactory.generateFont(64);
		skin.add("Title font", font, BitmapFont.class);
			
		// register the back button
				TextButton backButton = new TextButton("Back to Main", skin);
				backButton.addListener(new ClickListener() {
					
					@Override
			        public void clicked(InputEvent event, float x, float y) {
						backToMainMenu();
			        }
					/*@Override
					public void touchUp(ActorEvent event, float x, float y,
							int pointer, int button) {
						//super.touchUp(event, x, y, pointer, button);
						//game.getSoundManager().play(TyrianSound.CLICK);
						//game.setScreen(new MenuScreen(game));
					}*/
				});
				table.row();
				table.add(backButton).size(250, 60).colspan(3);
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
