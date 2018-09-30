package net.daporkchop.webchess.common.net.packet;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.daporkchop.lib.binary.stream.DataIn;
import net.daporkchop.lib.binary.stream.DataOut;
import net.daporkchop.lib.network.packet.Codec;
import net.daporkchop.lib.network.packet.Packet;
import net.daporkchop.webchess.common.net.WebChessSession;
import net.daporkchop.webchess.common.user.User;

import java.io.IOException;

@NoArgsConstructor
@AllArgsConstructor
public class UserDataPacket implements Packet {
    public User user;

    @Override
    public void read(DataIn in) throws IOException {
        this.user = new User();
        this.user.read(in, false);
    }

    @Override
    public void write(DataOut out) throws IOException {
        this.user.write(out, false);
    }

    public static class UserDataCodec<S extends WebChessSession> implements Codec<UserDataPacket, S> {
        @Override
        public void handle(UserDataPacket packet, S session) {
            ((WebChessSession.ClientSession) session).handle(packet);
        }

        @Override
        public UserDataPacket newPacket() {
            return new UserDataPacket();
        }
    }
}
