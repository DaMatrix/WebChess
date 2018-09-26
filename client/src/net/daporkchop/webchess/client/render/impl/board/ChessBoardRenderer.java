package net.daporkchop.webchess.client.render.impl.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import net.daporkchop.lib.math.vector.i.Vec2i;
import net.daporkchop.lib.math.vector.i.Vec2iM;
import net.daporkchop.webchess.client.ClientMain;
import net.daporkchop.webchess.client.input.board.BoardInputProcessor;
import net.daporkchop.webchess.common.game.AbstractBoard;
import net.daporkchop.webchess.common.game.impl.BoardPos;
import net.daporkchop.webchess.common.game.impl.chess.ChessBoard;
import net.daporkchop.webchess.common.game.impl.chess.figure.ChessFigure;

import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author DaPorkchop_
 */
public class ChessBoardRenderer extends BoardRenderer<ChessBoard, ChessBoardRenderer> {
    /*
     * From chess.ttf README:
     *
     * DIAGRAM BORDERS:
     *                                 SINGLE        DOUBLE       EXTRA *
     *  Top left corner                  1          !  or 033     a - A
     *  Top border                       2          "     034
     *  Top right corner                 3          #     035     s - S
     *  Left border                      4          $     036
     *  Right border                     5          %     037
     *  Bottom left corner               7          /     047     d - D
     *  Bottom border                    8          (     040
     *  Bottom right corner              9          )     041     f - F
     *
     * BOARD POSITION ASSIGNMENTS:
     *                               WHITE SQUARE         DARK SQUARE
     *  Squares                      [space] or * 042          +  043
     *  White pawn                        p                    P
     *  Black pawn                        o                    O
     *  White knight                      n                    N
     *  Black knight                      m                    M
     *  White bishop                      b                    B
     *  Black bishop                      v                    V
     *  White rook                        r                    R
     *  Black rook                        t                    T
     *  White queen                       q                    Q
     *  Black queen                       w                    W
     *  White king                        k                    K
     *  Black king                        l                    L
     */

    private final Map<Character, Texture> textures = new Hashtable<>(); //TODO: primitive map
    private Collection<BoardPos<ChessBoard>> possibleMoves = Collections.emptyList();
    private ChessFigure dragging;
    private final Vec2iM draggingRelativePos = new Vec2iM(0, 0);

    public ChessBoardRenderer(ChessBoard board, ClientMain client) {
        super(8, board, client);

        this.setInputProcessor(new BoardInputProcessor<ChessBoard, ChessBoardRenderer>(board, this) {
            @Override
            public boolean keyDown(int keycode) {
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (pointer == 0) {
                    if (button == Input.Buttons.LEFT && this.downPos == null) {
                        {
                            Vec2i coords = coordinateOffset.translateDisplayToAbsolute(screenX, screenY);
                            screenX = coords.getX();
                            screenY = coords.getY();
                        }
                        this.downPos = this.getPosFromCoords(screenX, screenY);
                        ChessFigure figure = this.downPos.removeFigure();
                        if (figure != null) {
                            ChessBoardRenderer.this.possibleMoves = figure.getValidMovePositions();
                        }

                        ChessBoardRenderer.this.dragging = figure;
                        ChessBoardRenderer.this.draggingRelativePos.setX(screenX % 64);
                        ChessBoardRenderer.this.draggingRelativePos.setY(screenY % 64); //TODO: make the whole screen thing handle resizing correctly
                    }
                }
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if (pointer == 0) {
                    if (button == Input.Buttons.LEFT && this.downPos != null) {
                        {
                            Vec2i coords = coordinateOffset.translateDisplayToAbsolute(screenX, screenY);
                            screenX = coords.getX();
                            screenY = coords.getY();
                        }
                        BoardPos<ChessBoard> upPos = this.getPosFromCoords(screenX, screenY);
                        ChessFigure figure = ChessBoardRenderer.this.dragging;
                        upPos.setFigure(figure);
                        this.downPos = null;
                        ChessBoardRenderer.this.dragging = null;
                    }
                }
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                if (pointer == 0) {
                    return this.mouseMoved(screenX, screenY);
                }
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            @Override
            public boolean scrolled(int amount) {
                return false;
            }
        });

        for (char c : "ponmbvrtqwkl".toCharArray()) {
            Texture tex = this.textures.computeIfAbsent(c, code -> {
                String s = String.valueOf(code);
                FileHandle handle = Gdx.files.internal(String.format("tex/chess/%s.png", s));
                if (handle == null) {
                    throw new NullPointerException(String.format("Unable to find file %s", String.format("tex/chess/%s.png", s)));
                }
                return new Texture(handle);
            });
            this.textures.put(Character.toUpperCase(c), tex);
        }
    }

    @Override
    public void renderBoard() {
        batch.setColor(1.0f, 1.0f, 0.0f, 1.0f);
        this.possibleMoves.forEach(pos -> batch.draw(this.square, pos.x * 64, pos.y * 64, 64, 64));
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);

        for (int x = this.size - 1; x >= 0; x--) {
            for (int y = this.size - 1; y >= 0; y--) {
                ChessFigure figure = this.board.getFigure(x, y);
                if (figure != null) {
                    Texture texture = this.textures.get(figure.getCode());
                    batch.draw(texture, x * 64, y * 64, 64, 64);
                }
            }
        }

        if (this.dragging != null)  {
            Texture texture = this.textures.get(this.dragging.getCode());
            batch.draw(texture, Gdx.input.getX() - this.draggingRelativePos.getX(), Gdx.graphics.getHeight() - Gdx.input.getY() - this.draggingRelativePos.getY(), 64, 64);
        }
    }

    @Override
    public void create() {
        super.create();
    }

    @Override
    public void dispose() {
        super.dispose();
        this.textures.values().forEach(Texture::dispose);
    }
}
