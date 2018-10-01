package net.daporkchop.webchess.client.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import lombok.NonNull;
import net.daporkchop.webchess.common.game.impl.chess.figure.ChessFigure;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author DaPorkchop_
 */
public class ChessTex implements ClientConstants {
    public static BitmapFont font;
    private static FreeTypeFontGenerator generator;

    public static void initTex() {
        generator = new FreeTypeFontGenerator(Gdx.files.internal("font/LiberationSans-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 128;
        font = generator.generateFont(parameter);
    }

    public static void disposeTex() {
        font.dispose();
        generator.dispose();
    }
}
