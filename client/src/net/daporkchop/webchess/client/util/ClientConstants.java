package net.daporkchop.webchess.client.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lombok.NonNull;
import net.daporkchop.webchess.common.util.Constants;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

/**
 * @author DaPorkchop_
 */
public interface ClientConstants extends Constants {
    SpriteBatch batch = null;

    AtomicReference<GlyphLayout> glyphLayout_do_not_use = new AtomicReference<>();

    float ASPECT_W = 2;
    float ASPECT_H = 3;

    int TARGET_WIDTH = 8 * 64;
    int TARGET_HEIGHT = 12 * 64;

    CoordinateOffset coordinateOffset = new CoordinateOffset();

    Preferences prefs = new Preferences() {
        @Override
        public Preferences putBoolean(String key, boolean val) {
            return null;
        }

        @Override
        public Preferences putInteger(String key, int val) {
            return null;
        }

        @Override
        public Preferences putLong(String key, long val) {
            return null;
        }

        @Override
        public Preferences putFloat(String key, float val) {
            return null;
        }

        @Override
        public Preferences putString(String key, String val) {
            return null;
        }

        @Override
        public Preferences put(Map<String, ?> vals) {
            return null;
        }

        @Override
        public boolean getBoolean(String key) {
            return false;
        }

        @Override
        public int getInteger(String key) {
            return 0;
        }

        @Override
        public long getLong(String key) {
            return 0;
        }

        @Override
        public float getFloat(String key) {
            return 0;
        }

        @Override
        public String getString(String key) {
            return null;
        }

        @Override
        public boolean getBoolean(String key, boolean defValue) {
            return false;
        }

        @Override
        public int getInteger(String key, int defValue) {
            return 0;
        }

        @Override
        public long getLong(String key, long defValue) {
            return 0;
        }

        @Override
        public float getFloat(String key, float defValue) {
            return 0;
        }

        @Override
        public String getString(String key, String defValue) {
            return null;
        }

        @Override
        public Map<String, ?> get() {
            return null;
        }

        @Override
        public boolean contains(String key) {
            return false;
        }

        @Override
        public void clear() {
        }

        @Override
        public void remove(String key) {
        }

        @Override
        public void flush() {
        }
    };
    String NUMBERS_0_9 = "0123456789";
    String[] LOCALIZATION_KEYS = new String[NUMBERS_0_9.length()];

    default void drawString(@NonNull String text, float x, float y) {
        ChessTex.font.draw(batch, text, x, y);
    }

    default void drawCentered(@NonNull String text, float x, float y) {
        GlyphLayout glyphLayout = glyphLayout_do_not_use.get();
        if (glyphLayout == null) {
            glyphLayout_do_not_use.set(glyphLayout = new GlyphLayout());
        }
        glyphLayout.setText(ChessTex.font, text);
        ChessTex.font.draw(batch, text,
                x - (glyphLayout.width * 0.5f),
                y - (glyphLayout.height * 0.5f)
        );
    }

    default void init() {
        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);

            {
                Field field = ClientConstants.class.getDeclaredField("batch");
                modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
                field.set(null, new SpriteBatch());
            }

            {
                Field field = ClientConstants.class.getDeclaredField("prefs");
                modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
                field.set(null, Gdx.app.getPreferences("WebChess"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Localization.initLocales();
        ChessTex.initTex();

        for (int i = 0; i < LOCALIZATION_KEYS.length; i++) {
            LOCALIZATION_KEYS[i] = Pattern.quote(String.format("${%d}", i));
        }
    }

    default void dispose() {
        ChessTex.disposeTex();

        batch.dispose();
        prefs.flush();
    }

    default String localize(@NonNull String key, Object... args) {
        if (args.length > LOCALIZATION_KEYS.length) {
            throw new IllegalArgumentException(String.format("Too many arguments: %d", args.length));
        }
        String msg = Localization.localize(key);
        for (int i = args.length - 1; i >= 0; i--)  {
            Object o = args[i];
            String s;
            if (o == null)  {
                s = "null";
            } else {
                s = o.toString();
            }
            key = LOCALIZATION_KEYS[i];
            msg = msg.replaceFirst(key, s);
        }
        return msg;
    }
}
