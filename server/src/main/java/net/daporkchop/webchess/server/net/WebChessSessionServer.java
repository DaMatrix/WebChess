package net.daporkchop.webchess.server.net;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.daporkchop.webchess.common.net.WebChessSession;
import net.daporkchop.webchess.common.net.packet.LoginRequestPacket;
import net.daporkchop.webchess.common.net.packet.LoginResponsePacket;
import net.daporkchop.webchess.common.user.User;
import net.daporkchop.webchess.server.ServerMain;

import java.util.Arrays;

/**
 * @author DaPorkchop_
 */
@RequiredArgsConstructor
public class WebChessSessionServer extends WebChessSession implements WebChessSession.ServerSession {
    private static final char[] LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-_.:,;öéäàüè+\"*ç%&/()=?$£¨!'^¦@#¬|¢[]{}".toCharArray();

    @NonNull
    public final ServerMain server;

    @Override
    public void handle(LoginRequestPacket packet) {
        LoginResponsePacket response = new LoginResponsePacket();
        switch (packet.type) {
            case LOG_IN: {
                if (this.user != null) {
                    response.type = LoginResponsePacket.LoginResponseType.LOGIN_FAILED_ALREADY_LOGGED_IN;
                } else if (!this.server.db.contains(packet.username)) {
                    response.type = LoginResponsePacket.LoginResponseType.LOGIN_FAILED_INVALID_CREDENTIALS;
                } else if (this.server.db.isLoaded(packet.username)) {
                    response.type = LoginResponsePacket.LoginResponseType.LOGIN_FAILED_ALREADY_LOGGED_IN;
                } else {
                    User user = this.server.db.load(packet.username).getValue();
                    if (user == null || !Arrays.equals(user.getPassword(), packet.password)) {
                        response.type = LoginResponsePacket.LoginResponseType.LOGIN_FAILED_INVALID_CREDENTIALS;
                        this.server.db.unload(packet.username);
                    } else {
                        response.type = LoginResponsePacket.LoginResponseType.LOGIN_SUCCESS;
                        this.user = user;
                    }
                }
            }
            break;
            case REGISTER: {
                if (this.user != null) {
                    response.type = LoginResponsePacket.LoginResponseType.REGISTER_FAILED_USERNAME_TAKEN;
                } else if (this.isValid(packet.username)) {
                    response.type = LoginResponsePacket.LoginResponseType.REGISTER_FAILED_USERNAME_INVALID;
                } else if (this.server.db.contains(packet.username)) {
                    response.type = LoginResponsePacket.LoginResponseType.REGISTER_FAILED_USERNAME_TAKEN;
                } else {
                    User user = new User(packet.password, packet.username);
                    this.server.db.put(packet.username, user);
                    response.type = LoginResponsePacket.LoginResponseType.REGISTER_SUCCESS;
                }
            }
            break;
        }
        this.send(response);
    }

    private boolean isValid(@NonNull String name) {
        for (int i = name.length() - 1; i >= 0; i--) {
            char c = name.charAt(i);
            boolean contains = false;
            for (int j = LETTERS.length - 1; j >= 0; j--) {
                if (LETTERS[j] == c) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                return false;
            }
        }

        return true;
    }
}
