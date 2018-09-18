package net.daporkchop.webchess.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import net.daporkchop.webchess.client.util.ClientConstants;
import net.daporkchop.webchess.client.util.Localization;
import net.daporkchop.webchess.common.net.WebChessPackets;
import net.daporkchop.webchess.common.net.packet.ColorUpdatePacket;
import net.daporkchop.webchess.common.util.Debug;

import java.io.IOException;

public class ClientMain extends ApplicationAdapter implements ClientConstants {
    public static int color = 0;

    SpriteBatch batch;
    Texture img;

    Client client;

    @Override
    public void create() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                Gdx.app.getApplicationLogger().log("a", "a", e);
            }
        });

        Localization.init();
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");

        Debug.colorSet = i -> color = i;

        try {
            this.client = new Client();
            WebChessPackets.registerPackets(this.client.getKryo());
            this.client.addListener(new Listener() {
                @Override
                public void received(Connection connection, Object object) {
                    if (object instanceof ColorUpdatePacket)    {
                        ColorUpdatePacket packet = (ColorUpdatePacket) object;
                        color = packet.color;
                    }
                }
            });
            this.client.connect(20000, "127.0.0.1", NETWORK_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(
                (float) (color & 0xFF) / 255.0f,
                (float) ((color >> 8) & 0xFF) / 255.0f,
                (float) ((color >> 16) & 0xFF) / 255.0f,
                1
        );
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //batch.begin();
        //batch.draw(img, 0, 0);
        //batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();

        try {
            this.client.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
