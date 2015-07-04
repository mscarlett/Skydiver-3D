// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.scarlettapps.skydiver3d.resources.AchievementsFactory;
import com.scarlettapps.skydiver3d.resources.AssetFactory.SoundType;
import com.scarlettapps.skydiver3d.resources.AssetFactory.TextureType;
import com.scarlettapps.skydiver3d.resources.AssetFactory;
import com.scarlettapps.skydiver3d.resources.FontFactory;
import com.scarlettapps.skydiver3d.resources.LanguageFactory;
import com.scarlettapps.skydiver3d.resources.SoundFactory;

public class LevelSelectScreen extends MenuScreen {
	
	// Transition time between this screen and the game screen
	private static final float TRANSITION_TIME = 0.2f;
	
	private final Table miniTable;
	
	private Texture cloudButton;
	private Texture lockButton;
	private Texture star;
	//private TextureRegion blueArrow;
	
	private int screenIdx;
	
	public LevelSelectScreen(Skydiver3D game) {
		super(game);
		
		miniTable = new Table();
	}
	
	@Override
	public void initializeScreen() {
		super.initializeScreen();
		
		final SoundFactory sound = SoundFactory.getInstance();
		
        FontFactory fontFactory = FontFactory.getInstance();
        LanguageFactory lang = LanguageFactory.getInstance();
        
        TextButtonStyle textButtonStyle = skin.get(TextButtonStyle.class);
		BitmapFont font = fontFactory.generateFont(42);
		textButtonStyle.font = font;
		
		LabelStyle labelStyle = skin.get(LabelStyle.class);
		font = fontFactory.generateFont(56, Color.DARK_GRAY);
		labelStyle.font = font;
		font = fontFactory.generateFont(64);
		skin.add("Title font", font, BitmapFont.class);
		
		Label title = new Label("Level Select", skin, "Title font", Color.WHITE);
		table.add(title).spaceBottom(5).center();
		table.row();
		
		TextureRegion blueArrow = new TextureRegion(new Texture(TextureType.ARROW_BLUE));
		TextureRegion blueArrow2 = new TextureRegion(blueArrow);
		blueArrow2.flip(true,  false);
				
		float arrowSize = 80f;
		final Image leftArrow = new Image(blueArrow2);
		leftArrow.setVisible(false);
		leftArrow.setWidth(arrowSize);
		leftArrow.setHeight(arrowSize);
		final Image rightArrow = new Image(blueArrow);
		leftArrow.setY(DefaultScreen.VIRTUAL_HEIGHT/2);
		rightArrow.setWidth(arrowSize);
		rightArrow.setHeight(arrowSize);
		rightArrow.setX(DefaultScreen.VIRTUAL_WIDTH-arrowSize);
		rightArrow.setY(DefaultScreen.VIRTUAL_HEIGHT/2);
		
		leftArrow.addListener(new ClickListener() {
			
			@Override
	        public void clicked(InputEvent event, float x, float y) {
				screenIdx -= 1;
				if (screenIdx == 0) {
					leftArrow.setVisible(false);
				}
				miniTable.clearChildren();
				makeMiniTable(miniTable);
				rightArrow.setVisible(true);
	        }
			
		});
		
        rightArrow.addListener(new ClickListener() {
			
			@Override
	        public void clicked(InputEvent event, float x, float y) {
				screenIdx += 1;
				leftArrow.setVisible(true);
				miniTable.clearChildren();
				makeMiniTable(miniTable);
				if (screenIdx == 2) {
					rightArrow.setVisible(false);
				}
	        }
			
		});
		
		stage.addActor(leftArrow);
		stage.addActor(rightArrow);
		
	    table.add(miniTable);
	    
	    // register the back button
		TextButton backButton = new TextButton("Back to Main", skin);
		backButton.addListener(new ClickListener() {
			
			@Override
	        public void clicked(InputEvent event, float x, float y) {
				backToMainMenu();
	        }
			
		});
		// add back button to table
		table.row().pad(20f);
		table.add(backButton).size(250, 60).colspan(3);
		
		cloudButton = new Texture(TextureType.BUTTON);
		lockButton = new Texture(TextureType.LOCK);
		 
		star = AssetFactory.getInstance().get(TextureType.GOLD_STAR, Texture.class);
	}
	
	private void makeMiniTable(Table miniTable) {
		AchievementsFactory achievements = AchievementsFactory.getInstance();
		final SoundFactory sound = SoundFactory.getInstance();
		
		miniTable.row().height(120f);
		
		int levelsCompleted = achievements.levelsCompleted();
		
		int start = screenIdx*16;
		
	    for (int i = start; i <= 15 + start; i++) {
	    	 if (i != 0 && i % 4 == 0) {
	    		 miniTable.row().height(120f);
	    	 }
	    	 
	    	 final int levelNum = i + 1;
	    	 
	    	 boolean isPlayable = levelsCompleted >= i;
	    	 
	    	 Image image = isPlayable ? new Image(cloudButton) : new Image(lockButton);
	    	 
	    	 
	    	 Stack stack = new Stack();
	    	//First add actual content
	    	 stack.add(image);

	    	 if (isPlayable) {
	    		//Second add wrapped overlay object
		    	 Table overlay = new Table();
	    		 Label level = new Label("" + (i+1), skin);
	    	     overlay.add(level).top().center().padBottom(12f);
	    	     
	    		 stack.addListener(new ClickListener() {
			 		@Override
					public void clicked(InputEvent event, float x, float y) {
			 			sound.play(SoundType.CLICK);
			 			startGame(levelNum);
					}
			     });
	    		 
	    		 int rating = achievements.getLevelRating(i+1);

                 overlay.row().height(25f).width(150f);
	    		 
	    		 Group g = new Group();
	    		 
	    		 for (int j = 0; j < rating; j++) {
	    			 Image s = new Image(star);
	    			 s.setWidth(35f);
	    			 s.setHeight(35f);
	    			 s.setX(25f*j);
	    			 g.addActor(s);
	    		 }
	    		 
	    		 overlay.add(g).padBottom(10f);
	    		 
	    		 stack.add(overlay);
	    	 }
	    	 
	    	 miniTable.add(stack).left().top().width(200f);
	     }
	}
	
	@Override
	protected void showScreen() {
		screenIdx = 0;
		miniTable.clearChildren();
		makeMiniTable(miniTable);
	}
	
	/**
	 * Return to the main menu screen
	 */
	private void backToMainMenu() {
		game.setScreen(game.mainMenuScreen);
	}
	
	private void startGame(int levelNum) {
		game.playingScreen.setLevel(levelNum);
		game.playingScreen.setPaused(false);
		DefaultScreen<?> next = game.playingScreen.isLoaded() ? game.playingScreen : game.loadingScreen;
		transitionScreen(next, TRANSITION_TIME);
	}

}
