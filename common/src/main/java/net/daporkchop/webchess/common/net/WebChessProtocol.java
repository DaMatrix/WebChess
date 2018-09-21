package net.daporkchop.webchess.common.net;

import net.daporkchop.lib.network.packet.protocol.PacketProtocol;
import net.daporkchop.webchess.common.net.packet.UpdateColorPacket;

import java.util.function.Supplier;

/**
 * @author DaPorkchop_
 */
public class WebChessProtocol extends PacketProtocol<WebChessSession> {
    private final Supplier<WebChessSession> sessionSupplier;

    public WebChessProtocol(Supplier<WebChessSession> sessionSupplier) {
        super("DaPorkchop_'s WebChess", 1);

        this.sessionSupplier = sessionSupplier;
    }

    @Override
    protected void registerPackets(PacketRegistry registry) {
        registry.register(
                new UpdateColorPacket.UpdateColorCodec()
        );
    }

    @Override
    public WebChessSession newSession() {
        return this.sessionSupplier.get();
    }
}
