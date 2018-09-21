package net.daporkchop.webchess.server;

import net.daporkchop.lib.crypto.CryptographySettings;
import net.daporkchop.lib.crypto.cipher.symmetric.BlockCipherMode;
import net.daporkchop.lib.crypto.cipher.symmetric.BlockCipherType;
import net.daporkchop.lib.crypto.cipher.symmetric.padding.BlockCipherPadding;
import net.daporkchop.lib.crypto.sig.ec.CurveType;
import net.daporkchop.lib.encoding.compression.EnumCompression;
import net.daporkchop.lib.network.endpoint.builder.ServerBuilder;
import net.daporkchop.lib.network.endpoint.server.PorkServer;
import net.daporkchop.webchess.common.net.WebChessProtocol;
import net.daporkchop.webchess.common.net.WebChessSession;
import net.daporkchop.webchess.common.net.packet.UpdateColorPacket;
import net.daporkchop.webchess.server.net.WebChessSessionServer;
import net.daporkchop.webchess.server.util.ServerConstants;

import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author DaPorkchop_
 */
public class ServerMain implements ServerConstants {
    public static void main(String... args) {
        PorkServer<WebChessSession> server = new ServerBuilder<WebChessSession>()
                .setCryptographySettings(new CryptographySettings(
                        CurveType.brainpoolp256t1,
                        BlockCipherType.THREEFISH_1024,
                        BlockCipherMode.CBC,
                        BlockCipherPadding.PKCS7
                ))
                .setProtocol(new WebChessProtocol(WebChessSessionServer::new))
                .setAddress(new InetSocketAddress(NETWORK_PORT))
                .build();

        AtomicBoolean running = new AtomicBoolean(true);

        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            scanner.close();
            running.set(false);
        }).start();

        try {
            while (running.get())   {
                server.broadcast(new UpdateColorPacket(ThreadLocalRandom.current().nextInt()));
                Thread.sleep(1000L);
            }
        } catch (InterruptedException e)    {
            e.printStackTrace();
        }

        server.close();
    }
}
