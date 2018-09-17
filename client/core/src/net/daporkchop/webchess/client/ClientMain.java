package net.daporkchop.webchess.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.daporkchop.lib.network.builder.ClientBuilder;
import net.daporkchop.lib.network.endpoint.client.Client;
import net.daporkchop.webchess.client.util.ClientConstants;
import net.daporkchop.webchess.client.util.Localization;
import net.daporkchop.webchess.common.net.WebChessProtocol;
import net.daporkchop.webchess.common.util.Debug;

import java.net.InetSocketAddress;

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

        this.client = new ClientBuilder()
                .setAddress(new InetSocketAddress("192.168.1.108", NETWORK_PORT))
                .setPacketProtocol(new WebChessProtocol())
                .build();

        new Thread(() -> {
            try {
                while (ClientMain.this.batch != null)   {
                    if (ClientMain.this.client == null || !ClientMain.this.client.getIoConnector().isActive())  {
                        if (ClientMain.this.client != null) {
                            ClientMain.this.client.close();
                        }
                        ClientMain.this.client = new ClientBuilder()
                                .setAddress(new InetSocketAddress("192.168.1.108", NETWORK_PORT))
                                .setPacketProtocol(new WebChessProtocol())
                                .build();
                    }
                    Thread.sleep(1000L);
                }
            } catch (InterruptedException e)    {
            }
        }).start();
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

        this.client.close();
    }
}
