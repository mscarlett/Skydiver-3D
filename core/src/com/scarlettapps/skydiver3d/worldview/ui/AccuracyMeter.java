// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.worldview.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.scarlettapps.skydiver3d.DefaultScreen;
import com.scarlettapps.skydiver3d.resources.AssetFactory;
import com.scarlettapps.skydiver3d.resources.AssetFactory.TextureType;

public class AccuracyMeter extends Actor { //TODO This needs to be resolution independent

	private Image slider;
	private Image bar;
	private float barX;
	private float barVelocity = 200;
	private boolean right = true;
	
	public AccuracyMeter() {
		AssetFactory assetFactory = AssetFactory.getInstance();
		Texture sliderTex = assetFactory.get(TextureType.SLIDER, Texture.class);
		Texture barTex = assetFactory.get(TextureType.SLIDERBAR, Texture.class);
		slider = new Image(new Sprite(sliderTex));
		bar = new Image(new Sprite(barTex));
		bar.setScale(0.5f, 1);
		slider.setPosition(DefaultScreen.VIRTUAL_WIDTH/2-slider.getWidth()/2,DefaultScreen.VIRTUAL_HEIGHT/2-slider.getHeight()/2-75);
		bar.setPosition(slider.getX()-bar.getWidth()/2,slider.getY()+(slider.getHeight()-bar.getHeight())/2);
		barX = bar.getX();
	}
	
	@Override
	public void act(float delta) {
		float minX = slider.getX();
		float maxX = minX + slider.getWidth();
		float barPos = barX-(minX-bar.getWidth()/2);
		if (right) {
			if (minX+barPos>maxX) {
				right = false;
			} else {
				barX=barX+barVelocity*delta;
			}
		} else {
			if (barPos<=0) {
				right = true;
			} else {
				barX=barX-barVelocity*delta;
			}
		}
		bar.setX(barX);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		slider.draw(batch, parentAlpha);
		bar.draw(batch, parentAlpha);
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
