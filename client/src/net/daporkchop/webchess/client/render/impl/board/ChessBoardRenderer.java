package net.daporkchop.webchess.client.render.impl.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import net.daporkchop.webchess.common.game.impl.chess.ChessBoard;
import net.daporkchop.webchess.common.game.impl.chess.figure.ChessFigure;

import java.util.Hashtable;
import java.util.Map;

/**
 * @author DaPorkchop_
 */
public class ChessBoardRenderer extends BoardRenderer<ChessBoard> {
    private static final Map<Character, Texture> textures = new Hashtable<>(); //TODO: do this better

    static {
        for (char c : "+pPoOnNmMbBvVrRtTqQwWkKlL".toCharArray()) {
            textures.computeIfAbsent(c, code -> {
                String s = String.valueOf(code);
                if (code == '+')   {
                    s = "plus";
                } else if (code == ' ') {
                    s = "space";
                }
                s = Character.isUpperCase(code) ? String.format("%c_uppercase", code) : s;
                FileHandle handle = Gdx.files.internal(String.format("tex/chess/%s.png", s));
                if (handle == null) {
                    throw new NullPointerException(String.format("Unable to find file %s", String.format("tex/chess/%s.png", s)));
                }
                return new Texture(handle);
            });
        }
    }

    public ChessBoardRenderer(ChessBoard board) {
        super(8, board);
    }

    @Override
    public void renderBoard() {
        //TODO
        for (int x = this.size - 1; x >= 0; x--)    {
            for (int y = this.size - 1; y >= 0; y--)    {
                ChessFigure figure = this.board.getFigure(x, y);
                if (figure != null) {
                    char code = figure.getCode();
                    if (((x & 1) == 0) ^ ((y & 1) == 0)) {
                        code = Character.toLowerCase(code);
                    }
                    Texture texture = textures.get(code);
                    batch.draw(texture, x * 64, y * 64, 64, 64);
                }
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        textures.values().forEach(Texture::dispose);
    }
}
