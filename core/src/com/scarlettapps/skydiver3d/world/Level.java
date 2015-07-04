package com.scarlettapps.skydiver3d.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import com.scarlettapps.skydiver3d.worldstate.WorldState;

public enum Level {
	LEVEL_FORTY_TWO(null, 190, 0.25f, 1, 1),
	LEVEL_FORTY_ONE(LEVEL_FORTY_TWO, 185, 0.25f, 1, 1),
	LEVEL_FORTY(LEVEL_FORTY_ONE, 180, 0.25f, 1, 1),
	LEVEL_THIRTY_NINE(LEVEL_FORTY, 175, 0.25f, 1, 1),
	LEVEL_THIRTY_EIGHT(LEVEL_THIRTY_NINE, 170, 0.25f, 1, 1),
	LEVEL_THIRTY_SEVEN(LEVEL_THIRTY_EIGHT, 165, 0.25f, 1, 1),
	LEVEL_THIRTY_SIX(LEVEL_THIRTY_SEVEN, 160, 0.25f, 1, 1),
	LEVEL_THIRTY_FIVE(LEVEL_THIRTY_SIX, 155, 0.25f, 1, 1),
	LEVEL_THIRTY_FOUR(LEVEL_THIRTY_FIVE, 150, 0.25f, 1, 1),
	LEVEL_THIRTY_THREE(LEVEL_THIRTY_FOUR, 145, 0.25f, 1, 1),
	LEVEL_THIRTY_TWO(LEVEL_THIRTY_THREE, 140, 0.25f, 1, 1),
	LEVEL_THIRTY_ONE(LEVEL_THIRTY_TWO, 135, 0.25f, 1, 1),
	LEVEL_THIRTY(LEVEL_THIRTY_ONE, 130, 0.25f, 1, 1),
	LEVEL_TWENTY_NINE(LEVEL_THIRTY, 125, 0.25f, 1, 1),
	LEVEL_TWENTY_EIGHT(LEVEL_TWENTY_NINE, 120, 0.25f, 1, 1),
	LEVEL_TWENTY_SEVEN(LEVEL_TWENTY_EIGHT, 115, 0.25f, 1, 1),
	LEVEL_TWENTY_SIX(LEVEL_TWENTY_SEVEN, 110, 0.5f, 1, 1),
	LEVEL_TWENTY_FIVE(LEVEL_TWENTY_SIX, 105, 0.5f, 1, 1),
	LEVEL_TWENTY_FOUR(LEVEL_TWENTY_FIVE, 100, 0.75f, 1, 1),
	LEVEL_TWENTY_THREE(LEVEL_TWENTY_FOUR, 95, 0.75f, 1, 1),
	LEVEL_TWENTY_TWO(LEVEL_TWENTY_THREE, 90, 0.75f, 1, 1),
	LEVEL_TWENTY_ONE(LEVEL_TWENTY_TWO, 85, 0.75f, 1, 1),
	LEVEL_TWENTY(LEVEL_TWENTY_ONE, 80, 0.75f, 1, 1),
	LEVEL_NINETEEN(LEVEL_TWENTY, 75, 0.75f, 1, 1),
	LEVEL_EIGHTTEEN(LEVEL_NINETEEN, 70, 0.7f, 1, 1),
	LEVEL_SEVENTEEN(LEVEL_EIGHTTEEN, 65, 0.7f, 1, 1),
	LEVEL_SIXTEEN(LEVEL_SEVENTEEN, 65, 0.7f, 1, 1),
	LEVEL_FIFTEEN(LEVEL_SIXTEEN, 60, 0.7f, 1, 1),
	LEVEL_FOURTEEN(LEVEL_FIFTEEN, 60, 0.6f, 1, 1),
	LEVEL_THIRTEEN(LEVEL_FOURTEEN, 60, 0.5f, 1, 1),
	LEVEL_TWELVE(LEVEL_THIRTEEN, 55, 0.4f, 1, 1),
	LEVEL_ELEVEN(LEVEL_TWELVE, 55, 0.35f, 1, 1),
	LEVEL_TEN(LEVEL_ELEVEN, 55, 0.3f, 1, 1),
	LEVEL_NINE(LEVEL_TEN, 50, 0.25f, 1, 1),
	LEVEL_EIGHT(LEVEL_NINE, 50, 0.2f, 1, 1),
	LEVEL_SEVEN(LEVEL_EIGHT, 50, 0.15f, 1, 1),
	LEVEL_SIX(LEVEL_SEVEN, 45, 0.15f, 1, 1),
	LEVEL_FIVE(LEVEL_SIX, 45, 0.1f, 1, 1),
	LEVEL_FOUR(LEVEL_FIVE, 45, 0.05f, 1, 1),
	LEVEL_THREE(LEVEL_FOUR, 45, 0, 1, 1),
	LEVEL_TWO(LEVEL_THREE, 40, 0, 1, 1),
	LEVEL_ONE(LEVEL_TWO, 35, 0, 1, 1);
	
	public static final List<Level> LEVELS;
	
    static {
        LEVELS = new ArrayList<Level>(EnumSet.allOf(Level.class));
	    Collections.reverse(LEVELS);
    }
    
    public final int DISTANCE = WorldState.INITIAL.minAltitude-WorldState.SKYDIVING.minAltitude-185;
    
	public final int numObjects; // total number of objects
	public final int numSafe; // number of objects that reward points
	public final int numDangerous; // number of objects that penalize points
	
	public final float verticalSpacing; // vertical distance between rings
	public final float chaos; // determines number of collectibles that are dangerous
	public final float entropy; // distribution of types of collectibles
	public final float zigzag; // amount of zigzag behavior
	
	public final Level nextLevel;
	
	private Level(Level nextLevel, int numObjects, float chaos, float entropy, float zigzag) {
		if (nextLevel == null) {
			this.nextLevel = this;
		} else {
		    this.nextLevel = nextLevel;
		}
		this.numObjects = numObjects;
		this.chaos = chaos;
		this.entropy =  entropy;
		this.zigzag = zigzag;
		
		verticalSpacing = this.DISTANCE/((float)numObjects);
		numDangerous = (int) (chaos*numObjects);
		numSafe = numObjects - numDangerous;
	}
	
	public int index() {
		return Math.min(LEVELS.indexOf(this), LEVELS.size()-1);
	}
}
