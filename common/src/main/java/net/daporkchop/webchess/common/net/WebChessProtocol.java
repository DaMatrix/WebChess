package net.daporkchop.webchess.common.net;

import net.daporkchop.lib.network.endpoint.AbstractSession;
import net.daporkchop.lib.network.protocol.Packet;
import net.daporkchop.lib.network.protocol.PacketProtocol;
import net.daporkchop.webchess.common.net.packet.ColorPacket;
import net.daporkchop.webchess.common.util.Constants;
import org.apache.mina.core.session.IoSession;

import java.util.function.BiFunction;

/**
 * @author DaPorkchop_
 */
public class WebChessProtocol extends PacketProtocol<Packet, AbstractSession> implements Constants {

    public WebChessProtocol() {
        super(PROTOCOL_VERSION, "WebChess");

        this.registerPacket(ColorPacket::new, new ColorPacket.ColorPacketHandler());
    }

    @Override
    public AbstractSession newSession(IoSession session, boolean server) {
        return new AbstractSession.NoopSession(session, server);
    }
}
