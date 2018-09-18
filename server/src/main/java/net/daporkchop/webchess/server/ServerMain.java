package net.daporkchop.webchess.server;

import com.esotericsoftware.kryonet.Server;
import net.daporkchop.webchess.common.net.WebChessPackets;
import net.daporkchop.webchess.common.net.packet.ColorUpdatePacket;
import net.daporkchop.webchess.server.util.ServerConstants;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author DaPorkchop_
 */
public class ServerMain implements ServerConstants {
    public static void main(String... args) throws InterruptedException, IOException {
        AtomicBoolean running = new AtomicBoolean(true);

        Server server = new Server();
        WebChessPackets.registerPackets(server.getKryo());
        server.bind(NETWORK_PORT);

        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            scanner.close();
            running.set(false);
        }).start();

        while (running.get())   {
            server.sendToAllTCP(new ColorUpdatePacket(ThreadLocalRandom.current().nextInt()));
            Thread.sleep(500L);
        }

        server.close();
    }
}
