// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.worldview.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.scarlettapps.skydiver3d.DefaultScreen;
import com.scarlettapps.skydiver3d.world.gamestate.StatusManager;

public class StatusView {
	
	private final Stage stage;
	private final Table initial;
	private final Table hud;
	private final Table parachuting;
	private final Table landing;
	private final StatusManager statusManager;
	
	public StatusView(StatusManager statusManager) {
		Viewport viewport = new ScalingViewport(Scaling.fill, DefaultScreen.VIRTUAL_WIDTH, DefaultScreen.VIRTUAL_HEIGHT);
		stage = new Stage(viewport);
		this.statusManager = statusManager;
		initial = new Table();
		initial.align(Align.center);
		initial.add("Tap screen to jump");
		hud = new Table();
		hud.align(Align.top);
		parachuting = new Table();
		parachuting.align(Align.center);
		parachuting.add("Tap screen to open parachute");
		landing = new Table();
		landing.align(Align.center);
		landing.add("Get ready to land");
	}
	
	protected void  loadAssets() {
		
	}
	
	private Color white = Color.WHITE.cpy();
	private Color fontColor = white.cpy();
	private Color tmp = new Color();
	
	float elapsedTime = 0.5f;

	public void update(float delta) {

	}

	public void render(float delta) {
		stage.act(delta);
		stage.draw();
	}
	
	
	}
