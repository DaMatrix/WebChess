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
    //public static BitmapFont font;
    private static FreeTypeFontGenerator generator;
    private static final Map<Character, Texture> textureMap = new Hashtable<>();
    private static final int size = 64;

    public static void init() {
        //generator = new FreeTypeFontGenerator(Gdx.files.internal("font/chess.ttf"));
        //FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        //parameter.size = 128;
        //parameter.characters = "BHJKLNOPQRTWbhjklopqrtw€+-/_\\";
        //font = generator.generateFont(parameter);
    }

    public static void dispose() {
        //font.dispose();
        //generator.dispose();
        textureMap.values().forEach(Texture::dispose);
    }

    public static void draw(ChessFigure figure, int x, int y) {
        char code = figure == null ? '+' : figure.getCode();
        if (((x & 1) == 0) ^ ((y & 1) == 0)) {
            code = figure == null ? ' ' : Character.toLowerCase(code);
        }
        Texture texture = textureMap.computeIfAbsent(code, ChessTex::load);
        batch.draw(texture, x * size, y * size, size, size);
        //font.draw(batch, s, x * 16, (y + 1) * font.getLineHeight());
    }

    public static Texture load(char code)   {
        String s = String.valueOf(code);
        if (code == '+')   {
            s = "plus";
        } else if (code == ' ') {
            s = "space";
        }
        s = Character.isUpperCase(code) ? String.format("%c_uppercase", code) : s;
        /*A:
        try {
            File in = new File(String.format("/home/daporkchop/TTF-to-PNG/images/chess_%s_512.png", s.replaceAll("_uppercase", "")));
            File out = new File(String.format("/media/daporkchop/TooMuchStuff/PortableIDE/school/WebChess/android/assets/tex/chess/%s.png", s));
            if (out.exists())   {
                break A;
            }
            DataInput dataIn = new DataInputStream(new FileInputStream(in));
            byte[] b = new byte[(int) in.length()];
            dataIn.readFully(b);
            ((DataInputStream) dataIn).close();
            if (!out.exists())  {
                out.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(out);
            fos.write(b);
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
        Texture texture = new Texture(Gdx.files.internal(String.format("tex/chess/%s.png", s)));
        return texture;
    }
}
