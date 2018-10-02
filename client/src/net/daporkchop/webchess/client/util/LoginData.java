package net.daporkchop.webchess.client.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.daporkchop.lib.binary.UTF8;
import net.daporkchop.lib.encoding.Hexadecimal;
import net.daporkchop.lib.encoding.basen.Base58;
import net.daporkchop.lib.hash.helper.sha.Sha512Helper;
import net.daporkchop.webchess.client.ClientMain;
import net.daporkchop.webchess.client.gui.GuiLoggingIn;
import net.daporkchop.webchess.common.net.packet.LoginRequestPacket;
import net.daporkchop.webchess.common.net.packet.LoginResponsePacket;
import net.daporkchop.webchess.common.net.packet.UserDataPacket;

import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor
public class LoginData implements ClientConstants, Disposable {
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
    public void create()  {
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
        if (!this.isReady() && this.listener == null && Localization.hasReceivedCurrent()) {
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
                this.client.setCurrentGui(new GuiLoggingIn(this.client, "login.inprogress"));
                this.client.client.send(new LoginRequestPacket(
                        LoginRequestPacket.LoginRequestType.LOG_IN,
                        this.username,
                        this.password
                ));
                this.listener = new Input.TextInputListener() {@Override public void input(String text) {}@Override public void canceled() {}};
                return;
            }
            String title = Localization.localize(String.format("login.prompt.%s.title", msg)); //TODO: localization formatting
            String hint = Localization.localize(String.format("login.prompt.%s.hint", msg));
            Gdx.input.getTextInput(this.listener = new Input.TextInputListener() {
                @Override
                public void input(String text) {
                    if (inputValidator.apply(text)) {
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
                this.prefs.putString("username", this.username);
                this.prefs.putString("password", Base58.encodeBase58(this.password));
                this.ready = true;
            }
            break;
            case LOGIN_FAILED_ACCOUNT_NOT_EXIST: {
                this.client.client.send(new LoginRequestPacket(
                        LoginRequestPacket.LoginRequestType.REGISTER,
                        this.username,
                        this.password
                ));
                this.listener = new Input.TextInputListener() {@Override public void input(String text) {}@Override public void canceled() {}};
                //TODO: update message
            }
            break;
            default: {
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
