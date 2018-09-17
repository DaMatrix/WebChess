package net.daporkchop.webchess.client.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.EnumMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author DaPorkchop_
 */
public class Localization {
    private static final Map<Locale, Map<String, String>> locales = new EnumMap<>(Locale.class);
    private static Map<String, String> currentMap;
    private static Locale currentLocale = Locale.EN_US;

    public static void init()   {
        for (Locale locale : Locale.values()) {
            Map<String, String> map = locales.computeIfAbsent(locale, l -> new Hashtable<>());
            FileHandle fileHandle = Gdx.files.internal(String.format("lang/%s.txt", locale.name().toLowerCase()));
            String[] values = fileHandle.readString("UTF-8").split("\n");
            for (String s : values) {
                s = s.trim();
                if (s.isEmpty()) {
                    continue;
                }
                String[] split = s.split("=");
                map.put(split[0], split[1]);
            }
        }
        currentMap = locales.get(currentLocale);
    }

    public static void setLocale(Locale locale) {
        assert locale != null;

        currentLocale = locale;
        currentMap = locales.get(locale);
    }

    public static String localize(String key)  {
        if (key == null)    {
            return "null";
        }
        return currentMap.getOrDefault(key, key);
    }

    public enum Locale {
        EN_US("English (US)"),
        DE_DE("Deutsch")
        ;

        public final String displayName;

        Locale(String displayName) {
            assert displayName != null;

            this.displayName = displayName;
        }
    }
}
