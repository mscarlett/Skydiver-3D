// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.world;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.SortedIntList;
import com.badlogic.gdx.utils.SortedIntList.Node;
import com.scarlettapps.skydiver3d.DefaultScreen;
import com.scarlettapps.skydiver3d.resources.Graphics;
import com.scarlettapps.skydiver3d.world.gamestate.StatusManager.WorldState;
import com.scarlettapps.skydiver3d.worldview.Renderer;

public class Collectibles extends GameObject  implements Iterable<Node<Collectible>> {
	
	private static final String RING_TEXTURE = "data/ring-big_2.png";
	private static final String STAR_TEXTURE = "data/star-big_2.png";
	private static final float ROTATION_FREQUENCY = 0.5f;
	private static final float DECAL_WIDTH = 7/480f*DefaultScreen.width();
	private static final float DECAL_HEIGHT = 7/320f*DefaultScreen.height();
	private static final int NUM_OBJECTS = 35;
	private static final int STARTING_OFFSET = 289;
	private static final int VERTICAL_SPACING = 89;
	private static final int X_RANGE = DefaultScreen.width()/75;
	private static final int Y_RANGE = DefaultScreen.height()/75;
	
	private final SortedIntList<Collectible> preRender;
	private final SortedIntList<Collectible> toRender;
	private final SortedIntList<Collectible> postRender;
	
	public Collectibles() {
		super(true,true);
		
		preRender = new SortedIntList<Collectible>();
		toRender = new SortedIntList<Collectible>();
		postRender = new SortedIntList<Collectible>();

		Texture ringTexture = Graphics.get(RING_TEXTURE, Texture.class);
		Texture starTexture = Graphics.get(STAR_TEXTURE, Texture.class);
		ringTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		starTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion ringTextureRegion = new TextureRegion(ringTexture);
		TextureRegion starTextureRegion = new TextureRegion(starTexture);

		int z = Skydiver.STARTING_HEIGHT-STARTING_OFFSET;

		for (int i = 0; i < NUM_OBJECTS; i++) {
			float x = MathUtils.random(X_RANGE * 1.8f) - X_RANGE / 2 * 1.8f;
			float y = MathUtils.random(Y_RANGE * 1.5f) - Y_RANGE / 2 * 1.5f;
			TextureRegion texture = MathUtils.randomBoolean() ? ringTextureRegion : starTextureRegion;
			Collectible collectible = new Collectible(DECAL_WIDTH, DECAL_HEIGHT, texture, x, y, z);
			preRender.insert(-z, collectible);
			z -= VERTICAL_SPACING;
		}
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

	@Override
	public void forEach(Consumer<? super Node<Collectible>> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Spliterator<Node<Collectible>> spliterator() {
		// TODO Auto-generated method stub
		return null;
	}
}
