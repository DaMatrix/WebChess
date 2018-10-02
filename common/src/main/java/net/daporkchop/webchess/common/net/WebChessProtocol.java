package net.daporkchop.webchess.common.net;

import net.daporkchop.lib.network.endpoint.Endpoint;
import net.daporkchop.lib.network.packet.protocol.PacketProtocol;
import net.daporkchop.webchess.common.net.packet.*;

import java.util.function.Supplier;

/**
 * @author DaPorkchop_
 */
public class WebChessProtocol<S extends WebChessSession> extends PacketProtocol<S> {
    static {
        if (false) {
            Endpoint.DEBUG = true;
        }
    }

    private final Supplier<S> sessionSupplier;

    public WebChessProtocol(Supplier<S> sessionSupplier) {
        super("DaPorkchop_'s WebChess", 1);

        this.sessionSupplier = sessionSupplier;
    }

    @Override
    protected void registerPackets(PacketRegistry registry) {
        registry.register(
                new LoginRequestPacket.LoginRequestCodec(),
                new LoginResponsePacket.LoginResponseCodec(),
                new UserDataPacket.UserDataCodec(),
                new LocaleDataPacket.LocaleDataCodec()
        );
    }

    @Override
    public S newSession() {
        return this.sessionSupplier.get();
    }
}
