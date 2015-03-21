package com.scarlettapps.skydiver3d.worldstate;

public final class Score {

	public final int ringScore;
	public final int parachutingScore;
	public final int landingScore;
	public final int totalScore;
	public final int rating;
	
	public Score(int ringScore, int parachutingScore, int landingScore, int rating) {
		this.ringScore = ringScore;
		this.parachutingScore = parachutingScore;
		this.landingScore = landingScore;
		this.totalScore = ringScore + parachutingScore + landingScore;
		this.rating = rating;
	}
}
