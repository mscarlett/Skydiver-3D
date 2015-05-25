package com.scarlettapps.skydiver3d.world;

import com.scarlettapps.skydiver3d.worldstate.Status;

import junit.framework.TestCase;

public class SkydiverTest extends TestCase {
	
	private Skydiver skydiver;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		skydiver = new Skydiver(new Status());
	}
	
	public void testInitialPosition() {
		assertEquals(skydiver.getPositionZ(), Skydiver.STARTING_HEIGHT);
	}
	
	public void testJumpsOffPlane() {
		
	}
	
	public void testIntersectsCollectible() {
		
	}
	
	public void testParachuteOpens() {
		
	}
	
	public void testOnWorldStateChanged() {
		
	}
	
	public void testOutOfBounds() {
		//Vector3 position = skydiver.getPosition();
	
	}
}