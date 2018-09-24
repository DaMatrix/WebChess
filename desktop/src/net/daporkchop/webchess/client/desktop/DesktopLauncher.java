package net.daporkchop.webchess.client.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.daporkchop.webchess.client.ClientMain;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 64 * 8;
		config.height = 64 * 8;
		new LwjglApplication(new ClientMain("127.0.0.1"), config);
	}
}
