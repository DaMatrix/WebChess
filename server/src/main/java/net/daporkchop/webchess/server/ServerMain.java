package net.daporkchop.webchess.server;

import net.daporkchop.lib.binary.stream.DataIn;
import net.daporkchop.lib.binary.stream.DataOut;
import net.daporkchop.lib.crypto.CryptographySettings;
import net.daporkchop.lib.crypto.cipher.symmetric.BlockCipherMode;
import net.daporkchop.lib.crypto.cipher.symmetric.BlockCipherType;
import net.daporkchop.lib.crypto.cipher.symmetric.padding.BlockCipherPadding;
import net.daporkchop.lib.crypto.sig.ec.CurveType;
import net.daporkchop.lib.db.DBBuilder;
import net.daporkchop.lib.db.DatabaseFormat;
import net.daporkchop.lib.db.PorkDB;
import net.daporkchop.lib.db.object.key.impl.HashKeyHasher;
import net.daporkchop.lib.db.object.key.impl.StringKeyHasher;
import net.daporkchop.lib.db.object.serializer.ValueSerializer;
import net.daporkchop.lib.encoding.compression.EnumCompression;
import net.daporkchop.lib.hash.HashAlg;
import net.daporkchop.lib.network.endpoint.builder.ServerBuilder;
import net.daporkchop.lib.network.endpoint.server.PorkServer;
import net.daporkchop.webchess.common.net.WebChessProtocol;
import net.daporkchop.webchess.common.user.User;
import net.daporkchop.webchess.server.net.ServerListener;
import net.daporkchop.webchess.server.net.WebChessSessionServer;
import net.daporkchop.webchess.server.util.ServerLocalization;
import net.daporkchop.webchess.server.util.ServerConstants;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author DaPorkchop_
 */
public class ServerMain implements ServerConstants {
    public PorkServer<WebChessSessionServer> netServer;
    public PorkDB<String, User> db;

    public static final ServerMain INSTANCE = new ServerMain();

    public static void main(String... args) {
        INSTANCE.start();
        AtomicBoolean running = new AtomicBoolean(true);

        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            String s;
            while (!(s = scanner.nextLine()).isEmpty()) {
                switch (s)  {
                    case "reloadlang":
                        ServerLocalization.load();
                        break;
                    case "help":
                        System.out.println("reloadlang"); //TODO: better
                        break;
                }
            }
            scanner.close();
            running.set(false);
        }).start();

        try {
            while (running.get())   {
                Thread.sleep(1000L);
            }
        } catch (InterruptedException e)    {
            e.printStackTrace();
        } finally {
            INSTANCE.stop();
        }
    }

    public void start() {
        ServerLocalization.load();

        this.netServer = new ServerBuilder<WebChessSessionServer>()
                .setCompression(EnumCompression.GZIP)
                .setCryptographySettings(new CryptographySettings(
                        CurveType.brainpoolp256t1,
                        BlockCipherType.AES,
                        BlockCipherMode.CBC,
                        BlockCipherPadding.PKCS7
                ))
                .setProtocol(new WebChessProtocol<>(() -> new WebChessSessionServer(this)))
                .setAddress(new InetSocketAddress(NETWORK_PORT))
                .addListener(new ServerListener(this))
                .build();

        this.db = new DBBuilder<String, User>()
                .setKeyHasher(new StringKeyHasher(new HashKeyHasher(HashAlg.SHA_256)))
                .setValueSerializer(new ValueSerializer<User>() {
                    @Override
                    public void write(User value, DataOut out) throws IOException {
                        value.write(out);
                    }

                    @Override
                    public User read(DataIn in) throws IOException {
                        User user = new User();
                        user.read(in);
                        return user;
                    }
                })
                .setRootFolder(new File(".", "users"))
                .setFormat(DatabaseFormat.ZIP_TREE)
                .setForceOpen(true)
                .build();
    }

    public void stop()  {
        this.netServer.close("Server closing");
        this.db.shutdown();
    }
}
