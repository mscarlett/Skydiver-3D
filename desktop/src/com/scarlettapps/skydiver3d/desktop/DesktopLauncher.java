package com.scarlettapps.skydiver3d.desktop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.scarlettapps.skydiver3d.Skydiver3D;

public class DesktopLauncher {
	public static void main (String[] arg) {
		launchApplication(new Skydiver3D());
	}
	
	public static void launchApplication(ApplicationListener listener) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 960;
		config.height = 640;
		launchApplication(listener, config);
	}
	
	public static void launchApplication(ApplicationListener listener,
			LwjglApplicationConfiguration config) {
		new LwjglApplication(listener, config);
	}
}
