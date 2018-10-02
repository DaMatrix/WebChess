package net.daporkchop.webchess.client.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import lombok.NonNull;
import net.daporkchop.webchess.common.net.packet.LocaleDataPacket;
import net.daporkchop.webchess.common.util.locale.Locale;

import java.util.EnumMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author DaPorkchop_
 */
public class Localization implements ClientConstants {
    private static final Map<Locale, Map<String, String>> locales = new EnumMap<>(Locale.class);
    private static Map<String, String> currentMap;
    private static Locale currentLocale = Locale.EN_US;

    public static void initLocales()   {
        Locale locale;
        if (prefs.contains("locale"))   {
            locale = Locale.valueOf(prefs.getString("locale"));
        } else {
            locale = Locale.EN_US;
            prefs.putString("locale", locale.name());
        }
        setLocale(locale);
    }

    public static void receiveLocales(@NonNull LocaleDataPacket packet) {
        locales.put(packet.locale, packet.mappings);
    }

    public static void setLocale(@NonNull Locale locale) {
        currentLocale = locale;
        currentMap = locales.get(locale);
        prefs.putString("locale", locale.name());
    }

    public static String localize(String key)  {
        if (key == null)    {
            return "null";
        }
        if (currentMap == null) {
            if (locales.containsKey(currentLocale)) {
                currentMap = locales.get(currentLocale);
            } else {
                return "Loading...";
            }
        }
        return currentMap.getOrDefault(key, key);
    }

    public static boolean hasReceivedAll()  {
        return locales.size() == Locale.values().length;
    }

    public static boolean hasReceivedCurrent()  {
        return locales.containsKey(currentLocale);
    }

    public static void waitForReceive() {
        try {
            while (!hasReceivedCurrent())    {
                Thread.sleep(1L);
            }
        } catch (InterruptedException e)    {
            throw new RuntimeException(e);
        }
    }
}
