package com.scarlettapps.skydiver3d.worldview.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.scarlettapps.skydiver3d.DefaultScreen;
import com.scarlettapps.skydiver3d.worldstate.Status;

public class HUD {
	
	private Group hud;
	
	public HUD(Skin skin, final Status status) {
		Label label;
		
		hud = new Group();
		hud.setVisible(false);
		label = new Label("Points: 0", skin);
		TextBounds bounds = label.getTextBounds();
		label.setPosition(10, DefaultScreen.VIRTUAL_HEIGHT*0.975f-bounds.height);
		label.setColor(Color.WHITE);
		label.addAction(new Action() {

			@Override
			public boolean act(float delta) {
				Label label = (Label)getActor();
				int points = status.getScore();
				label.setText("Points: " + points);
				return false;
			}
			
		});
		hud.addActor(label);
		
		label = new Label("Speed: 000 mph", skin);
		bounds = label.getTextBounds();
		label.setPosition(9*DefaultScreen.VIRTUAL_WIDTH/20-bounds.width/2, DefaultScreen.VIRTUAL_HEIGHT*0.975f-bounds.height);
		label.setColor(Color.WHITE);
		label.addAction(new Action() {

			@Override
			public boolean act(float delta) {
				Label label = (Label)getActor();
				int speed = Math.round(-2.23694f*status.velocity().z);
				label.setText("Speed: " + speed + " mph");
				return false;
			}
			
		});
		hud.addActor(label);
		
		label = new Label("Altitude: 00000 feet", skin);
		bounds = label.getTextBounds();
		label.setPosition(DefaultScreen.VIRTUAL_WIDTH-bounds.width, DefaultScreen.VIRTUAL_HEIGHT*0.975f-bounds.height);
		label.setColor(Color.WHITE);
		label.addAction(new Action() {

			@Override
			public boolean act(float delta) {
				Label label = (Label)getActor();
				int altitude = Math.round(3.28084f*status.position().z);
				label.setText("Altitude: " + altitude + " feet");
				return false;
			}
			
		});
		hud.addActor(label);
	}
	
	public Group getGroup() {
		return hud;
	}

}
