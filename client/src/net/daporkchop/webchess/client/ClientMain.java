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
import net.daporkchop.webchess.client.gui.Gui;
import net.daporkchop.webchess.client.input.BaseInputProcessor;
import net.daporkchop.webchess.client.net.WebChessSessionClient;
import net.daporkchop.webchess.client.render.RenderManager;
import net.daporkchop.webchess.client.render.impl.BackgroundRenderer;
import net.daporkchop.webchess.client.util.*;
import net.daporkchop.webchess.common.net.WebChessProtocol;
import net.daporkchop.webchess.common.net.WebChessSession;
import net.daporkchop.webchess.common.net.packet.UpdateColorPacket;
import net.daporkchop.webchess.common.user.User;

import java.net.InetSocketAddress;
import java.util.Hashtable;
import java.util.Map;
import java.util.WeakHashMap;

@RequiredArgsConstructor
public class ClientMain extends ApplicationAdapter implements ClientConstants {
    @NonNull
    private final String localAddress;
    @Getter
    private final BaseInputProcessor inputProcessor = new BaseInputProcessor(this);
    public PorkClient<WebChessSession> client;
    @Getter
    private RenderManager renderManager;
    private final boolean android;

    public final LoginData loginData = new LoginData(this);
    public User user;
    public Map<String, User> cachedUsers = new Hashtable<>();

    @Getter
    private Gui currentGui = new Gui(this) {};

    @SuppressWarnings("unchecked")
    @Override
    public void create() {
        init();
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        this.renderManager = new RenderManager(this);
        this.renderManager.create();

        this.loginData.create();

        Gdx.input.setInputProcessor(this.inputProcessor);

        if (true) {
            this.client = (PorkClient<WebChessSession>) new ClientBuilder<WebChessSession>()
                    .setProtocol(new WebChessProtocol(() -> new WebChessSessionClient(this)))
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

        if (false)   {
            //debug: test localization replacement
            Localization.waitForReceive();
            for (String s : new String[]{"test.test1", "test.test2", "test.test3"}){
                System.out.println(localize(s, "a", "ab", "abc"));
            }
            for (String s : new String[]{"test.test1", "test.test2", "test.test3"}){
                System.out.println(localize(s, 123, 456, 789));
            }
            System.exit(0);
            return;
        }

        //this.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render() {
        if (!this.loginData.isReady())  {
            this.loginData.prompt();
            return;
        }

        batch.begin();

        this.renderManager.render(0, 0); //TODO
        batch.end();
    }

    @Override
    public void dispose() {
        ClientConstants.super.dispose();
        this.renderManager.dispose();

        this.loginData.dispose();

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

    public void setCurrentGui(@NonNull Gui gui) {
        gui.create();
        Gui old = this.currentGui;
        this.currentGui = gui;
        old.dispose();
    }
}
