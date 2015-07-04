// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.SnapshotArray;
import com.scarlettapps.skydiver3d.resources.AchievementsFactory;
import com.scarlettapps.skydiver3d.resources.AssetFactory;
import com.scarlettapps.skydiver3d.resources.AssetFactory.TextureType;
import com.scarlettapps.skydiver3d.resources.FontFactory;
import com.scarlettapps.skydiver3d.resources.LanguageFactory;
import com.scarlettapps.skydiver3d.worldstate.Score;

public class LevelCompletedScreen extends MenuScreen {
	
	private Table stars;
	private Drawable goldTextureDrawable;
	private Drawable emptyTextureDrawable;
	
	private Label ringScore;
	private Label parachutingScore;
	private Label landingScore;
	private Label totalScore;
	
    private ShapeRenderer shapeRenderer;
	
	private float elapsedTime;
	private static final float MAX_ALPHA = 0.8f;
	private static final float TRANSITION_TIME = 5f;
	private static final float DELAY = 2f;
	private static final float VISIBILITY_THRESHOLD = 0.3f;
	
	public LevelCompletedScreen(Skydiver3D game) {
		super(game, false);
		
		shapeRenderer = null;
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
		font = fontFactory.generateFont(36);
		labelStyle.font = font;
		
		ringScore = new Label(lang.RING_SCORE + ": 0", skin);
		table.add(ringScore).size(300, 40);
		table.row();
		parachutingScore = new Label(lang.PARACHUTING_SCORE + ": 0", skin);
		table.add(parachutingScore).size(300, 40);
		table.row();
		landingScore = new Label(lang.LANDING_SCORE + ": 0", skin);
		table.add(landingScore).size(300, 40);
		table.row();
		totalScore = new Label(lang.TOTAL_SCORE + ": 0", skin);
		table.add(totalScore).size(300, 40);
		table.row();
		
		stars = new Table();
				
		final int size = 75;
		final int offset = 25;
		
		// Add stars to stage
		for (int i = 0; i < 5; i++) {
			Image image = new Image();
			image.setVisible(true);
			image.setSize(size, size);
			Cell<Image> cell = stars.add(image).size(size,size);
			if (i != 4) {
				cell.spaceRight(offset);
			}
		}
		
		table.add(stars).size(300, 100).spaceBottom(10);
		stars.setVisible(true);
		table.row();
		
		// register the button "Next Level"
		TextButton nextLevelButton = new TextButton(lang.NEXT_LEVEL, skin);
		nextLevelButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				nextLevel();
			}
		});
		table.align(Align.bottom);
		table.add(nextLevelButton).size(400, 60).uniform().spaceBottom(10);
		table.row();

		// register the button "Try Again"
		TextButton tryAgainButton = new TextButton(lang.TRY_AGAIN, skin);
		tryAgainButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				restartLevel();
			}

		});
		table.add(tryAgainButton).size(400, 60).uniform().fill().spaceBottom(10);
		table.row();

		// register the button "Main Menu"
		TextButton optionsButton = new TextButton(lang.MAIN_MENU, skin);
		optionsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				backToMainMenu();
			}
		});
		table.add(optionsButton).size(400, 60).uniform().fill().spaceBottom(10);
		table.row();

		// register the button "Quit"
		TextButton quitButton = new TextButton(lang.QUIT, skin);
		quitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				quitGame();
			}
		});
		table.add(quitButton).size(400, 60).uniform().fill();
		
		table.padBottom(50);
		
		stage.addActor(table);
		
		if (shapeRenderer == null) {
			shapeRenderer = new ShapeRenderer();
		}
	}
	
	@Override
	public void render(float delta) {
		// Clear screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		// Update and draw world
		game.playingScreen.updateObjects(delta);
		game.playingScreen.renderObjects(delta);
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.begin(ShapeType.Filled);
		final float bgAlpha = Math.min(elapsedTime/(TRANSITION_TIME/MAX_ALPHA),MAX_ALPHA);
		shapeRenderer.setColor(0f, 0f, 0f, bgAlpha);
		shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		
		// Update visibility of buttons
		SnapshotArray<Actor> children = table.getChildren();
		final float buttonAlpha = MathUtils.clamp((elapsedTime-DELAY)/(TRANSITION_TIME-DELAY),0,1);
		for (Actor child : children) {
			Color c = child.getColor();
			c.a = buttonAlpha;
		}
		for (Actor child : stars.getChildren()) {
			Color c = child.getColor();
			c.a = buttonAlpha;
		}
		
		// Update the actors
		if (buttonAlpha > VISIBILITY_THRESHOLD) {
			stage.act(delta);
		}
		
		// Draw the stage
		stage.draw();
		
		elapsedTime += delta;
	}

	private void backToMainMenu() {
		if (Skydiver3D.DEV_MODE) {
			Gdx.app.log(Skydiver3D.LOG, "Back to main menu");
		}
		
		game.playingScreen.restartLevel();
		game.setScreen(game.mainMenuScreen);
	}

	private void nextLevel() { //TODO make this a transition screen that shows next level
		if (Skydiver3D.DEV_MODE) {
			Gdx.app.log(Skydiver3D.LOG, "Creating Next level");
		}
		
		game.playingScreen.nextLevel();
		game.setScreen(game.playingScreen);
	}

	private void restartLevel() {
		if (Skydiver3D.DEV_MODE) {
			Gdx.app.log(Skydiver3D.LOG, "Restarting level");
		}
		
		game.playingScreen.restartLevel();
		game.setScreen(game.playingScreen);
	}
	
	private void quitGame() {
		if (Skydiver3D.DEV_MODE) {
			Gdx.app.log(Skydiver3D.LOG, "Quitting game");
		}
		
		game.exit();
	}

	@Override
	protected void showScreen() {	
		AssetFactory assetFactory = AssetFactory.getInstance();
		LanguageFactory lang = LanguageFactory.getInstance();
		goldTextureDrawable = new TextureRegionDrawable(new TextureRegion(assetFactory.get(TextureType.GOLD_STAR, Texture.class))); //TODO load with asset loader
		emptyTextureDrawable = new TextureRegionDrawable(new TextureRegion(assetFactory.get(TextureType.EMPTY_STAR, Texture.class)));
		
		Score score = game.playingScreen.scoreSummary();
		
		ringScore.setText(lang.RING_SCORE + ": " + score.ringScore);
		parachutingScore.setText(lang.PARACHUTING_SCORE + ": " + score.parachutingScore);
		landingScore.setText(lang.LANDING_SCORE + ": " + score.landingScore);
		totalScore.setText(lang.TOTAL_SCORE + ": " + score.totalScore);
		
		SnapshotArray<Actor> children = stars.getChildren();
		
		for (int i = 0; i < 5; i++) {
			Drawable drawable = i < score.rating ? goldTextureDrawable : emptyTextureDrawable;	
			Image image = (Image)children.get(i);
			image.setDrawable(drawable);
		}
		
		elapsedTime = 0f;
		
		AchievementsFactory achievements = AchievementsFactory.getInstance();
		int rating = achievements.getLevelRating(game.playingScreen.level());
		if (score.rating > rating) {
		    achievements.setLevelRating(game.playingScreen.level(), score.rating);
		}
		
		int levels = achievements.levelsCompleted();
		if (game.playingScreen.level().index() >= levels) {
			 achievements.setLevelsCompleted(game.playingScreen.level());
		}
	}

	@Override
	protected void pauseScreen() {
		
	}

	@Override
	protected void resumeScreen() {
		
	}

	@Override
	protected void hideScreen() {
		
	}

	@Override
	protected void disposeScreen() {
		
	}

}
