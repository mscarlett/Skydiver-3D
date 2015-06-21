package com.scarlettapps.skydiver3d.world;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public enum Level {
	LEVEL_ONE(35, 0, 1, 1), LEVEL_TWO(35, 0, 1, 1), LEVEL_THREE(35, 0, 1, 1);
	
	public static final List<Level> LEVELS =
            new ArrayList<Level>(EnumSet.allOf(Level.class));
	
	public final int numObjects; // total number of objects
	public final int numSafe; // number of objects that reward points
	public final int numDangerous; // number of objects that penalize points
	
	public final float verticalSpacing; // vertical distance between rings
	public final float chaos; // determines number of collectibles that are dangerous
	public final float entropy; // distribution of types of collectibles
	public final float zigzag; // amount of zigzag behavior
	
	private Level(int numObjects, float chaos, float entropy, float zigzag) {
		this.numObjects = numObjects;
		this.chaos = chaos;
		this.entropy =  entropy;
		this.zigzag = zigzag;
		
		verticalSpacing = 35*89/((float)numObjects);
		numDangerous = (int) (chaos*numObjects);
		numSafe = numObjects - numDangerous;
	}
}
