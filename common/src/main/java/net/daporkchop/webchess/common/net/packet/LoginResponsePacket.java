package net.daporkchop.webchess.common.net.packet;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.daporkchop.lib.binary.stream.DataIn;
import net.daporkchop.lib.binary.stream.DataOut;
import net.daporkchop.lib.network.packet.Codec;
import net.daporkchop.lib.network.packet.Packet;
import net.daporkchop.webchess.common.net.WebChessSession;

import java.io.IOException;

@NoArgsConstructor
@AllArgsConstructor
public class LoginResponsePacket implements Packet {
    public LoginResponseType type;

    @Override
    public void read(DataIn in) throws IOException {
        this.type = LoginResponseType.values()[in.read()];
    }

    @Override
    public void write(DataOut out) throws IOException {
        out.write(this.type.ordinal());
    }

    public enum LoginResponseType {
        LOGIN_SUCCESS,
        LOGIN_FAILED_INVALID_CREDENTIALS,
        LOGIN_FAILED_ALREADY_LOGGED_IN,
        REGISTER_SUCCESS,
        REGISTER_FAILED_USERNAME_TAKEN,
        REGISTER_FAILED_USERNAME_INVALID,
        REGISTER_FAILED_PASSWORD_INVALID
        ;
    }

    public static class LoginResponseCodec<S extends WebChessSession> implements Codec<LoginResponsePacket, S> {
        @Override
        public void handle(LoginResponsePacket packet, S session) {
            ((WebChessSession.ClientSession) session).handle(packet);
        }

        @Override
        public LoginResponsePacket newPacket() {
            return new LoginResponsePacket();
        }
    }
}
