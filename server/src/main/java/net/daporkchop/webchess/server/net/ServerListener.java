package net.daporkchop.webchess.server.net;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.daporkchop.lib.network.endpoint.EndpointListener;
import net.daporkchop.lib.network.packet.Packet;
import net.daporkchop.webchess.server.ServerMain;
import net.daporkchop.webchess.server.util.ServerLocalization;

@RequiredArgsConstructor
public class ServerListener implements EndpointListener<WebChessSessionServer> {
    @NonNull
    public final ServerMain server;

    @Override
    public void onConnect(WebChessSessionServer session) {
        ServerLocalization.sendAll(session);
    }

    @Override
    public void onDisconnect(WebChessSessionServer session, String reason) {
        if (session.isLoggedIn()) {
            this.server.db.unload(session.getUser().getName());
        }
    }

    @Override
    public void onReceieve(WebChessSessionServer session, Packet packet) {
    }
}
