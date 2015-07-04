package com.scarlettapps.skydiver3d.worldview.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.scarlettapps.skydiver3d.DefaultScreen;
import com.scarlettapps.skydiver3d.resources.LanguageFactory;
import com.scarlettapps.skydiver3d.resources.PreferenceFactory;
import com.scarlettapps.skydiver3d.worldstate.Status;

public class HUD {
	
	private Group hud;
	
	public HUD(Skin skin, final Status status) {
		final boolean useMetric = PreferenceFactory.getInstance().useMetric();
		final LanguageFactory lang = LanguageFactory.getInstance();
		
		Label label;
		
		hud = new Group();
		hud.setVisible(false);
		label = new Label(lang.POINTS + ": 0", skin);
		GlyphLayout bounds = new GlyphLayout(label.getStyle().font, label.getText());
		label.setPosition(10, DefaultScreen.VIRTUAL_HEIGHT*0.975f-bounds.height);
		label.setColor(Color.WHITE);
		label.addAction(new Action() {

			@Override
			public boolean act(float delta) {
				Label label = (Label)getActor();
				int points = status.getScore();
				label.setText(lang.POINTS + ": " + points);
				return false;
			}
			
		});
		hud.addActor(label);
		
		label = new Label(lang.SPEED + ": 000 mph", skin);
		bounds = new GlyphLayout(label.getStyle().font, label.getText());
		label.setPosition(9*DefaultScreen.VIRTUAL_WIDTH/20-bounds.width/2, DefaultScreen.VIRTUAL_HEIGHT*0.975f-bounds.height);
		label.setColor(Color.WHITE);
		label.addAction(new Action() {

			@Override
			public boolean act(float delta) {
				Label label = (Label)getActor();
				if (useMetric) {
					int speed = Math.round(-3.6f*status.velocity().z);
				    label.setText(lang.SPEED + ": " + speed + " kmh");
				} else {
				    int speed = Math.round(-2.23694f*status.velocity().z);
				    label.setText(lang.SPEED + ": " + speed + " mph");
				}
				return false;
			}
			
		});
		hud.addActor(label);
		
		label = new Label(lang.ALTITUDE + ": 00000 feet", skin);
		bounds = new GlyphLayout(label.getStyle().font, label.getText());
		label.setPosition(DefaultScreen.VIRTUAL_WIDTH-bounds.width, DefaultScreen.VIRTUAL_HEIGHT*0.975f-bounds.height);
		label.setColor(Color.WHITE);
		label.addAction(new Action() {

			@Override
			public boolean act(float delta) {
				Label label = (Label)getActor();
				if (useMetric) {
					int altitude = Math.round(status.position().z);
				    label.setText(lang.ALTITUDE + ": " + altitude + " m");
				} else {
				    int altitude = Math.round(3.28084f*status.position().z);
				    label.setText(lang.ALTITUDE + ": " + altitude + " feet");
				}
				return false;
			}
			
		});
		hud.addActor(label);
	}
	
	public Group getGroup() {
		return hud;
	}

	
	
}
