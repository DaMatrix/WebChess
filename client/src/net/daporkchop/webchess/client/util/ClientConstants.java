package net.daporkchop.webchess.client.util;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lombok.NonNull;
import net.daporkchop.webchess.common.util.Constants;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.atomic.AtomicReference;

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

    default void drawString(@NonNull String text, float x, float y) {
        ChessTex.font.draw(batch, text, x, y);
    }

    default void drawCentered(@NonNull String text, float x, float y) {
        GlyphLayout glyphLayout = glyphLayout_do_not_use.get();
        if (glyphLayout == null)   {
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
            Field field = ClientConstants.class.getDeclaredField("batch");

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

            field.set(null, new SpriteBatch());
        } catch (Exception e)   {
            throw new RuntimeException(e);
        }

        Localization.init();
        ChessTex.initTex();
    }

    default void dispose()  {
        ChessTex.disposeTex();

        batch.dispose();
    }}
