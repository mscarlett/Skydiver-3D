package com.scarlettapps.skydiver3d.world.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.scarlettapps.skydiver3d.Skydiver3D;
import com.scarlettapps.skydiver3d.world.Collectible;
import com.scarlettapps.skydiver3d.world.Skydiver;

/**
 * Tests whether or not the skydiver and a collectible are intersecting.
 *
 */
public class IntersectUtil {

	private static Vector3 circlePos = new Vector3();
	private static Vector3 circleSize = new Vector3();
	private static Rectangle rectangle = new Rectangle();
	private static Circle circle = new Circle();
	
	private IntersectUtil() {}
	
	public static boolean intersects(Skydiver skydiver, Collectible collectible) {
		Decal decal = collectible.getDecal();
		if (Math.abs(decal.getZ()-skydiver.getPositionZ()) > 20f) {
			return false;
		}
		float x = 0.2f*decal.getX();
		float y = 0.2f*decal.getY();
		float width = 0.15f*decal.getWidth();
		float height = 0.15f*decal.getHeight();
		Vector3 position = skydiver.getPosition();
		float px = position.x + 1.9f;
		float py = position.y + 2.5f;
		float dx = px - x;
		float dy = py - y;
		return (dx > 0 && dx < width) && (dy > 0 && dy < height);
	}
}
