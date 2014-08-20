// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class MenuScreen extends DefaultScreen<SkyDiver3D> {

	private static final String BACKGROUND_FILE = "data/textures/Menu3Background.png";
	private static final String SKIN_FILE = "skin/uiskin.json";
	
	protected final Stage stage;
	protected final Table table;
	protected final Skin skin;
	
	public MenuScreen(SkyDiver3D game) {
		this(game, true);
	}
	
	public MenuScreen(SkyDiver3D game, boolean useDefaultBackground) {
		super(game);
		
		// Initialize stage and skin
		Viewport viewport = new ScalingViewport(Scaling.fill, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
		stage = new Stage(viewport);
		skin = new Skin(Gdx.files.internal(SKIN_FILE));
		
		// Set default font to use linear texture filter
		BitmapFont font = skin.getFont("default-font");
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		// Initialize table and add to stage
		table = new Table(skin);
        table.setFillParent(true);
        stage.addActor(table);
        
        // Add default background if we should use it
        if (useDefaultBackground) {
        	Texture background = new Texture(BACKGROUND_FILE);
        	background.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        	table.setBackground(new TextureRegionDrawable(new TextureRegion(background)));
        }
        
        // Turn on debug lines if we are in dev mode
        if (SkyDiver3D.DEV_MODE) {
        	table.debug();
        }
	}
	
	@Override
	public void render(float delta) {
		// Update the actors
		stage.act(delta);
		
		// Clear screen with black
		Gdx.gl.glClearColor( 0f, 0f, 0f, 1f );
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		// Draw the stage
		stage.draw();
		
		// Draw debug lines if we are in dev mode
		if (SkyDiver3D.DEV_MODE) {
			Table.drawDebug(stage);
		}
	}
	
	@Override
	protected InputProcessor getInputProcessor() {
		return stage;
	}

}
