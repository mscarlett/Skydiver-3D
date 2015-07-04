package com.scarlettapps.skydiver3d.resources;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.scarlettapps.skydiver3d.world.Level;

public class AchievementsFactory {
	
	private static AchievementsFactory instance;

	private final Preferences achievements;
	
	private AchievementsFactory() {
		achievements = Gdx.app.getPreferences("Achievements");
	}
	
	public void setLevelsCompleted(Level level) {
		setLevelsCompleted(level.index()+1);
	}

	public void setLevelsCompleted(int i) {
		achievements.putInteger("Levels", i);
		achievements.flush();
	}
	
	public int levelsCompleted() {
		return achievements.getInteger("Levels", 0);
	}
	
	public void setLevelRating(Level level, int rating) {
		setLevelRating(level.index()+1, rating);
	}
	
	public void setLevelRating(int level, int rating) {
		achievements.putInteger("Rating%" + level, rating);
		achievements.flush();
	}
	
	public int getLevelRating(Level level) {
		return getLevelRating(level.index()+1);
	}
	
	public int getLevelRating(int level) {
		return achievements.getInteger("Rating%" + level, -1);
	}
	
	public static AchievementsFactory getInstance() {
		if (instance == null) {
			instance = new AchievementsFactory();
		}
		return instance;
	}
}
