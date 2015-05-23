package com.scarlettapps.skydiver3d.world;

import com.scarlettapps.skydiver3d.Skydiver3D;
import com.scarlettapps.skydiver3d.WorldPresenter;
import com.scarlettapps.skydiver3d.worldstate.Status;

public class MockWorldPresenter extends WorldPresenter {

	public MockWorldPresenter(Skydiver3D game) {
		super(game);
	}
	
	public void setState(Status status) {
		// Allow game to be set to predetermined state
	}

}
