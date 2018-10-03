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
import net.daporkchop.webchess.client.gui.GuiDisconnected;
import net.daporkchop.webchess.client.gui.GuiLoggingIn;
import net.daporkchop.webchess.client.input.BaseInputProcessor;
import net.daporkchop.webchess.client.net.WebChessSessionClient;
import net.daporkchop.webchess.client.render.RenderManager;
import net.daporkchop.webchess.client.render.impl.BackgroundRenderer;
import net.daporkchop.webchess.client.util.ClientConstants;
import net.daporkchop.webchess.client.util.Localization;
import net.daporkchop.webchess.client.util.LoginData;
import net.daporkchop.webchess.common.net.WebChessProtocol;
import net.daporkchop.webchess.common.net.WebChessSession;
import net.daporkchop.webchess.common.net.packet.UpdateColorPacket;
import net.daporkchop.webchess.common.user.User;

import java.net.InetSocketAddress;
import java.util.Hashtable;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

@RequiredArgsConstructor
public class ClientMain extends ApplicationAdapter implements ClientConstants {
    public static ClientMain INSTANCE;
    public final LoginData loginData = new LoginData(this);
    @NonNull
    private final String localAddress;
    @Getter
    private final BaseInputProcessor inputProcessor = new BaseInputProcessor(this);
    public final boolean android;
    public PorkClient<WebChessSession> client;
    public User user;
    public Map<String, User> cachedUsers = new Hashtable<>();
    @Getter
    private RenderManager renderManager;
    private Gui currentGui = new GuiLoggingIn(this, "login.gui.waiting");
    private final Queue<Runnable> renderThreadQueue = new ConcurrentLinkedQueue<>();

    {
        INSTANCE = this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void create() {
        this.init(this);
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
                            ClientMain.this.setGui(new GuiDisconnected(ClientMain.this));
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

        if (false) {
            //debug: test localization replacement
            for (String s : new String[]{"test.test1", "test.test2", "test.test3"}) {
                System.out.println(this.localize(s, "a", "ab", "abc"));
            }
            for (String s : new String[]{"test.test1", "test.test2", "test.test3"}) {
                System.out.println(this.localize(s, 123, 456, 789));
            }
            System.exit(0);
            return;
        }

        Localization.waitForReceive();
    }

    @Override
    public void render() {
        if (!this.loginData.isReady() && Localization.hasReceivedCurrent()) {
            this.loginData.prompt();
            return;
        }

        batch.begin();

        if (!this.renderThreadQueue.isEmpty()){
            Runnable runnable;
            while ((runnable = this.renderThreadQueue.poll()) != null)  {
                runnable.run();
            }
        }

        this.renderManager.render(0, 0); //TODO
        batch.end();
    }

    @Override
    public void dispose() {
        ClientConstants.super.dispose();
        this.renderManager.dispose();

        this.loginData.dispose();

        if ((this.client != null) && this.client.isRunning()) {
            this.client.close("User exit");
        }
    }

    @Override
    public void resize(int width, int height) {
        if (false) {
            Gdx.gl.glViewport(0, 0, width, height);
            return;
        }
        if (((float) width / ASPECT_W) < ((float) height / ASPECT_H)) {
            //width should remain, height needs to be scaled
            int h = Math.round(width / (ASPECT_W / ASPECT_H));
            this.glViewport(0, (height - h) >> 1, width, this.android ? (h + ((height - h) >> 1)) : h);
        } else if (((float) width / ASPECT_W) > ((float) height / ASPECT_H)) {
            int w = Math.round(height * (ASPECT_W / ASPECT_H));
            this.glViewport((width - w) >> 1, 0, w, height);
        } else {
            this.glViewport(0, 0, width, height);
        }
    }

    private void glViewport(int x, int y, int width, int height) {
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

    @SuppressWarnings("unchecked")
    public <T extends Gui> T getGui() {
        return (T) this.currentGui;
    }

    public void setGui(@NonNull Gui gui) {
        gui.create();
        Gui old = this.currentGui;
        this.currentGui = gui;
        old.dispose();
    }

    public void runOnRenderThread(@NonNull Runnable runnable)   {
        this.renderThreadQueue.add(runnable);
    }
}
