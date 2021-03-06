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

package net.daporkchop.webchess.client.util;

import lombok.NonNull;
import net.daporkchop.webchess.client.ClientMain;
import net.daporkchop.webchess.client.gui.GuiLanguageSelector;
import net.daporkchop.webchess.client.gui.GuiLoggingIn;
import net.daporkchop.webchess.common.net.packet.LocaleDataPacket;
import net.daporkchop.webchess.common.util.locale.Locale;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author DaPorkchop_
 */
public class Localization implements ClientConstants {
    private static final Map<Locale, Map<String, String>> locales = new EnumMap<>(Locale.class);
    private static Map<String, String> currentMap;
    private static Locale currentLocale;

    public static void initLocales(@NonNull ClientMain client) {
        Locale locale;
        if (prefs.contains("locale")) {
            locale = Locale.valueOf(prefs.getString("locale"));
        } else {
            //locale = Locale.EN_US;
            //prefs.putString("locale", locale.name());
            client.setGui(new GuiLanguageSelector(client));
            return;
        }
        setLocale(locale);
    }

    public static void receiveLocales(@NonNull LocaleDataPacket packet) {
        locales.put(packet.locale, packet.mappings);
    }

    public static void setLocale(@NonNull Locale locale) {
        if (currentLocale == null) {
            ClientMain.INSTANCE.setGui(new GuiLoggingIn(ClientMain.INSTANCE, "login.gui.waiting"));
        }
        currentLocale = locale;
        currentMap = locales.get(locale);
        prefs.putString("locale", locale.name());
    }

    public static String localize(String key) {
        if (key == null) {
            return "null";
        }
        if (currentMap == null) {
            if (locales.containsKey(currentLocale)) {
                currentMap = locales.get(currentLocale);
            } else {
                return key;
            }
        }
        return currentMap.getOrDefault(key, key);
    }

    public static boolean isLocaleSet() {
        return currentLocale != null;
    }

    public static boolean hasReceivedAll() {
        return locales.size() == Locale.values().length;
    }

    public static boolean hasReceivedCurrent() {
        return isLocaleSet() && locales.containsKey(currentLocale);
    }

    public static void waitForReceive() {
        if (!isLocaleSet()) {
            return;
        }
        try {
            while (!hasReceivedCurrent()) {
                Thread.sleep(1L);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
