/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2018-2018 DaPorkchop_ and contributors
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it. Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from DaPorkchop_.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original authors of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package net.daporkchop.webchess.client.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.daporkchop.webchess.client.ClientMain;
import net.daporkchop.webchess.client.util.ClientConstants;

public class DesktopLauncher implements ClientConstants {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = TARGET_WIDTH;
        config.height = TARGET_HEIGHT;

        config.addIcon("icon/128.png", Files.FileType.Internal);
        config.addIcon("icon/32.png", Files.FileType.Internal);
        config.addIcon("icon/16.png", Files.FileType.Internal);
        config.title = "MultiGames";

        if (IDE && true) {
            String s = System.getProperty("window.offset", "");
            int a = Integer.parseInt(s.isEmpty() ? "0" : s);
            //config.x = (int) (s.isEmpty() ? -1 : (TARGET_WIDTH * 2 + (TARGET_WIDTH * 1.3f * a)));
            config.x = (int) (s.isEmpty() ? -1 : TARGET_WIDTH * 1.3f * a);
        }

        //config.resizable = false;
        new LwjglApplication(new ClientMain("127.0.0.1", false), config);
    }
}
