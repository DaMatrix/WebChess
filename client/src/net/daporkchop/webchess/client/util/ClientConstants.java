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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Matrix4;
import lombok.NonNull;
import net.daporkchop.webchess.client.ClientMain;
import net.daporkchop.webchess.common.util.Constants;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author DaPorkchop_
 */
public interface ClientConstants extends Constants {
    Batch batch = new Batch() {
        @Override
        public void begin() {
        }

        @Override
        public void end() {
        }

        @Override
        public void setColor(Color tint) {
        }

        @Override
        public void setColor(float r, float g, float b, float a) {
        }

        @Override
        public Color getColor() {
            return null;
        }

        @Override
        public void setColor(float color) {
        }

        @Override
        public float getPackedColor() {
            return 0;
        }

        @Override
        public void draw(Texture texture, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {
        }

        @Override
        public void draw(Texture texture, float x, float y, float width, float height, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {
        }

        @Override
        public void draw(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight) {
        }

        @Override
        public void draw(Texture texture, float x, float y, float width, float height, float u, float v, float u2, float v2) {
        }

        @Override
        public void draw(Texture texture, float x, float y) {
        }

        @Override
        public void draw(Texture texture, float x, float y, float width, float height) {
        }

        @Override
        public void draw(Texture texture, float[] spriteVertices, int offset, int count) {
        }

        @Override
        public void draw(TextureRegion region, float x, float y) {
        }

        @Override
        public void draw(TextureRegion region, float x, float y, float width, float height) {
        }

        @Override
        public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation) {
        }

        @Override
        public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, boolean clockwise) {
        }

        @Override
        public void draw(TextureRegion region, float width, float height, Affine2 transform) {
        }

        @Override
        public void flush() {
        }

        @Override
        public void disableBlending() {
        }

        @Override
        public void enableBlending() {
        }

        @Override
        public void setBlendFunction(int srcFunc, int dstFunc) {
        }

        @Override
        public void setBlendFunctionSeparate(int srcFuncColor, int dstFuncColor, int srcFuncAlpha, int dstFuncAlpha) {
        }

        @Override
        public int getBlendSrcFunc() {
            return 0;
        }

        @Override
        public int getBlendDstFunc() {
            return 0;
        }

        @Override
        public int getBlendSrcFuncAlpha() {
            return 0;
        }

        @Override
        public int getBlendDstFuncAlpha() {
            return 0;
        }

        @Override
        public Matrix4 getProjectionMatrix() {
            return null;
        }

        @Override
        public void setProjectionMatrix(Matrix4 projection) {
        }

        @Override
        public Matrix4 getTransformMatrix() {
            return null;
        }

        @Override
        public void setTransformMatrix(Matrix4 transform) {
        }

        @Override
        public ShaderProgram getShader() {
            return null;
        }

        @Override
        public void setShader(ShaderProgram shader) {
        }

        @Override
        public boolean isBlendingEnabled() {
            return false;
        }

        @Override
        public boolean isDrawing() {
            return false;
        }

        @Override
        public void dispose() {
        }
    };

    ThreadLocal<GlyphLayout> glyphLayout_do_not_use = ThreadLocal.withInitial(GlyphLayout::new);

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

    Texture whiteSquare = null;

    default void drawCentered(String text, float x, float y, Color color) {
        this.drawCentered(text, x, y, color.r, color.g, color.b, color.a);
    }

    default void drawCentered(String text, float x, float y, float r, float g, float b) {
        this.drawCentered(text, x, y, r, g, b, 1.0f);
    }

    default void drawCentered(String text, float x, float y, float r, float g, float b, float a) {
        ChessTex.font.setColor(r, g, b, a);
        this.drawCentered(text, x, y);
        ChessTex.font.setColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    default void drawCentered(@NonNull String text, float x, float y) {
        GlyphLayout glyphLayout = glyphLayout_do_not_use.get();
        glyphLayout.setText(ChessTex.font, text);
        this.drawString(
                text,
                x - (glyphLayout.width * 0.5f),
                y - (glyphLayout.height * 0.5f)
        );
    }

    default float getWidth(@NonNull String text)     {
        GlyphLayout glyphLayout = glyphLayout_do_not_use.get();
        glyphLayout.setText(ChessTex.font, text);
        return glyphLayout.width;
    }

    default void drawString(@NonNull String text, float x, float y, Color color) {
        this.drawString(text, x, y, color.r, color.g, color.b, color.a);
    }

    default void drawString(String text, float x, float y, float r, float g, float b) {
        this.drawString(text, x, y, r, g, b, 1.0f);
    }

    default void drawString(@NonNull String text, float x, float y, float r, float g, float b, float a) {
        ChessTex.font.setColor(r, g, b, a);
        this.drawString(text, x, y);
        ChessTex.font.setColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    default void drawString(@NonNull String text, float x, float y) {
        ChessTex.font.draw(batch, text, x, y);
    }

    default void init(@NonNull ClientMain client) {
        try {
            Field modifiersField = Field.class.getDeclaredField(client.android ? "accessFlags" : "modifiers");
            modifiersField.setAccessible(true);

            {
                Field field = ClientConstants.class.getDeclaredField("batch");
                modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
                field.set(null, new SpriteBatch());
            }

            {
                Field field = ClientConstants.class.getDeclaredField("prefs");
                modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
                field.set(null, Gdx.app.getPreferences(System.getProperty("config.path", "WebChess")));
            }

            {
                Field field = ClientConstants.class.getDeclaredField("whiteSquare");
                modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
                field.set(null, new Texture("tex/square.png"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Localization.initLocales(client);
        ChessTex.initTex();

        for (int i = 0; i < LOCALIZATION_KEYS.length; i++) {
            LOCALIZATION_KEYS[i] = Pattern.quote(String.format("${%d}", i));
        }
    }

    default void dispose() {
        ChessTex.disposeTex();

        batch.dispose();
        prefs.flush();

        whiteSquare.dispose();
    }

    default String localize(@NonNull String key, Object... args) {
        if (args.length > LOCALIZATION_KEYS.length) {
            throw new IllegalArgumentException(String.format("Too many arguments: %d", args.length));
        }
        String msg = Localization.localize(key);
        for (int i = args.length - 1; i >= 0; i--) {
            Object o = args[i];
            String s;
            s = (o == null) ? "null" : o.toString();
            key = LOCALIZATION_KEYS[i];
            msg = msg.replaceFirst(key, s);
        }
        return msg;
    }
}
