package com.scarlettapps.skydiver3d.world;

import junit.framework.TestCase;

public class SkydiverTest extends TestCase {
	
	private Skydiver skydiver;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		skydiver = new Skydiver();
	}
	
	public void testInitialPosition() {
		assertEquals(skydiver.getPositionZ(), Skydiver.STARTING_HEIGHT);
	}
	
	public void testJumpsOffPlane() {
    	assertFalse(skydiver.jumpedOffAirplane());
		skydiver.jumpOffAirplane();
		assertTrue(skydiver.jumpedOffAirplane());
	}
	
	public void testIntersectsCollectible() {
		
	}
	
	public void testParachuteOpens() {
		//assertFalse(skydiver.parachuteDeployed());
		skydiver.jumpOffAirplane();
		//assertTrue(skydiver.parachuteDeployed)_);
	}
	
	public void testOnWorldStateChanged() {
		
	}
}