// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.worldview.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.scarlettapps.skydiver3d.DefaultScreen;
import com.scarlettapps.skydiver3d.resources.Graphics;

public class AccuracyMeter { //TODO I can draw this manually, pixel by pixel but for debugging purposes use image

	Sprite slider;
	Sprite bar;
	boolean stopped = false;
	
	public AccuracyMeter() {
		Texture sliderTex = Graphics.get("data/slider2.png", Texture.class);
		Texture barTex = Graphics.get("data/sliderbar.png", Texture.class);
		slider = new Sprite(sliderTex);
		bar = new Sprite(barTex);
		bar.setScale(0.5f, 1);
		slider.setPosition(DefaultScreen.width()/2-slider.getWidth()/2,DefaultScreen.height()/2-slider.getHeight()/2-75);
		bar.setPosition(slider.getX()-bar.getWidth()/2,slider.getY()+(slider.getHeight()-bar.getHeight())/2);
		barX = bar.getX();
	}
	
	float barX;
	float barVelocity = 200;
	boolean right = true;
	float epsilon = 0;
	
	public void update(float delta) {
		float minX = slider.getX();
		float maxX = minX + slider.getWidth();
		float barPos = barX-(minX-bar.getWidth()/2);
		if (right) {
			if (minX+barPos>maxX) {
				right = false;
			} else {
				//barPos += barVelocity*delta;
				//bar.translateX(barVelocity*delta);
				barX=barX+barVelocity*delta;
				//bar.setBounds(bar.getX(), bar.getY(), bar.getWidth(), bar.getHeight());
			}
		} else {
			if (barPos<=0) {
				right = true;
			} else {
				//barPos -= barVelocity*delta;
				//bar.translateX(-barVelocity*delta);
				barX=barX-barVelocity*delta;
				//bar.setBounds(bar.getX(), bar.getY(), bar.getWidth(), bar.getHeight());
			}
		}
		//barX = bar.getX();
		bar.setX(barX);
	}
	
	public void render(SpriteBatch spriteBatch) {
		//bar.setX(barX);
		slider.draw(spriteBatch);
		//bar.setX(barX);
		bar.draw(spriteBatch);
		//bar.setX(barX);
	}

	public void stop() {
		barVelocity = 0;
	}
	
	public float getAccuracy() {
		float minX = slider.getX();
		float maxX = minX + slider.getWidth();
		float barPos = barX-(minX-bar.getWidth()/2);
		float percent = barPos/(maxX-minX);
		return 2*(percent < 0.5f? percent : 1-percent);
	}
}
