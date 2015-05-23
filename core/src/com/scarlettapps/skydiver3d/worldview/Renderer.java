

package com.scarlettapps.skydiver3d.worldview;

import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.SortedIntList.Node;
import com.scarlettapps.skydiver3d.DefaultScreen;
import com.scarlettapps.skydiver3d.Skydiver3D;
import com.scarlettapps.skydiver3d.world.Cloud;
import com.scarlettapps.skydiver3d.world.Collectible;
import com.scarlettapps.skydiver3d.world.Collectibles;
import com.scarlettapps.skydiver3d.world.Plane;
import com.scarlettapps.skydiver3d.world.Sky;
import com.scarlettapps.skydiver3d.world.Skydiver;
import com.scarlettapps.skydiver3d.world.Target;
import com.scarlettapps.skydiver3d.world.Terrain;
import com.scarlettapps.skydiver3d.world.World;
import com.scarlettapps.skydiver3d.worldstate.Status;
import com.scarlettapps.skydiver3d.worldstate.StatusManager;

public class Renderer {
	
	private WorldViewController controller;
	
	private PerspectiveCamera cam;
	private DecalBatch decalBatch;
	private ModelBatch modelBatch;
	private SpriteBatch spriteBatch;
	private World world;

	public Renderer(World world) {
		this.world = world;
	}
	
	public void initialize() {
		cam = new PerspectiveCamera(67, DefaultScreen.VIRTUAL_WIDTH, DefaultScreen.VIRTUAL_HEIGHT);
        
        decalBatch = new DecalBatch(new CameraGroupStrategy(cam, new Comparator<Decal>(){
			@Override
			public int compare(Decal decal1, Decal decal2) {
				return (int)Math.signum(decal1.getZ()-decal2.getZ());
			}
        }));
        
		modelBatch = new ModelBatch();
		
		spriteBatch = new SpriteBatch();
		spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, DefaultScreen.VIRTUAL_WIDTH, DefaultScreen.VIRTUAL_HEIGHT);
	}
	
	public void switchState(StatusManager statusManager, WorldView worldView) {
		String oldName;
		
		if (Skydiver3D.DEV_MODE) {
			oldName = controller == null ? null : controller.getClass().getSimpleName();
		}
		
		Status status = Status.getInstance();
		
		switch(status.getState()) {
			case FINAL:
				controller = new FinalStateController(worldView);
				break;
			case INITIAL:
				controller = new InitialStateController(worldView);
				break;
			case LANDING:
				controller = new LandingStateController(worldView);
				break;
			case PARACHUTING:
				controller = new ParachutingStateController(worldView);
				break;
			case SKYDIVING:
				controller = new SkydivingStateController(worldView);
				break;
			default:
				throw new GdxRuntimeException("Invalid World State");
		}
		
		if (Skydiver3D.DEV_MODE) {
			Gdx.app.log(Skydiver3D.LOG, "Switching controller from " + oldName + " to " + controller.getClass().getSimpleName());
		}
		
		controller.initialize();
	}
	
	public void update(float delta) {
		controller.update(delta);
	}
	
	public void render(float delta) {
		controller.render(delta);
	}
	
	public void drawTerrain() {
		Terrain terrain = world.getTerrain();
		terrain.render(cam);
	}
	
	public void drawTargetAndSkydiver() {
		modelBatch.begin(cam);
		Target target = world.getTarget();
		Skydiver skydiver = world.getSkydiver();
		target.render(decalBatch);
		decalBatch.flush();
		skydiver.render(modelBatch);
		modelBatch.end();
	}
	
	public void drawSkydiverAndPlane() {
		modelBatch.begin(cam);
		Skydiver skydiver = world.getSkydiver();
		Plane plane = world.getPlane();
		plane.render(modelBatch);
		skydiver.render(modelBatch);
		modelBatch.end();
	}
	
	public void drawCollectibles() {
		Target target = world.getTarget();
		target.render(decalBatch);
		Collectibles collectibles = world.getCollectibles();
		Array<Cloud> clouds = world.getClouds();
		for (Node<Collectible> node: collectibles) {
			decalBatch.add(node.value.getDecal());
		}
		for (Cloud c: clouds) {
			decalBatch.add(c.getDecal());
		}
		decalBatch.flush();
	}
	
	public void drawSky() {
		Sky sky = world.getSky();
		spriteBatch.begin();
		sky.render(spriteBatch);
		spriteBatch.end();
	}
	
	public PerspectiveCamera getCam() {
		return cam;
	}
	
	public World getWorld() {
		return world;
	}

	public void reset() {
		if (Skydiver3D.DEV_MODE) {
			Gdx.app.log(Skydiver3D.LOG, "Resetting Renderer");
		}
	}
}
