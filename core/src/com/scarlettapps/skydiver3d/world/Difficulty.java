package com.scarlettapps.skydiver3d.world;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public enum Difficulty {
	LEVEL_ONE(35, 0, 1, 1), LEVEL_TWO(35, 0, 1, 1), LEVEL_THREE(35, 0, 1, 1);
	
	public static final List<Difficulty> levels =
            new ArrayList<Difficulty>(EnumSet.allOf(Difficulty.class));
	
	public final int numObjects;
	public final int numSafe;
	public final int numDangerous;
	
	private float startingOffset; // 289
	
	public final float verticalSpacing; // 89
	public final float chaos; // determines number of collectibles that are dangerous
	public final float entropy; // distribution of types of collectibles
	public final float zigzag; // amount of zigzag behavior
	
	private Difficulty(int numObjects, float chaos, float entropy, float zigzag) {
		this.numObjects = numObjects;
		this.chaos = chaos;
		this.entropy =  entropy;
		this.zigzag = zigzag;
		
		verticalSpacing = 35*89/((float)numObjects);
		numDangerous = (int) (chaos*numObjects);
		numSafe = numObjects - numDangerous;
	}
}
