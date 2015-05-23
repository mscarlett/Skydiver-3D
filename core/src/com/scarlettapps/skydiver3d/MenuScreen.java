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
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Represents a menu screen which contains buttons for navigating to other screens
 * @author Michael Scarlett
 *
 */
public abstract class MenuScreen extends DefaultScreen<Skydiver3D> {
    /* Resource locations */
	private static final String BACKGROUND_FILE = "data/textures/Menu3Background.png";
	private static final String SKIN_FILE = "skin/uiskin.json";
	
	/* Store resources to speed up screen loading */
	// Default background
	private static TextureRegionDrawable defaultBackground = null;
	// Default skin
	private static Skin defaultSkin = null;
	// Default viewport
	private static Viewport defaultViewport = null;
	
	// Renders UI objects
	protected Stage stage;
	// UI object to which other objects are added
	protected Table table;
	// Represents the style of UI objects
	protected Skin skin;
	// Used for displaying screen
	protected Viewport viewport;
	
	// Whether to use menu background theme
	private final boolean useDefaultBackground;
	
	public MenuScreen(Skydiver3D game) {
		this(game, true);
	}
	
	public MenuScreen(Skydiver3D game, boolean useDefaultBackground) {
		super(game);
		this.useDefaultBackground = useDefaultBackground;
	}
	
	@Override
	public void initializeScreen() {
		// Initialize stage, skin, and table
		viewport = getDefaultViewport();
		stage = new Stage(viewport);
		
		skin = getDefaultSkin();
		table = new Table(skin);
		table.setFillParent(true);
		
		stage.addActor(table);
		        
		// Add default background if we should use it
		if (useDefaultBackground) {
			table.setBackground(getDefaultBackground());
		}
		        
		// Draw debug lines if we are in dev mode
		if (Skydiver3D.DEV_MODE) {
			stage.setDebugAll(true);
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
	}
	
	@Override
	protected InputProcessor getInputProcessor() {
		return stage;
	}
	
	@Override
	protected void resizeScreen(int width, int height) {
		viewport.update(width, height);
	}
		
	protected static Skin getDefaultSkin() {
		if (defaultSkin == null) {
			defaultSkin = new Skin(Gdx.files.internal(SKIN_FILE));
			// Set default font to use linear texture filter
			BitmapFont font = defaultSkin.getFont("default-font");
			font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		return defaultSkin;
	}
	
	protected static TextureRegionDrawable getDefaultBackground() {
		if (defaultBackground == null) {
			Texture background = new Texture(BACKGROUND_FILE);
			background.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultBackground = new TextureRegionDrawable(new TextureRegion(background));
		}
		return defaultBackground;
	}
	
	protected static Viewport getDefaultViewport() {
		if (defaultViewport == null) {
		    defaultViewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
		}
		return defaultViewport;
	}

}
