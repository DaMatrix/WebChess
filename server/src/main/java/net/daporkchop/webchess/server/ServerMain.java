package net.daporkchop.webchess.server;

import net.daporkchop.lib.crypto.cipher.symmetric.BlockCipherMode;
import net.daporkchop.lib.crypto.cipher.symmetric.BlockCipherType;
import net.daporkchop.lib.crypto.cipher.symmetric.padding.BlockCipherPadding;
import net.daporkchop.lib.crypto.sig.ec.CurveType;
import net.daporkchop.lib.encoding.compression.EnumCompression;
import net.daporkchop.lib.network.builder.ServerBuilder;
import net.daporkchop.lib.network.endpoint.AbstractSession;
import net.daporkchop.lib.network.endpoint.server.Server;
import net.daporkchop.lib.network.protocol.encapsulated.session.ConnectionState;
import net.daporkchop.webchess.common.net.WebChessProtocol;
import net.daporkchop.webchess.common.net.packet.ColorPacket;
import net.daporkchop.webchess.server.util.ServerConstants;
import org.apache.mina.core.session.IoSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author DaPorkchop_
 */
public class ServerMain implements ServerConstants {
    public static void main(String... args) throws InterruptedException {
        List<AbstractSession> sessions = new ArrayList<>();

        Server server = new ServerBuilder()
                .setPort(NETWORK_PORT)
                .setCompression(EnumCompression.GZIP)
                .setPacketProtocol(new WebChessProtocol(){
                    @Override
                    public AbstractSession newSession(IoSession session, boolean server) {
                        AbstractSession a = super.newSession(session, server);
                        sessions.add(a);
                        return a;
                    }
                })
                .build();

        AtomicBoolean running = new AtomicBoolean(true);

        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            scanner.close();
            running.set(false);
        }).start();

        while (running.get())   {
            sessions.removeIf(s -> !s.getSession().isConnected());
            sessions.forEach(s -> {
                if (ConnectionState.get(s) == ConnectionState.RUN) {
                    s.send(new ColorPacket(ThreadLocalRandom.current().nextInt()));
                }
            });
            Thread.sleep(1000L);
        }

        server.close();
    }
}
