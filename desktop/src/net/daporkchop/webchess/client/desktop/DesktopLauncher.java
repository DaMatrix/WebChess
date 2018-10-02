package net.daporkchop.webchess.client.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.daporkchop.webchess.client.ClientMain;
import net.daporkchop.webchess.client.util.ClientConstants;

public class DesktopLauncher implements ClientConstants  {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = TARGET_WIDTH;
		config.height = TARGET_HEIGHT;

		config.x = 1024;

		//config.resizable = false;
		new LwjglApplication(new ClientMain("127.0.0.1", false), config);
	}
}
