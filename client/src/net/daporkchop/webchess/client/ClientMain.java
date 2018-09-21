package net.daporkchop.webchess.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.daporkchop.lib.network.endpoint.EndpointListener;
import net.daporkchop.lib.network.endpoint.builder.ClientBuilder;
import net.daporkchop.lib.network.endpoint.client.PorkClient;
import net.daporkchop.lib.network.packet.Packet;
import net.daporkchop.webchess.client.net.WebChessSessionClient;
import net.daporkchop.webchess.client.util.ClientConstants;
import net.daporkchop.webchess.common.net.WebChessProtocol;
import net.daporkchop.webchess.common.net.WebChessSession;
import net.daporkchop.webchess.common.net.packet.UpdateColorPacket;

import java.net.InetSocketAddress;

@RequiredArgsConstructor
public class ClientMain extends ApplicationAdapter implements ClientConstants {
    public static int color;

    @NonNull
    private final String localAddress;

    SpriteBatch batch;
    Texture img;
    PorkClient<WebChessSession> client;

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");

        client = new ClientBuilder<WebChessSession>()
                .setProtocol(new WebChessProtocol(WebChessSessionClient::new))
                .setAddress(new InetSocketAddress(this.localAddress, NETWORK_PORT))
                .addListener(new EndpointListener<WebChessSession>() {
                    @Override
                    public void onConnect(WebChessSession session) {
                    }

                    @Override
                    public void onDisconnect(WebChessSession sesion, String reason) {
                    }

                    @Override
                    public void onReceieve(WebChessSession session, Packet packet) {
                        if (packet instanceof UpdateColorPacket) {
                            color = ((UpdateColorPacket) packet).color;
                        }
                    }
                })
                .build();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor((float) (color & 0xFF) / 255.0f, (float) ((color >> 8) & 0xFF) / 255.0f, (float) ((color >> 16) & 0xFF) / 255.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (false) {
            batch.begin();
            batch.draw(img, 0, 0);
            batch.end();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }
}
