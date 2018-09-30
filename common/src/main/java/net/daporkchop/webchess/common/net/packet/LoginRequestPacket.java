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
public class LoginRequestPacket implements Packet {
    public LoginRequestType type;
    public String username;
    public byte[] password;

    @Override
    public void read(DataIn in) throws IOException {
        this.type = LoginRequestType.values()[in.read()];
        this.username = in.readUTF();
        this.password = in.readBytesSimple();
    }

    @Override
    public void write(DataOut out) throws IOException {
        out.write(this.type.ordinal());
        out.writeUTF(this.username);
        out.writeBytesSimple(this.password);
    }

    public enum LoginRequestType    {
        LOG_IN,
        REGISTER
        ;
    }

    public static class LoginRequestCodec<S extends WebChessSession> implements Codec<LoginRequestPacket, S> {
        @Override
        public void handle(LoginRequestPacket packet, S session) {
            ((WebChessSession.ServerSession) session).handle(packet);
        }

        @Override
        public LoginRequestPacket newPacket() {
            return new LoginRequestPacket();
        }
    }
}
