// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class TransitionScreen implements Screen {

	private final Game game;
	private final Screen current;
	private final Screen next;
	private final TransitionEffect transitionEffect;

	public TransitionScreen(Game game, Screen current, Screen next, float duration) {
		this.current = current;
		this.next = next;
		this.transitionEffect = new FadeOutTransitionEffect(duration);
		this.game = game;
	}

	@Override
	public void render(float delta) {
		transitionEffect.update(delta);
		transitionEffect.render(current, next, delta);

		if (transitionEffect.isFinished()) {
			game.setScreen(next);
		}
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

	public static abstract class TransitionEffect {

		protected final float duration;
		protected float elapsedTime;

		public TransitionEffect(float duration) {
			this.duration = duration;
		}

		public void update(float delta) {
			elapsedTime += delta;
		}

		abstract void render(Screen current, Screen next, float delta);

		public final boolean isFinished() {
			return elapsedTime >= duration;
		}
	}

	public static class FadeOutTransitionEffect extends TransitionEffect {

		private static final ShapeRenderer shapeRenderer = new ShapeRenderer();

		public FadeOutTransitionEffect(float duration) {
			super(duration);
		}

		@Override
		public void render(Screen current, Screen next, float delta) {
			// Draw current screen
			current.render(delta);
			// Draw a quad over the screen using the color
			Gdx.gl.glEnable(GL20.GL_BLEND);
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(0f, 0f, 0f, getAlpha());
			shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			shapeRenderer.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);
		}

		private float getAlpha() {
			return elapsedTime / duration;
		}

	}
}