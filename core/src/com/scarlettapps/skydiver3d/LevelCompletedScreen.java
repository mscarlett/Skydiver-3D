// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.SnapshotArray;
import com.scarlettapps.skydiver3d.resources.Graphics;

public class LevelCompletedScreen extends MenuScreen {
	
	private static final String GOLD_STAR = "data/goldstar.png";
	private static final String EMPTY_STAR = "data/emptystar.png";
	private final Group stars;
	private final Drawable goldTextureDrawable;
	private final Drawable emptyTextureDrawable;
	
	public LevelCompletedScreen(SkyDiver3D game) { //TODO rate how well the user did using stars animation
		super(game, false);
		
		stars = new Group();
		
		goldTextureDrawable = new TextureRegionDrawable(new TextureRegion(Graphics.get(GOLD_STAR, Texture.class))); //TODO load with asset loader
		emptyTextureDrawable = new TextureRegionDrawable(new TextureRegion(Graphics.get(EMPTY_STAR, Texture.class)));
		
		final int y = 350;
		final int size = 75;
		final int offset = 25;
		int x = DefaultScreen.VIRTUAL_WIDTH/2-size/2-2*size-2*offset;
		
		// Add stars to stage
		for (int i = 0; i < 5; i++) {
			Image image = new Image();
			image.setBounds(x, y, size, size);
			stars.addActor(image);
			x += size + offset;
		}
		
		stage.addActor(stars);
		
		table.align(Align.bottom);
		
		// register the button "Next Level"
		TextButton nextLevelButton = new TextButton("Next Level", skin);
		nextLevelButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				nextLevel();
			}
		});
		table.add(nextLevelButton).size(300, 60).uniform().spaceBottom(10);
		table.row();

		// register the button "Try Again"
		TextButton tryAgainButton = new TextButton("Try Again", skin);
		tryAgainButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				restartLevel();
			}

		});
		table.add(tryAgainButton).uniform().fill().spaceBottom(10);
		table.row();

		// register the button "Main MEnu"
		TextButton optionsButton = new TextButton("Main Menu", skin);
		optionsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				backToMainMenu();
			}
		});
		table.add(optionsButton).uniform().fill().spaceBottom(10);
		table.row();

		// register the button "Quit"
		TextButton quitButton = new TextButton("Quit", skin);
		quitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				quitGame();
			}
		});
		table.add(quitButton).uniform().fill();
		
		table.padBottom(50);
		
		shapeRenderer = new ShapeRenderer();
	}

	private ShapeRenderer shapeRenderer;
	
	private float elapsedTime;
	private static final float MAX_ALPHA = 0.8f;
	private static final float TRANSITION_TIME = 5f;
	private static final float DELAY = 2f;
	private static final float VISIBILITY_THRESHOLD = 0.3f;
	
	@Override
	public void render(float delta) {
		// Clear screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		// Update and draw world
		game.playingScreen.updateWorld(delta);
		game.playingScreen.renderWorld(delta);
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.begin(ShapeType.Filled);
		final float bgAlpha = Math.min(elapsedTime/(TRANSITION_TIME/MAX_ALPHA),MAX_ALPHA);
		shapeRenderer.setColor(0f, 0f, 0f, bgAlpha);
		shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		
		game.playingScreen.renderScore();
		
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
			
		// Draw debug lines if we are in dev mode
		if (SkyDiver3D.DEV_MODE) {
			Table.drawDebug(stage);
		}
		
		
		elapsedTime += delta;
	}

	private void backToMainMenu() {
		if (SkyDiver3D.DEV_MODE) {
			Gdx.app.log(SkyDiver3D.LOG, "");
		}
		
		game.playingScreen.restartLevel();
		game.setScreen(game.mainMenuScreen);
	}

	private void nextLevel() {
		if (SkyDiver3D.DEV_MODE) {
			Gdx.app.log(SkyDiver3D.LOG, "Creating Next level");
		}
		
		game.playingScreen.nextLevel();
		game.setScreen(game.playingScreen);
	}

	private void restartLevel() {
		if (SkyDiver3D.DEV_MODE) {
			Gdx.app.log(SkyDiver3D.LOG, "Restarting level");
		}
		
		game.playingScreen.restartLevel();
		game.setScreen(game.playingScreen);
	}
	
	private void quitGame() {
		if (SkyDiver3D.DEV_MODE) {
			Gdx.app.log(SkyDiver3D.LOG, "Quitting game");
		}
		
		game.exit();
	}

	@Override
	protected void showScreen() {
		final int rating = game.playingScreen.getRating();
		
		SnapshotArray<Actor> children = stars.getChildren();
		
		for (int i = 0; i < 5; i++) {
			Drawable drawable = i < rating ? goldTextureDrawable : emptyTextureDrawable;	
			Image image = (Image)children.get(i);
			image.setDrawable(drawable);
		}
		
		elapsedTime = 0f;
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
