// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.world;

import java.util.Iterator;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.SortedIntList;
import com.badlogic.gdx.utils.SortedIntList.Node;
import com.scarlettapps.skydiver3d.DefaultScreen;
import com.scarlettapps.skydiver3d.worldstate.Status;
import com.scarlettapps.skydiver3d.worldstate.WorldState;
import com.scarlettapps.skydiver3d.worldview.Renderer;

public class Collectibles extends GameObject  implements Iterable<Node<Collectible>> {
	
	private static final float ROTATION_FREQUENCY = 0.5f;
	private static final float DECAL_WIDTH = 7/480f*DefaultScreen.VIRTUAL_WIDTH;
	private static final float DECAL_HEIGHT = 7/320f*DefaultScreen.VIRTUAL_HEIGHT;
	private static final int STARTING_OFFSET = 289;
	private static final int X_RANGE = DefaultScreen.VIRTUAL_WIDTH/75;
	private static final int Y_RANGE = DefaultScreen.VIRTUAL_HEIGHT/75;
	
	private final SortedIntList<Collectible> preRender;
	private final SortedIntList<Collectible> toRender;
	private final SortedIntList<Collectible> postRender;
	
	private Level difficulty = Level.LEVEL_ONE;
	
	public Collectibles() {
		super(true, true);
		
		preRender = new SortedIntList<Collectible>();
		toRender = new SortedIntList<Collectible>();
		postRender = new SortedIntList<Collectible>();
	}
	
	public void setDifficulty(Level difficulty) {
		this.difficulty = difficulty;
	}
	
	@Override
	public void initialize() {
		int z = Skydiver.STARTING_HEIGHT-STARTING_OFFSET;

		int numDangerous = difficulty.numDangerous;
		
		for (int i = 0; i < difficulty.numObjects; i++) {
			boolean dangerous = MathUtils.randomBoolean(numDangerous/((float)difficulty.numObjects-i));
			
			float x = MathUtils.random(X_RANGE * 1.8f) - X_RANGE / 2 * 1.8f;
			float y;
			
			if (Gdx.app.getType() == ApplicationType.Android) {
				y = 0;
			} else {
				y = MathUtils.random(Y_RANGE * 1.5f) - Y_RANGE / 2 * 1.5f;
			}
			
			Collectible collectible;
			
			if (dangerous) {
				numDangerous--;
				int type = MathUtils.random(2);
				
				switch (type) {
					case 0: collectible = new RingNuclear(DECAL_WIDTH, DECAL_HEIGHT, x, y, z); break;
					case 1: collectible = new RingGhost(DECAL_WIDTH, DECAL_HEIGHT, x, y, z); break;
					case 2: collectible = new RingSkull(DECAL_WIDTH, DECAL_HEIGHT, x, y, z); break;
					default: throw new GdxRuntimeException("Invalid type: " + type);
				}
			} else {
				boolean isRing = MathUtils.randomBoolean();
				collectible = isRing ? new RingGold(DECAL_WIDTH, DECAL_HEIGHT, x, y, z) : new Star(DECAL_WIDTH, DECAL_HEIGHT, x, y, z);
			}
			
			preRender.insert(-z, collectible);
			z -= difficulty.verticalSpacing;
		}
	}
	
	@Override
	public void reset() {
		initialize();
	}
	
	public void updateObject(float delta) {
		for (Node<Collectible> node : toRender) {
			Decal decal = node.value.getDecal();
			decal.rotateZ(delta*ROTATION_FREQUENCY);
		}
	}
	
	public void setToRender(float camHeight, float offset) {
		Iterator<Node<Collectible>> iterator;
		
		iterator = preRender.iterator();
		while (iterator.hasNext()) {
			Node<Collectible> node = iterator.next();
			if (node != null) {
				float height = -node.index;
				if (height > camHeight - offset) {
					iterator.remove();
					toRender.insert(node.index, node.value);
				} else {
					break;
				}
			}
		}
		
		iterator = toRender.iterator();
		while (iterator.hasNext()) {
			Node<Collectible> node = iterator.next();
			if (node != null) {
				float height = -node.index;
				if (height > camHeight) {
					iterator.remove();
					postRender.insert(node.index, node.value);
				} else {
					break;
				}
			}
		}
	}

	public SortedIntList<Collectible> getCollectibles() {
		return toRender;
	}

	@Override
	public Iterator<Node<Collectible>> iterator() {
		return toRender.iterator();
	}

	public Collectible getClosest() {
		Iterator<Node<Collectible>> iterator = iterator();
		if (iterator.hasNext()) {
			return iterator.next().value;
		}
		return null;
	}

	/**
	 * Check whether the skydiver is close enough that we should test for
	 * intersection between the skydiver and the next Collectible
	 * @param skydiverZ
	 * @return
	 */
	public boolean checkIntersect(float skydiverZ) {
		Collectible first = getClosest();
		if (first == null) {
			return false;
		}
		final float z = first.getDecal().getZ();
		return Math.abs(z-skydiverZ) <= 20f;
	}

	@Override
	protected void renderObject(Renderer renderer) {
		
	}

	@Override
	public void onWorldStateChanged(WorldState worldState) {
		
	}

	public void removeClosest() {
		Iterator<Node<Collectible>> iterator = iterator();
		Node<Collectible> node = iterator.next();
		iterator.remove();
		postRender.insert(node.index, node.value);
	}
}
