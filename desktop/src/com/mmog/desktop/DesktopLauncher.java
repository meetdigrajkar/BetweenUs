package com.mmog.desktop;

import java.io.IOException;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mmog.Client;
import com.mmog.MMOG;

public class DesktopLauncher {
	public static void main (String[] arg) throws IOException {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Between Us";
        config.height = 1080;
        config.width = 1920;
        config.resizable = true;
        config.addIcon("icons/icon_32.png", FileType.Internal);
        config.addIcon("icons/icon_128.png", FileType.Internal);
        
        config.fullscreen = true;
        
        Client client = new Client();
		// Start the listening thread...
		new LwjglApplication(new MMOG(), config);
	}
}