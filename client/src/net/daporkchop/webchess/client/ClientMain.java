package net.daporkchop.webchess.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.daporkchop.lib.network.endpoint.EndpointListener;
import net.daporkchop.lib.network.endpoint.builder.ClientBuilder;
import net.daporkchop.lib.network.endpoint.client.PorkClient;
import net.daporkchop.lib.network.packet.Packet;
import net.daporkchop.webchess.client.input.BaseInputProcessor;
import net.daporkchop.webchess.client.net.WebChessSessionClient;
import net.daporkchop.webchess.client.render.impl.BackgroundRenderer;
import net.daporkchop.webchess.client.render.RenderManager;
import net.daporkchop.webchess.client.util.ChessTex;
import net.daporkchop.webchess.client.util.ClientConstants;
import net.daporkchop.webchess.client.util.Localization;
import net.daporkchop.webchess.common.net.WebChessProtocol;
import net.daporkchop.webchess.common.net.WebChessSession;
import net.daporkchop.webchess.common.net.packet.UpdateColorPacket;

import java.net.InetSocketAddress;

@RequiredArgsConstructor
public class ClientMain extends ApplicationAdapter implements ClientConstants {
    @NonNull
    private final String localAddress;
    private PorkClient<WebChessSession> client;
    @Getter
    private RenderManager renderManager;
    @Getter
    private final BaseInputProcessor inputProcessor = new BaseInputProcessor(this);

    @Override
    public void create() {
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
}
