package com.scarlettapps.skydiver3d;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.scarlettapps.skydiver3d.resources.AssetFactory;
import com.scarlettapps.skydiver3d.world.Sky;
import com.scarlettapps.skydiver3d.worldstate.Status;

public class SkyUI implements ApplicationListener {

	Sky sky;
	Batch batch;
	
	@Override
	public void create () {
		sky = new Sky(new Status());
		AssetFactory factory = AssetFactory.getInstance();
		factory.load();
		factory.finishLoading();
		sky.initialize();
		
		batch = new SpriteBatch();
		batch.getProjectionMatrix().setToOrtho2D(0, 0, DefaultScreen.VIRTUAL_WIDTH, DefaultScreen.VIRTUAL_HEIGHT);
	}
	
	@Override
	public void dispose () {
		
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(242 / 255.0f, 210 / 255.0f, 111 / 255.0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		float delta = Gdx.graphics.getDeltaTime();
		sky.update(delta);
		
		batch.begin();
		sky.render(batch);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
}