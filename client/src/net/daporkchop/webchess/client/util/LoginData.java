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

package net.daporkchop.webchess.client.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.daporkchop.lib.binary.UTF8;
import net.daporkchop.lib.encoding.basen.Base58;
import net.daporkchop.lib.hash.helper.sha.Sha512Helper;
import net.daporkchop.webchess.client.ClientMain;
import net.daporkchop.webchess.client.gui.GuiLoggingIn;
import net.daporkchop.webchess.client.gui.GuiMainMenu;
import net.daporkchop.webchess.common.net.packet.LoginRequestPacket;
import net.daporkchop.webchess.common.net.packet.LoginResponsePacket;
import net.daporkchop.webchess.common.net.packet.UserDataPacket;

import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor
public class LoginData implements Disposable {
    @NonNull
    public final ClientMain client;

    public String username;
    private byte[] password;

    private volatile boolean ready;

    private Input.TextInputListener listener;

    public boolean isReady() {
        return this.ready;
    }

    @Override
    public void create() {
        if (prefs.contains("username")) {
            this.username = prefs.getString("username");
        }
        if (prefs.contains("password")) {
            this.password = Base58.decodeBase58(prefs.getString("password"));
        }
    }

    @Override
    public void dispose() {
        this.prefs.flush();
    }

    public void prompt() {
        if (!this.isReady() && (this.listener == null)) {
            String msg;
            Consumer<String> consumer;
            Function<String, Boolean> inputValidator;
            if (this.username == null) {
                consumer = s -> this.username = s;
                inputValidator = this::ensureUsernameValid;
                msg = "username";
            } else if (this.password == null) {
                consumer = s -> this.password = Sha512Helper.sha512(s.getBytes(UTF8.utf8));
                inputValidator = s -> s.length() >= 4;
                msg = "password";
            } else {
                //actually log in
                this.client.<GuiLoggingIn>getGui().setMessage("login.gui.login");
                this.client.client.send(new LoginRequestPacket(
                        LoginRequestPacket.LoginRequestType.LOG_IN,
                        this.username,
                        this.password
                ));
                this.listener = new Input.TextInputListener() {
                    @Override
                    public void input(String text) {
                    }

                    @Override
                    public void canceled() {
                    }
                };
                return;
            }
            String title = this.localize(String.format("login.prompt.%s.title", msg)); //TODO: localization formatting
            String hint = this.localize(String.format("login.prompt.%s.hint", msg));
            String invalidMessage = String.format("login.gui.%sinvalid", msg);
            Gdx.input.getTextInput(this.listener = new Input.TextInputListener() {
                @Override
                public void input(String text) {
                    if (inputValidator.apply(text)) {
                        LoginData.this.client.<GuiLoggingIn>getGui().setMessage(invalidMessage, USERNAME_LENGTH_MIN, USERNAME_LENGTH_MAX);
                        consumer.accept(text);
                        LoginData.this.listener = null;
                    } else {
                        this.canceled();
                    }
                }

                @Override
                public void canceled() {
                    Gdx.input.getTextInput(this, title, "", hint);
                }
            }, title, "", hint);
        }
    }

    public void handle(@NonNull LoginResponsePacket packet) {
        if (this.isReady()) {
            throw new IllegalStateException("Already ready!");
        }

        switch (packet.type) {
            case LOGIN_SUCCESS:
            case REGISTER_SUCCESS: {
                this.client.setGui(new GuiMainMenu(this.client));
                this.prefs.putString("username", this.username);
                this.prefs.putString("password", Base58.encodeBase58(this.password));
                this.ready = true;
            }
            break;
            case LOGIN_FAILED_ACCOUNT_NOT_EXIST: {
                this.client.<GuiLoggingIn>getGui().setMessage("login.gui.register");
                this.client.client.send(new LoginRequestPacket(
                        LoginRequestPacket.LoginRequestType.REGISTER,
                        this.username,
                        this.password
                ));
                this.listener = new Input.TextInputListener() {
                    @Override
                    public void input(String text) {
                    }

                    @Override
                    public void canceled() {
                    }
                };
            }
            break;
            default: {
                this.client.<GuiLoggingIn>getGui().setMessage("login.gui.invalidcredentials");
                this.username = null;
                this.password = null;
                this.listener = null;
                this.prompt();
            }
            break;
        }
    }

    public void handle(@NonNull UserDataPacket packet) {
        if (this.username.equals(packet.user.getName())) {
            this.client.user = packet.user;
        }
    }
}
