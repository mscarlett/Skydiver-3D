// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.world;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.scarlettapps.skydiver3d.DefaultScreen;
import com.scarlettapps.skydiver3d.resources.AssetFactory;
import com.scarlettapps.skydiver3d.resources.AssetFactory.ModelType;
import com.scarlettapps.skydiver3d.world.utils.AnimationController;
import com.scarlettapps.skydiver3d.world.utils.IntersectUtil;
import com.scarlettapps.skydiver3d.worldstate.WorldState;
import com.scarlettapps.skydiver3d.worldview.Renderer;

public class Skydiver extends GameObject {
	
	// Bounds for skydiver x and y positions
	public static final float MIN_X = -4.5f;
	public static final float MIN_Y = -3.3f;
	public static final float MAX_X = 4.1f;
	public static final float MAX_Y = 1.7f;
	//Skydiver starting height in meters
	public static final int STARTING_HEIGHT = 4480;
	//Terminal speed for skydiver belly position in meters/second
	public static final float MIN_TERMINAL_SPEED = 53f;
	//Terminal speed for skydiver head down position in meters/second
	public static final float MAX_TERMINAL_SPEED = 89f;
	private static final float STARTING_POSE = 8.266682f*34/200;
	
	public static PerspectiveCamera cam;

	private ModelInstance instance;
	private AnimationController controller;
	
	private Vector3 position;
	private Vector3 velocity;
	
	private Environment environment;
	
	private Vector3 axis;
	private Vector3 angle;
	private Vector2 skydiverAngle;
	
	private boolean landing;
	private boolean parachuting;
	private boolean parachuteDeployed;
	private float timeSinceParachuteDeployed;
	private boolean jumpedOffAirplane;
	private float timeSinceJumpedOffAirplane;
	private boolean finalState;
	private float timeSinceFinalState;
	private Vector3 minCpy;
	private Vector3 maxCpy;
	
	public Skydiver() {
		super(true,true);
	}
	
	@Override
	public void initialize() {
		landing = false;
		parachuting = false;
		parachuteDeployed = false;
		timeSinceParachuteDeployed = 0f;
		jumpedOffAirplane = false;
		timeSinceJumpedOffAirplane = 0f;
		finalState = false;
		timeSinceFinalState = 0f;
		
		axis = new Vector3();
		angle = new Vector3();
		skydiverAngle = new Vector2();
		
        String filename = ModelType.SKYDIVER;
		
		Model model = AssetFactory.getInstance().get(filename, Model.class);
		instance = new ModelInstance(model);
		instance.materials.get(0).set(
				new BlendingAttribute(GL20.GL_SRC_ALPHA,
						GL20.GL_ONE_MINUS_SRC_ALPHA, 1f));
		controller = new AnimationController(instance);
		
		float xScale = DefaultScreen.width()/480f;
		float yScale = DefaultScreen.height()/320f;
		minCpy = new Vector3(-1.436071f*xScale, 0.0028594136f*yScale, -0.38007197f);
		maxCpy = new Vector3(0.436073f*xScale, 1.9008393f*yScale, 0.17680216f);
		//bounds = new BoundingBox(minCpy, maxCpy);
		
		position = new Vector3();
		setToTranslation(0, 0, STARTING_HEIGHT);
		velocity = new Vector3(0, 0, 0);
		
		controller.animate(instance.animations.get(0).id, -1, 1f, null,0.2f);
		
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.7f, 0.7f, 0.7f, 1.0f));
        environment.add(new DirectionalLight().set(0.4f, 0.4f, 0.4f, -1f, -0.8f, -0.2f));
	}
	
	@Override
	public void updateObject(float delta) {
		if (getPositionZ() < WorldState.INITIAL.minAltitude) {
			updateSkydiving(delta);
		} else {
			updateBefore(delta);
		}
		
	}
	
	private void updateSkydiving(float delta) {
		if (!landing) {
			checkBounds();
		}
		
		final float dx = velocity.x*delta;
		final float dy = velocity.y*delta;
		final float dz = velocity.z*delta;
		
		setToTranslation(position.x+dx, position.y+dy, position.z+dz);
		
		float pose = (-velocity.z-MIN_TERMINAL_SPEED)/(MAX_TERMINAL_SPEED-MIN_TERMINAL_SPEED);
		
		if (landing) {
			skydiverAngle.y = -90;
		} else {
			if (!parachuting) {
			    skydiverAngle.y = pose*90;
			} else {
			    skydiverAngle.y -= (skydiverAngle.y+90)*delta;
			}
		}
		
		axis.set(skydiverAngle.y, skydiverAngle.x, 0);
		angle.set(skydiverAngle.y, skydiverAngle.x, 0);
		
		rotate(axis.nor(), angle.len());
		
		if (finalState) {
			final float totalTime = 4f;
			if (timeSinceFinalState < totalTime) {
				timeSinceFinalState += delta;
				controller.update(delta, 7.06801311f+(8.266682f-7.06801311f)*timeSinceFinalState/totalTime);
			} else {
				controller.update(delta, 8.266682f);
			}
		} else if (landing) {
			controller.update(delta, 6.32401173f);
		} else if (parachuting) {
			final float totalTime = 1f;
			if (parachuteDeployed) {
				if (timeSinceParachuteDeployed < totalTime) {
					timeSinceParachuteDeployed += delta;
					controller.update(delta, 4.133341f+(6.32401173f-4.133341f)*timeSinceParachuteDeployed/totalTime);
				} else {
					controller.update(delta, 6.32401173f);
				}
			} else {
				controller.update(delta, 4.133341f);
			}
		} else {
			controller.update(delta, (1-pose)*3.3066728f);
		}
		
		skydiverAngle.x = Math.signum(skydiverAngle.x)
				* (Math.abs(skydiverAngle.x) - 100 * delta / 2);
		velocity.z += delta*20f*pose;
	}
	
	private void updateBefore(float delta) {
		if (jumpedOffAirplane) {
			float jumpTime = 5f;
			
			if (timeSinceJumpedOffAirplane < jumpTime) {
				timeSinceJumpedOffAirplane += delta;
				controller.update(delta, STARTING_POSE+(3.3066728f-STARTING_POSE)*timeSinceJumpedOffAirplane/jumpTime);
			}
			
			float vx = 0;
			float vy = 0;
			float vz = -Math.min(MIN_TERMINAL_SPEED,MIN_TERMINAL_SPEED*(2*timeSinceJumpedOffAirplane)/jumpTime*(2*timeSinceJumpedOffAirplane)/jumpTime);
			
			velocity.set(vx,vy,vz);
			
			final float dx = velocity.x*delta;
			final float dy = velocity.y*delta;
			final float dz = velocity.z*delta;
			
			setToTranslation(position.x+dx, position.y+dy+Math.min(4*0.005f*(timeSinceJumpedOffAirplane),5), position.z+dz+Math.max(0.05f-16*0.05f*(timeSinceJumpedOffAirplane-0.25f)*(timeSinceJumpedOffAirplane-0.25f),0));
			
			if (timeSinceJumpedOffAirplane < jumpTime) {
				skydiverAngle.set(0,(float) (-88*(1-Math.sqrt(timeSinceJumpedOffAirplane)/jumpTime)));
				axis.set(skydiverAngle.y, skydiverAngle.x, 0);
				angle.set(skydiverAngle.y, skydiverAngle.x, 0);
				rotate(axis.nor(), angle.len());
			}
			
		} else {
			setToTranslation(position.x, position.y, position.z);
			
			skydiverAngle.set(0,-88);
			axis.set(skydiverAngle.y, skydiverAngle.x, 0);
			angle.set(skydiverAngle.y, skydiverAngle.x, 0);
			rotate(axis.nor(), angle.len());
			controller.update(delta, STARTING_POSE);
		}
	}
	
	private void checkBounds() {
		if (position.x < MIN_X) {
			position.x = MIN_X;
			velocity.x = 0;
		} else if (position.x > MAX_X) {
			position.x = MAX_X;
			velocity.x = 0;
		}
		if (position.y < MIN_Y) {
			position.y = MIN_Y;
			velocity.y = 0;
		} else if (position.y > MAX_Y) {
			position.y = MAX_Y;
			velocity.y = 0;
		}
	}
	
	public void render(ModelBatch modelBatch) {
		if (render) {
			modelBatch.render(instance, environment);
		}
	}
	
	public void jumpOffAirplane() {
		jumpedOffAirplane = true;
	}
	
	public void deployParachute() {
		parachuteDeployed = true;
	}
	
	public void addToVelocity(float x, float y, float z) { //TODO separate methods for each component
		velocity.x += x;
		velocity.y += y;
		velocity.z += z;
		if (!landing) {
			if (velocity.z < -MAX_TERMINAL_SPEED) {
				velocity.z = -MAX_TERMINAL_SPEED;
			} else if (velocity.z > -MIN_TERMINAL_SPEED) {
				velocity.z = -MIN_TERMINAL_SPEED;
			}
		}
	}

	public void setToTranslation(float x, float y, float z) {
		instance.transform.setToTranslation(x, y, z);
		instance.transform.rotate(Vector3.Y, 180);
		instance.transform.getTranslation(position);
	}
	
	public void translate(float x, float y, float z) {
		instance.transform.translate(x, y, z);
		instance.transform.getTranslation(position);
	}
	
	public void setToRotatation(Vector3 axis, float angle) {
		instance.transform.setToRotation(axis, angle);
	}
	
	public void rotate(Vector3 vec, float f) {
		instance.transform.rotate(vec, f);
	}
	
	public void setPose(float alpha) {
		controller.update(0, alpha);
	}
	
	public float getPositionX() {
		return position.x;
	}
	
	public float getPositionY() {
		return position.y;
	}

	public float getPositionZ() {
		return position.z;
	}
	
	public boolean intersects(Collectible collectible) { //offset collectible position up and to right
		return IntersectUtil.intersects(this, collectible, cam, minCpy, maxCpy);
	}

	public Vector3 getPosition() {
		return position;
	}
	
	public Vector3 getVelocity() {
		return velocity;
	}

	@Override
	protected void renderObject(Renderer renderer) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onWorldStateChanged(WorldState worldState) {
		switch (worldState) {
			case INITIAL:
				break;
			case SKYDIVING:
				break;
			case PARACHUTING:
				break;
			case LANDING:
				break;
			case FINAL:
				break;
		}
	}

	public void setRender(boolean b) {
		this.render = b;
	}
	
	public ModelInstance getInstance() {
		return instance;
	}

	public Vector2 skydiverAngle() {
		return skydiverAngle;
	}

	public void setLanding(boolean b) {
		landing = b;
	}

	public void setParachuting(boolean b) {
		parachuting = b;
	}

	public boolean jumpedOffAirplane() {
		return jumpedOffAirplane;
	}

	public void setFinalState(boolean b) {
		finalState = b;
	}
}
