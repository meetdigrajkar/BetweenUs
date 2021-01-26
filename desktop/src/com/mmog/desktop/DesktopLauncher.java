package com.mmog.desktop;

import java.io.IOException;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mmog.Client;
import com.mmog.MMOG;

public class DesktopLauncher {
	public static void main (String[] arg) throws IOException {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Among Us";
        config.height = 1080;
        config.width = 1920;
        
		new LwjglApplication(new MMOG(), config);
		
		// Start the listening thread...
		Client client = new Client();
	}
}