// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.worldview;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.scarlettapps.skydiver3d.DefaultScreen;
import com.scarlettapps.skydiver3d.resources.Graphics;
import com.scarlettapps.skydiver3d.world.Skydiver;
import com.scarlettapps.skydiver3d.world.gamestate.StatusManager;
import com.scarlettapps.skydiver3d.worldview.ui.AccuracyMeter;
import com.scarlettapps.skydiver3d.worldview.ui.CenterFont;

public class StatusView {

	// Stage stage;
	// Table table;

	private final SpriteBatch hudBatch;

	final CenterFont font;
	private final CenterFont fontOutline;

	private final Sprite pauseIcon;
	
	private final SpriteBatch fogBatch;
	
	private final StatusManager statusManager;
	
	private final AccuracyMeter accuracyMeter;

	public StatusView(StatusManager statusManager) {
		// stage = new Stage();
		// table = new Table();
		// stage.addActor(table);
		this.statusManager = statusManager;

		hudBatch = new SpriteBatch();

		fontOutline = new CenterFont(
				Gdx.files.internal("data/comic_sans_modified_4.fnt"), false);
		fontOutline.setColor(Color.BLACK);
		font = new CenterFont(
				Gdx.files.internal("data/comic_sans_modified_5.fnt"), false);
		font.setColor(Color.WHITE);
		font.setScale(0.5f * DefaultScreen.width() / 480f,
				0.5f * DefaultScreen.height() / 360f);
		fontOutline.setScale(0.5f * DefaultScreen.width() / 480f,
				0.5f * DefaultScreen.height() / 360f);

		pauseIcon = new Sprite(Graphics.get("data/pause2.png", Texture.class));
		pauseIcon.setPosition(860, 600);
		
		accuracyMeter = new AccuracyMeter();
		
		fogBatch = new SpriteBatch();
	}

	public void update(float delta) {
		// stage.act(delta);
	}

	public void render(float delta) {
		// stage.draw();
	}
	
	public void drawJumpOffPlane(Skydiver skydiver) {
		hudBatch.begin();
		if (skydiver.jumpedOffAirplane) {
			if (skydiver.timeSinceJumpedOffAirplane > 2f) {
				font.setColor(Color.WHITE);
				fontOutline.draw(hudBatch, "Get ready!", DefaultScreen.width()/2, DefaultScreen.height()/2);
				font.draw(hudBatch, "Get ready!", DefaultScreen.width()/2, DefaultScreen.height()/2);
			}
		} else {
			fontOutline.draw(hudBatch, "Tap screen to jump off plane", DefaultScreen.width()/2, DefaultScreen.height()/2);
			font.draw(hudBatch, "Tap screen to jump off plane", DefaultScreen.width()/2, DefaultScreen.height()/2);
		}
		hudBatch.end();
	}
	
	public void drawCollected(PerspectiveCamera cam, float delta) {
		if (statusManager.collected()) {
			float displayScoreTime = statusManager.displayScoreTime();
			if (displayScoreTime < 1) {
				Vector3 intersectPoint = statusManager.intersectPoint();
				if (displayScoreTime == 0) {
					intersectPoint.set(statusManager.position());
					cam.project(intersectPoint);
					statusManager.addToScore(1000);
				}
				fogBatch.begin();
        		font.setColor(0, 0, 1, (1-(float)Math.sqrt(displayScoreTime))/2f);
        		font.setScale((1+2*displayScoreTime)*DefaultScreen.width()/480f, (1+2*displayScoreTime)*DefaultScreen.height()/360f);
	        	font.draw(fogBatch,"1000",intersectPoint.x+DefaultScreen.width()/8,intersectPoint.y+DefaultScreen.width()/8);
	        	font.setColor(Color.WHITE);
	        	font.setScale(0.5f*DefaultScreen.width()/480f, 0.5f*DefaultScreen.height()/360f);
	        	statusManager.addToDisplayScoreTime(delta);
	        	fogBatch.end();
        	} else {
        		statusManager.setDisplayScoreTime(0);
        		statusManager.setCollected(false);
        	}
		}
	}
	
	String successString;
	
	public void drawParachuteCaption(Skydiver skydiver, float delta) {
		fogBatch.begin();
		accuracyMeter.render(fogBatch);
		if (!statusManager.parachuteDeployed()) {
			font.draw(fogBatch,"Touch screen to open parachute",DefaultScreen.width()/2,120/320f*DefaultScreen.height());
		}
		if (statusManager.justOpenedParachute()) {
			if (!statusManager.parachuteDeployed()) {
				skydiver.deployParachute();
				statusManager.setParachuteDeployed(true);
				
				int success = (int)(statusManager.getAccuracy()*100+0.5f);
				
				if (success < 10) {
					successString = "Broken Chute";
				} else if (success < 20) {
					successString = "Crash Landing!!!";
				} else if (success < 30) {
					successString = "Poor Reflexes";
				} else if (success < 40) {
					successString = "Sloppy Timing";
				} else if (success < 50) {
					successString = "OK Timing";
				} else if (success < 60) {
					successString = "Average Timing";
				} else if (success < 70) {
					successString = "Good Timing";
				} else if (success < 80) {
					successString = "Great Timing!";
				} else if (success < 90) {
					successString = "Nice Timing!";
				} else if (success < 97) {
					successString = "Perfect Timing!";
				} else {
					successString = "Incredible Timing!!!";
				}
			}
			
			statusManager.velocity().z -= 15*Math.signum(statusManager.velocity().z+30)*delta;
			
        	font.draw(fogBatch,successString,DefaultScreen.width()/2,100/320f*DefaultScreen.height());
        	font.draw(fogBatch,"Get ready to land...",DefaultScreen.width()/2,70/320f*DefaultScreen.height());
		}
		fogBatch.end();
	}
	
	public void drawHud() {
		hudBatch.begin();
        fontOutline.drawTopLeft(hudBatch, "Points: " + statusManager.score(),20,10);
        fontOutline.drawTopCenter(hudBatch, "Speed: " + ((int)(statusManager.velocity().z*-2.23694)) + " mph",-15,10);
        fontOutline.drawTopRight(hudBatch, "Altitude: " + ((int)(statusManager.position().z*3.28084f)) + " feet",0,10);
        font.drawTopLeft(hudBatch, "Points: " + statusManager.score(),20,0);
        font.drawTopCenter(hudBatch, "Speed: " + ((int)(statusManager.velocity().z*-2.23694)) + " mph",-15,0);
        font.drawTopRight(hudBatch, "Altitude: " + ((int)(statusManager.position().z*3.28084f)) + " feet");
        //pauseIcon.draw(hudBatch);
        hudBatch.end();
	}
	
	public void renderScore() {
		final int bonus = statusManager.landingBonus();
		final int timeBonus = statusManager.timeBonus();
		fogBatch.begin();
		font.draw(fogBatch,"Ring Score: " + statusManager.score(),DefaultScreen.width()/2,300/320f*DefaultScreen.height());
		font.draw(fogBatch,"Speed Bonus: " + timeBonus,DefaultScreen.width()/2,280/320f*DefaultScreen.height());
		font.draw(fogBatch,"Landing Bonus: " + bonus,DefaultScreen.width()/2,260/320f*DefaultScreen.height());
		font.draw(fogBatch,"Total Score: " + (statusManager.score()+bonus+timeBonus),DefaultScreen.width()/2,240/320f*DefaultScreen.height());
		fogBatch.end();
	}

	public AccuracyMeter getAccuracyMeter() {
		return accuracyMeter;
	}

}
