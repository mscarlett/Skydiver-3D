// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d;

import java.util.Calendar;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.scarlettapps.skydiver3d.resources.FontFactory;
import com.scarlettapps.skydiver3d.resources.AssetFactory.SoundType;
import com.scarlettapps.skydiver3d.resources.LanguageFactory;
import com.scarlettapps.skydiver3d.resources.SoundFactory;

public class CreditsScreen extends MenuScreen {

	public CreditsScreen(Skydiver3D game) {
		super(game);
	}
	
	@Override
	public void initializeScreen() {
		super.initializeScreen();
		
		drawCopyrightMessage();
		addBackButton();
	}
	
	private void drawCopyrightMessage() {
		FontFactory fontFactory = FontFactory.getInstance();
		LanguageFactory lang = LanguageFactory.getInstance();
		
		TextButtonStyle textButtonStyle = skin.get(TextButtonStyle.class);
		BitmapFont font = fontFactory.generateFont(42);
		textButtonStyle.font = font;
		
		LabelStyle labelStyle = skin.get(LabelStyle.class);
		font = fontFactory.generateFont(36);
		labelStyle.font = font;
		font = fontFactory.generateFont(64);
		skin.add("Title font", font, BitmapFont.class);
		
		Label title = new Label(lang.CREDITS, skin, "Title font", Color.WHITE);
		table.add(title);
		table.row();
		
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		String yearInString = String.valueOf(year);
		Label label = new Label("Developed by Michael Scarlett\n\nCopyright (c) "+yearInString+" Michael Scarlett\n\nMain menu music \"Local Forecast\" by Kevin MacLeod\nWebsite: incompetech.com\n\nThis game is purely fictional and unrealistic.\nIt is not intended to teach skydiving. Don't\nget yourself injured because of a video game!", skin);
		ScrollPane scrollPane = new ScrollPane(label);
		scrollPane.setWidth(VIRTUAL_WIDTH*0.75f);
		scrollPane.setHeight(VIRTUAL_HEIGHT*0.9f);
		scrollPane.setScrollingDisabled(true, false);
		table.add(scrollPane).fill().spaceBottom(20);
		table.row();
	}
	
	private void addBackButton() {
		// register the button "resume game"
		LanguageFactory lang = LanguageFactory.getInstance();
		TextButton backButton = new TextButton(lang.BACK_TO_MAIN, skin);
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
		table.add(backButton).size(300, 60);
	}
	
	private void backToMainMenu() {
		game.setScreen(game.mainMenuScreen);
	}

}
