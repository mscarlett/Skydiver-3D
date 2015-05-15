// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.scarlettapps.skydiver3d.resources.FontFactory;

public class LevelSelectScreen extends MenuScreen {

	public LevelSelectScreen(Skydiver3D game) {
		super(game);
	}
	
	@Override
	public void initializeScreen() {
		super.initializeScreen();
		
        FontFactory fontFactory = FontFactory.getInstance();
		
		TextButtonStyle textButtonStyle = skin.get(TextButtonStyle.class);
		BitmapFont font = fontFactory.generateFont(42);
		textButtonStyle.font = font;
		
		LabelStyle labelStyle = skin.get(LabelStyle.class);
		font = fontFactory.generateFont(20);
		labelStyle.font = font;
		font = fontFactory.generateFont(64);
		skin.add("Title font", font, BitmapFont.class);
			
		// register the back button
		TextButton backButton = new TextButton("Back to Main", skin);
		backButton.addListener(new ClickListener() {
			
			@Override
	        public void clicked(InputEvent event, float x, float y) {
				backToMainMenu();
	        }
			
		});
		// add back button to table
		table.row();
		table.add(backButton).size(250, 60).colspan(3);
	}
	
	/**
	 * Return to the main menu screen
	 */
	private void backToMainMenu() {
		game.setScreen(game.mainMenuScreen);
	}

}
