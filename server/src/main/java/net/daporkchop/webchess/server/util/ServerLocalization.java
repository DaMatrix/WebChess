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
import java.util.EnumMap;
import java.util.Hashtable;
import java.util.Map;

public class ServerLocalization {
    private static final Map<Locale, Map<String, String>> locales = new EnumMap<>(Locale.class);

    public static void load()   {
        try {
            for (Locale locale : Locale.values())   {
                Map<String, String> map = locales.computeIfAbsent(locale, l -> new Hashtable<>());
                String fileName = String.format("/lang/%s.lang", locale.name().toLowerCase());
                if (false) {
                    System.out.println("Resource '" + locale.name().toLowerCase() + ".lang': " + (ServerMain.class.getResource(locale.name().toLowerCase() + ".lang") != null));
                    System.out.println("Resource '/" + locale.name().toLowerCase() + ".lang': " + (ServerMain.class.getResource('/' + locale.name().toLowerCase() + ".lang") != null));
                    System.out.println("Resource '" + fileName + "': " + (ServerMain.class.getResource(fileName) != null));
                    System.out.println("Resource '/" + fileName + "': " + (ServerMain.class.getResource('/' + fileName) != null));
                }
                InputStream is = ServerLocalization.class.getResourceAsStream(fileName);
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
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendAll(@NonNull WebChessSessionServer session)  {
        locales.forEach((locale, map) -> session.send(new LocaleDataPacket(locale, map)));
    }
}
