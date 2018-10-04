/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2018-2018 DaPorkchop_ and contributors
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it. Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from DaPorkchop_.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original authors of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

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
import net.daporkchop.webchess.server.util.ServerConstants;
import net.daporkchop.webchess.server.util.ServerLocalization;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author DaPorkchop_
 */
public class ServerMain implements ServerConstants {
    public static final ServerMain INSTANCE = new ServerMain();
    public PorkServer<WebChessSessionServer> netServer;
    public PorkDB<String, User> db;
    public final Matcher matcher = new Matcher(this);

    public static void main(String... args) {
        INSTANCE.start();
        AtomicBoolean running = new AtomicBoolean(true);

        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            String s;
            while (!(s = scanner.nextLine()).isEmpty()) {
                switch (s) {
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
            while (running.get()) {
                Thread.sleep(1000L);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            INSTANCE.stop();
        }
    }

    public void start() {
        ServerLocalization.load();

        this.matcher.start();

        this.netServer = new ServerBuilder<WebChessSessionServer>()
                .setCompression(EnumCompression.GZIP)
                .setCryptographySettings(IDE ?
                        new CryptographySettings() :
                        new CryptographySettings(
                        CurveType.brainpoolp256t1,
                        BlockCipherType.AES, //BlockCipherType.PORKCRYPT2,
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

    public void stop() {
        this.matcher.stop();
        this.netServer.close("Server closing");
        this.db.shutdown();
    }
}
