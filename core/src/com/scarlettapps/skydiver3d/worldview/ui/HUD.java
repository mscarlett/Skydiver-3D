package com.scarlettapps.skydiver3d.worldview.ui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class HUD {
	
	private Stage stage;
	private Group initial;
	private Group main;
	private Group parachuting;
	private Group landed;
	
	public HUD() {
		initial = new Group();
		Table table = new Table();
		stage.addActor(initial);
	}

}
