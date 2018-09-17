package net.daporkchop.webchess.common.net;

import net.daporkchop.lib.network.protocol.Packet;
import net.daporkchop.lib.network.protocol.PacketProtocol;
import net.daporkchop.webchess.common.util.Constants;
import org.apache.mina.core.session.IoSession;

import java.util.function.BiFunction;

/**
 * @author DaPorkchop_
 */
public class WebChessProtocol<T extends WebChessSession> extends PacketProtocol<Packet, T> implements Constants {
    private final BiFunction<IoSession, Boolean, T> sessionSupplier;

    public WebChessProtocol(BiFunction<IoSession, Boolean, T> sessionSupplier) {
        super(PROTOCOL_VERSION, "WebChess");

        assert sessionSupplier != null;

        this.sessionSupplier = sessionSupplier;
    }

    @Override
    public T newSession(IoSession session, boolean server) {
        return this.sessionSupplier.apply(session, server);
    }
}
