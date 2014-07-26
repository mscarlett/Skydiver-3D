// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.worldview.ui;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.scarlettapps.skydiver3d.DefaultScreen;

public class CenterFont extends BitmapFont {
	
	private final TextBounds bounds;
			
	public CenterFont(FileHandle internal, boolean b) {
		super(internal,b);
		bounds = new TextBounds();
	}

	@Override
	public TextBounds draw(Batch batch, CharSequence str, float width, float height) {
		getBounds(str, bounds);
		return super.draw(batch, str, width-bounds.width/2, height-bounds.height/2);
	}
	
	public void drawTopCenter(Batch batch, CharSequence str) {
		drawTopCenter(batch,str,0,0);
	}
	
	public void drawTopCenter(Batch batch, CharSequence str, float xoffset, float yoffset) {
		getBounds(str, bounds);
		super.draw(batch, str, DefaultScreen.width()/2-bounds.width/2+xoffset, DefaultScreen.height()-bounds.height+yoffset);
	}
	
	public void drawTopLeft(Batch batch, CharSequence str) {
		drawTopLeft(batch,str,0,0);
	}
	
	public void drawTopLeft(Batch batch, CharSequence str, float xoffset, float yoffset) {
		getBounds(str, bounds);
		super.draw(batch, str, xoffset, DefaultScreen.height()-bounds.height+yoffset);
	}
	
	public void drawTopRight(SpriteBatch batch, CharSequence str) {
		drawTopRight(batch,str,0,0);
	}
	
	public void drawTopRight(SpriteBatch batch, CharSequence str, float xoffset, float yoffset) {
		getBounds(str, bounds);
		super.draw(batch, str, DefaultScreen.width()-bounds.width+xoffset, DefaultScreen.height()-bounds.height+yoffset);
	}
}