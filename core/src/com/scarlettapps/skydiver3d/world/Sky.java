package com.scarlettapps.skydiver3d.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.scarlettapps.skydiver3d.DefaultScreen;
import com.scarlettapps.skydiver3d.resources.AssetFactory;
import com.scarlettapps.skydiver3d.resources.AssetFactory.TextureType;
import com.scarlettapps.skydiver3d.worldstate.Status;
import com.scarlettapps.skydiver3d.worldstate.WorldState;
import com.scarlettapps.skydiver3d.worldview.Renderer;

public class Sky extends GameObject {
	
	private static final float VELOCITY = 20;
	
	private TextureRegion sky;
	private TextureRegion sky2;
	
	private float offsetX;
	private float offsetX2;
	
	private final Status status;

	public Sky(Status status) {
		super(true, true);
		
		this.status = status;
	}
	
	public void initialize() {
		Texture texture = AssetFactory.getInstance().get(TextureType.SKY, Texture.class);
		sky = new TextureRegion(texture);
		sky2 = new TextureRegion(sky);
		sky2.flip(true, false);
		
		reset();
	}

	@Override
	protected void updateObject(float delta) {
		float dx = VELOCITY*delta;
		offsetX -= dx;
		offsetX2 -= dx;
		if (offsetX < -DefaultScreen.VIRTUAL_WIDTH) {
			offsetX = DefaultScreen.VIRTUAL_WIDTH;
		}
		if (offsetX2 < -DefaultScreen.VIRTUAL_WIDTH) {
			offsetX2 = DefaultScreen.VIRTUAL_WIDTH;
		}
		
		if (status.jumpedOffAirplane()) {
			int height = sky.getRegionHeight();
			height *= 1 + 0.1f*delta;
			sky.setRegionHeight(height);
			sky2.setRegionHeight(height);
		}
	}

	@Override
	protected void renderObject(Renderer renderer) {
		
	}

	@Override
	public void onWorldStateChanged(WorldState worldState) {
		
	}

	@Override
	public void reset() {
		sky.setRegionWidth(DefaultScreen.VIRTUAL_WIDTH+1);
		sky.setRegionHeight(DefaultScreen.VIRTUAL_HEIGHT);
		sky2.setRegionWidth(DefaultScreen.VIRTUAL_WIDTH+1);
		sky2.setRegionHeight(DefaultScreen.VIRTUAL_HEIGHT);
		
		offsetX = 0;
		offsetX2 = DefaultScreen.VIRTUAL_WIDTH;
	}

	public void render(Batch batch) {
		batch.draw(sky, offsetX, 0);
		batch.draw(sky2, offsetX2, 0);
	}
}
