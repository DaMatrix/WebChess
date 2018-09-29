package net.daporkchop.webchess.client;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.daporkchop.lib.network.endpoint.EndpointListener;
import net.daporkchop.lib.network.endpoint.builder.ClientBuilder;
import net.daporkchop.lib.network.endpoint.client.PorkClient;
import net.daporkchop.lib.network.packet.Packet;
import net.daporkchop.webchess.client.input.BaseInputProcessor;
import net.daporkchop.webchess.client.net.WebChessSessionClient;
import net.daporkchop.webchess.client.render.RenderManager;
import net.daporkchop.webchess.client.render.impl.BackgroundRenderer;
import net.daporkchop.webchess.client.util.ChessTex;
import net.daporkchop.webchess.client.util.ClientConstants;
import net.daporkchop.webchess.client.util.CoordinateOffset;
import net.daporkchop.webchess.client.util.Localization;
import net.daporkchop.webchess.common.net.WebChessProtocol;
import net.daporkchop.webchess.common.net.WebChessSession;
import net.daporkchop.webchess.common.net.packet.UpdateColorPacket;

import java.net.InetSocketAddress;

@RequiredArgsConstructor
public class ClientMain extends ApplicationAdapter implements ClientConstants {
    @NonNull
    private final String localAddress;
    @Getter
    private final BaseInputProcessor inputProcessor = new BaseInputProcessor(this);
    private PorkClient<WebChessSession> client;
    @Getter
    private RenderManager renderManager;
    private final boolean android;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        Localization.init();
        ChessTex.init();

        this.renderManager = new RenderManager(this);
        this.renderManager.create();

        Gdx.input.setInputProcessor(this.inputProcessor);

        if (false) {
            this.client = new ClientBuilder<WebChessSession>()
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
                                ClientMain.this.renderManager.<BackgroundRenderer>getRenderer(RenderManager.RenderType.BACKGROUND).setRgb(((UpdateColorPacket) packet).color);
                            }
                        }
                    })
                    .build();
        }

        //this.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.app.debug("AAA", "jeff");
    }

    @Override
    public void render() {
        batch.begin();

        this.renderManager.render(0, 0); //TODO
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        ChessTex.dispose();
        this.renderManager.dispose();

        if (this.client != null && this.client.isRunning()) {
            this.client.close("User exit");
        }
    }

    @Override
    public void resize(int width, int height) {
        if (false)   {
            Gdx.gl.glViewport(0, 0, width, height);
            return;
        }
        if (((float) width / ASPECT_W) < ((float) height / ASPECT_H)) {
            //width should remain, height needs to be scaled
            int h = Math.round(width / (ASPECT_W / ASPECT_H));
            this.glViewport(0, (height - h) >> 1, width, this.android ? h + ((height - h) >> 1) : h);
        } else if (((float) width / ASPECT_W) > ((float) height / ASPECT_H)) {
            int w = Math.round(height * (ASPECT_W / ASPECT_H));
            this.glViewport((width - w) >> 1, 0, w, height);
        } else {
            this.glViewport(0, 0, width, height);
        }
    }

    private void glViewport(int x, int y, int width, int height)    {
        coordinateOffset.setX(x);
        coordinateOffset.setY(y);
        coordinateOffset.setWidth(width);
        coordinateOffset.setHeight(height);
        Gdx.gl.glViewport(x, y, width, height);
        //Gdx.app.debug("SCALE", String.format("Pos: (%d,%d), res: %dx%d\n", x, y, width, height));
        Vector3 scale = new Vector3((float) width / (float) TARGET_WIDTH, (float) height / (float) TARGET_HEIGHT, 1);
        coordinateOffset.setXScale(scale.x);
        coordinateOffset.setYScale(scale.y);
        if (this.android) { //TODO: fix scaling on desktop
            batch.setTransformMatrix(new Matrix4(new Vector3(), new Quaternion(), scale));
        }
    }
}
