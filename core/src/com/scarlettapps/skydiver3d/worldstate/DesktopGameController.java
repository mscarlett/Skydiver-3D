// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.worldstate;

import com.badlogic.gdx.Input.Keys;

public class DesktopGameController extends GameController {

	private static final float VX = 10;
	private static final float VY = 10;

	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;
	
	@Override
	public void reset() {
		super.reset();
		left = false;
		right = false;
		up = false;
		down = false;
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Keys.RIGHT:
			right = true;
			break;
		case Keys.LEFT:
			left = true;
			break;
		case Keys.UP:
			up = true;
			break;
		case Keys.DOWN:
			down = true;
			break;
		case Keys.A:
			faster = true;
			break;
		}
		ax = (right ? VX : 0) - (left ? VX : 0);
		ay = (up ? VY : 0) - (down ? VY : 0);
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
		case Keys.RIGHT:
			right = false;
			break;
		case Keys.LEFT:
			left = false;
			break;
		case Keys.UP:
			up = false;
			break;
		case Keys.DOWN:
			down = false;
			break;
		case Keys.A:
			faster = false;
			break;
		}
		ax = (right ? VX : 0) - (left ? VX : 0);
		ay = (up ? VY : 0) - (down ? VY : 0);
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		super.touchDown(screenX, screenY, pointer, button);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		super.touchUp(screenX, screenY, pointer, button);
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	
	@Override
	public void update(float delta) {
		
	}

}
