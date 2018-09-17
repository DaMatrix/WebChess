package net.daporkchop.webchess.common.net;

import net.daporkchop.lib.network.endpoint.AbstractSession;
import org.apache.mina.core.session.IoSession;

/**
 * @author DaPorkchop_
 */
public abstract class WebChessSession extends AbstractSession {
    public WebChessSession(IoSession session, boolean server) {
        super(session, server);
    }
}
