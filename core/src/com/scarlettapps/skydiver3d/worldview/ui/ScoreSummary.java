package com.scarlettapps.skydiver3d.worldview.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.scarlettapps.skydiver3d.DefaultScreen;

public class ScoreSummary {
	
	private final Group scoreSummary;

	public ScoreSummary(Skin skin) {
		scoreSummary = new Group();
		scoreSummary.setVisible(false);
		Label label = new Label("Ring Score: 0", skin);
		label.setX(DefaultScreen.VIRTUAL_WIDTH/2);
		label.setY(300/320f*DefaultScreen.VIRTUAL_HEIGHT);
		label.setColor(Color.WHITE);
		scoreSummary.addActor(label);
		label = new Label("Speed Bonus: 0", skin);
		label.setX(DefaultScreen.VIRTUAL_WIDTH/2);
		label.setY(280/320f*DefaultScreen.VIRTUAL_HEIGHT);
		label.setColor(Color.WHITE);
		scoreSummary.addActor(label);
		label = new Label("Landing Bonus: 0", skin);
		label.setX(DefaultScreen.VIRTUAL_WIDTH/2);
		label.setY(260/320f*DefaultScreen.VIRTUAL_HEIGHT);
		label.setColor(Color.WHITE);
		label = new Label("Total Bonus: 0", skin);
		label.setX(DefaultScreen.VIRTUAL_WIDTH/2);
		label.setY(240/320f*DefaultScreen.VIRTUAL_HEIGHT);
		label.setColor(Color.WHITE);
		scoreSummary.addActor(label);
	}
	
	public Group getGroup() {
		return scoreSummary;
	}
	
}
