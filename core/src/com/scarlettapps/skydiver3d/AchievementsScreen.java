// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class AchievementsScreen extends MenuScreen {

	public AchievementsScreen(SkyDiver3D game) {
		super(game);
		
		Label title = new Label("Achievements", skin);
		title.setFontScale(2f);
		table.add(title);
		table.row();
		
		Label label = new Label("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", skin);
		ScrollPane scrollPane = new ScrollPane(label);
		scrollPane.setWidth(500);
		scrollPane.setHeight(100);
		scrollPane.setScrollingDisabled(true, false);
		table.add(scrollPane).width(500).height(100);
		table.row();
		
		// register the button "resume game"
		TextButton backToMainButton = new TextButton("Back to Main", skin);
		backToMainButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				backToMainMenu();
			}
		});
		table.add(backToMainButton).size(300, 60);
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
