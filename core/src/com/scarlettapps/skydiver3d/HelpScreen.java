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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.scarlettapps.skydiver3d.resources.AssetFactory.SoundType;
import com.scarlettapps.skydiver3d.resources.AssetFactory.TextureType;
import com.scarlettapps.skydiver3d.resources.FontFactory;
import com.scarlettapps.skydiver3d.resources.LanguageFactory;
import com.scarlettapps.skydiver3d.resources.SoundFactory;

public class HelpScreen extends MenuScreen {

	public HelpScreen(Skydiver3D game) {
		super(game);
	}
	
	@Override
	public void initializeScreen() {
		super.initializeScreen();
		
        FontFactory fontFactory = FontFactory.getInstance();
		LanguageFactory lang = LanguageFactory.getInstance();
        
		TextButtonStyle textButtonStyle = skin.get(TextButtonStyle.class);
		BitmapFont font = fontFactory.generateFont(42);
		textButtonStyle.font = font;
		
		LabelStyle labelStyle = skin.get(LabelStyle.class);
		font = fontFactory.generateFont(32);
		labelStyle.font = font;
		font = fontFactory.generateFont(64);
		skin.add("Title font", font, BitmapFont.class);
		
		Label title = new Label(lang.HELP, skin, "Title font", Color.WHITE);
		title.setAlignment(Align.center);
		table.add(title).spaceBottom(5).fill().center();
		table.row();
		
		Label label = new Label(lang.HELP_TEXT_1, skin);
		label.setAlignment(Align.center);
		table.add(label).center();
		table.row();
		
		Image phone = new Image(new Texture(TextureType.PHONE_ROTATE));
		table.add(phone).spaceBottom(5).center();
		table.row();
		
		label = new Label(lang.HELP_TEXT_2, skin);
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
		Image skull = new Image(new Texture(TextureType.RING_SKULL));
		skull.setSize(s,s);
		group.add(skull).size(s, s);
		Image nuclear = new Image(new Texture(TextureType.RING_NUCLEAR));
		nuclear.setSize(s,s);
		group.add(nuclear).size(s, s);
		Image ghost = new Image(new Texture(TextureType.RING_GHOST));
		ghost.setSize(s,s);
		group.add(ghost).size(s, s);
		
		table.add(group).spaceBottom(5);
		table.row();
		
		label = new Label(lang.HELP_TEXT_3, skin);
		label.setAlignment(Align.center);
		table.add(label).center().fill();
		table.row();
		
		Image diving = new Image(new Texture(TextureType.DARTS));
		table.add(diving).spaceBottom(5).center();
		table.row();
		
		// register the button "resume game"
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
		table.add(backButton).fill().center();
	}
	
	private void backToMainMenu() {
		game.setScreen(game.mainMenuScreen);
	}

}
