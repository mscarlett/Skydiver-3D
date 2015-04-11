package com.scarlettapps.skydiver3d.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.scarlettapps.skydiver3d.SkydivingUI;

public class SkydivingUILauncher {

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//config.width = 960;
		//config.height = 640;
		new LwjglApplication(new SkydivingUI(), config);
	}
}
