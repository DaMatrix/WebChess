package net.daporkchop.webchess.common.net.packet;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.daporkchop.lib.binary.stream.DataIn;
import net.daporkchop.lib.binary.stream.DataOut;
import net.daporkchop.lib.network.packet.Codec;
import net.daporkchop.lib.network.packet.Packet;
import net.daporkchop.webchess.common.net.WebChessSession;

import java.io.IOException;

/**
 * @author DaPorkchop_
 */
@Deprecated
@NoArgsConstructor
@AllArgsConstructor
public class UpdateColorPacket implements Packet {
    public int color;

    @Override
    public void read(DataIn in) throws IOException {
        this.color = in.readInt();
    }

    @Override
    public void write(DataOut out) throws IOException {
        out.writeInt(this.color);
    }

    public static class UpdateColorCodec<S extends WebChessSession> implements Codec<UpdateColorPacket, S>   {
        @Override
        public void handle(UpdateColorPacket packet, S session) {
            throw new IllegalStateException("This packet is only here for debug purposes!");
        }

        @Override
        public UpdateColorPacket newPacket() {
            return new UpdateColorPacket();
        }
    }
}
