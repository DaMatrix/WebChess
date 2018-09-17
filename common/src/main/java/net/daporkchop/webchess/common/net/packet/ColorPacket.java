package net.daporkchop.webchess.common.net.packet;

import net.daporkchop.lib.binary.stream.DataIn;
import net.daporkchop.lib.binary.stream.DataOut;
import net.daporkchop.lib.network.endpoint.AbstractSession;
import net.daporkchop.lib.network.protocol.Packet;
import net.daporkchop.lib.network.protocol.PacketDirection;
import net.daporkchop.lib.network.protocol.PacketHandler;
import net.daporkchop.webchess.common.util.Debug;

import java.io.IOException;

/**
 * @author DaPorkchop_
 */
public class ColorPacket implements Packet {
    public int color;

    public ColorPacket() {
    }

    public ColorPacket(int color) {
        this.color = color;
    }

    @Override
    public void read(DataIn in) throws IOException {
        this.color = in.readInt();
    }

    @Override
    public void write(DataOut out) throws IOException {
        out.writeInt(this.color);
    }

    @Override
    public PacketDirection getDirection() {
        return PacketDirection.CLIENTBOUND;
    }

    @Override
    public byte getId() {
        return 0;
    }

    public static final class ColorPacketHandler implements PacketHandler<ColorPacket, AbstractSession>   {
        @Override
        public void handle(ColorPacket packet, AbstractSession session) {
            Debug.colorSet.accept(packet.color);
        }
    }
}
