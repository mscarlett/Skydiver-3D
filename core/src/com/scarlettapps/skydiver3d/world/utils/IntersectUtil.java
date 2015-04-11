package com.scarlettapps.skydiver3d.world.utils;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.scarlettapps.skydiver3d.DefaultScreen;
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
	
	public static boolean intersects(Skydiver skydiver, Collectible collectible, PerspectiveCamera perspective, Vector3 minCpy, Vector3 maxCpy) {
		Decal decal = collectible.getDecal();
		if (Math.abs(decal.getZ()-skydiver.getPositionZ()) > 20f) {
			return false;
		}
		final float[] circlePosCpy = {decal.getPosition().x, decal.getPosition().y, decal.getPosition().z};
		final float[] circleSizeCpy = {decal.getWidth(), decal.getHeight(), 0};
		
		circlePos.set(circlePosCpy);
		circleSize.set(circleSizeCpy);
		perspective.project(circlePos);
		perspective.project(circleSize);
		circlePos.sub(DefaultScreen.width()/2,DefaultScreen.height()/2,0);
		circleSize.sub(DefaultScreen.width()/2,DefaultScreen.height()/2,0);
		circle.set(circlePos.x+Math.abs(circleSize.x)/2,
				circlePos.y+Math.abs(circleSize.y)/2,
				Math.abs(circleSize.y)/2+30f);
		
		final float[] minCpyTemp = {minCpy.x,minCpy.y,minCpy.z};
	    final float[] maxCpyTemp = {maxCpy.x,maxCpy.y,maxCpy.z};
	    
	    ModelInstance instance = skydiver.getInstance();
	    perspective.project(minCpy.mul(instance.transform));
	    perspective.project(maxCpy.mul(instance.transform));
	    minCpy.sub(DefaultScreen.width()/2,DefaultScreen.height()/2,0);
	    maxCpy.sub(DefaultScreen.width()/2,DefaultScreen.height()/2,0);
	    float x = Math.min(maxCpy.x, minCpy.x);
	    float y = Math.min(maxCpy.y, minCpy.y);
	    float width = Math.abs(maxCpy.x-minCpy.x);
	    float height = Math.abs(maxCpy.y-minCpy.y);
	    rectangle.set(x,y,width,height);
	    minCpy.set(minCpyTemp);
	    maxCpy.set(maxCpyTemp);
	    return Intersector.overlaps(circle,rectangle);
	}
}
