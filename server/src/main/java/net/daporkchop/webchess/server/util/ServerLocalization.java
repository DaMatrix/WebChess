package net.daporkchop.webchess.server.util;

import lombok.NonNull;
import net.daporkchop.webchess.common.net.packet.LocaleDataPacket;
import net.daporkchop.webchess.common.util.locale.Locale;
import net.daporkchop.webchess.server.ServerMain;
import net.daporkchop.webchess.server.net.WebChessSessionServer;
import sun.misc.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ServerLocalization implements ServerConstants {
    private static final Map<Locale, Map<String, String>> locales = new EnumMap<>(Locale.class);
    private static final Collection<LocaleDataPacket> packets = new ArrayDeque<>();

    public synchronized static void load() {
        try {
            int localeCount = 0;
            int mappingCount = 0;
            for (Locale locale : Locale.values()) {
                Map<String, String> map = locales.computeIfAbsent(locale, l -> new Hashtable<>());
                map.clear();
                String fileName = String.format("/lang/%s.lang", locale.name().toLowerCase());
                if (false) {
                    System.out.println("Resource '" + locale.name().toLowerCase() + ".lang': " + (ServerMain.class.getResource(locale.name().toLowerCase() + ".lang") != null));
                    System.out.println("Resource '/" + locale.name().toLowerCase() + ".lang': " + (ServerMain.class.getResource('/' + locale.name().toLowerCase() + ".lang") != null));
                    System.out.println("Resource '" + fileName + "': " + (ServerMain.class.getResource(fileName) != null));
                    System.out.println("Resource '/" + fileName + "': " + (ServerMain.class.getResource('/' + fileName) != null));
                }
                InputStream is;
                if (IDE)    {
                    is = new FileInputStream(new File(".", "../src/main/resources" + fileName));
                } else {
                    is = ServerLocalization.class.getResourceAsStream(fileName);
                }
                //InputStream is = new FileInputStream(new File(".", fileName));
                byte[] b = IOUtils.readFully(is, -1, false);
                is.close();
                String[] values = new String(b).split("\n");
                for (String s : values) {
                    s = s.trim();
                    if (s.isEmpty()) {
                        continue;
                    }
                    String[] split = s.split("=");
                    map.put(split[0], split[1]);
                    mappingCount++;
                }
                localeCount++;
            }
            System.out.printf("Loaded %d locales with a total of %d mappings\n", localeCount, mappingCount);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        synchronized (packets)  {
            packets.clear();
            locales.forEach((locale, map) -> packets.add(new LocaleDataPacket(locale, map)));
        }

        if (ServerMain.INSTANCE.netServer != null && ServerMain.INSTANCE.netServer.isRunning()){
            ServerMain.INSTANCE.netServer.getSessions().forEach(ServerLocalization::sendLocales);
        }
    }

    public static void sendLocales(@NonNull WebChessSessionServer session) {
        synchronized (packets)  {
            packets.forEach(session::send);
        }
    }
}
