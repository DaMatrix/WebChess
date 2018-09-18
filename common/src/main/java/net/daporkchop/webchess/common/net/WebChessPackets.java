package net.daporkchop.webchess.common.net;

import com.esotericsoftware.kryo.Kryo;
import net.daporkchop.webchess.common.net.packet.ColorUpdatePacket;

/**
 * @author DaPorkchop_
 */
public class WebChessPackets {
    public static void registerPackets(Kryo kryo)  {
        assert kryo != null;

        kryo.register(ColorUpdatePacket.class);
    }
}
