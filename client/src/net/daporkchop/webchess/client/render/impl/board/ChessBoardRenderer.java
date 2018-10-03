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

package net.daporkchop.webchess.client.render.impl.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import net.daporkchop.lib.math.vector.i.Vec2i;
import net.daporkchop.lib.math.vector.i.Vec2iM;
import net.daporkchop.webchess.client.ClientMain;
import net.daporkchop.webchess.client.input.board.BoardInputProcessor;
import net.daporkchop.webchess.common.game.impl.BoardPos;
import net.daporkchop.webchess.common.game.impl.Side;
import net.daporkchop.webchess.common.game.impl.chess.ChessBoard;
import net.daporkchop.webchess.common.game.impl.chess.ChessPlayer;
import net.daporkchop.webchess.common.game.impl.chess.figure.ChessFigure;
import net.daporkchop.webchess.common.net.packet.MoveFigurePacket;

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
    private ChessFigure dragging;
    private Collection<BoardPos<ChessBoard>> validMoves = Collections.emptyList();
    private final boolean flip;

    public ChessBoardRenderer(ChessBoard board, ClientMain client) {
        super(8, board, client);

        ChessPlayer localPlayer = null;
        for (ChessPlayer player : board.getPlayers())   {
            if (player.user == client.user) {
                localPlayer = player;
                break;
            }
        }
        this.flip = localPlayer != null && localPlayer.side == Side.BLACK;

        {
            ChessPlayer localPlayer_a = localPlayer;
            this.setInputProcessor(new BoardInputProcessor<ChessBoard, ChessBoardRenderer>(board, this) {
                private final boolean flip = ChessBoardRenderer.this.flip;
                private final Side side = localPlayer_a == null ? null : localPlayer_a.side;

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
                    System.out.printf("Up next: %s, current: %s\n", this.board.upNow.name(), this.side.name());
                    if (this.board.upNow == this.side && (pointer == 0) && (button == Input.Buttons.LEFT) && (this.downPos == null)) {
                        {
                            Vec2i coords = coordinateOffset.translateDisplayToAbsolute(screenX, screenY);
                            screenX = coords.getX();
                            screenY = coords.getY();
                        }
                        if ((this.downPos = this.getPosFromCoords(screenX, screenY, this.flip)) != null) {
                            if ((ChessBoardRenderer.this.dragging = this.downPos.removeFigure()) != null) {
                                ChessBoardRenderer.this.validMoves = ChessBoardRenderer.this.dragging.getValidMovePositions();
                            }
                        }
                    }
                    return false;
                }

                @Override
                public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                    if (this.board.upNow == this.side && (pointer == 0) && (button == Input.Buttons.LEFT) && (this.downPos != null)) {
                        {
                            Vec2i coords = coordinateOffset.translateDisplayToAbsolute(screenX, screenY);
                            screenX = coords.getX();
                            screenY = coords.getY();
                        }
                        BoardPos<ChessBoard> upPos = this.getPosFromCoords(screenX, screenY, this.flip);
                        if (upPos == null) {
                            upPos = this.downPos;
                        }
                        ChessFigure figure = ChessBoardRenderer.this.dragging;
                        if (figure != null) {
                            if (figure.isValidMove(upPos)) {
                                //upPos.setFigure(figure);
                                //this.board.updateValidMoves();
                                this.downPos.setFigure(figure);
                                ChessBoardRenderer.this.client.client.send(new MoveFigurePacket(
                                        new Vec2i(this.downPos.x, this.downPos.y),
                                        new Vec2i(upPos.x, upPos.y)
                                ));
                            } else {
                                this.downPos.setFigure(figure);
                            }
                            ChessBoardRenderer.this.dragging = null;
                        }
                        if ((figure = upPos.getFigure()) != null) {
                            ChessBoardRenderer.this.validMoves = figure.getValidMovePositions();
                        }
                    }
                    this.downPos = null;
                    return false;
                }

                @Override
                public boolean touchDragged(int screenX, int screenY, int pointer) {
                    if (false && (pointer == 0)) {
                        return this.mouseMoved(screenX, screenY);
                    }
                    return false;
                }

                @Override
                public boolean mouseMoved(int screenX, int screenY) {
                    {
                        Vec2i coords = coordinateOffset.translateDisplayToAbsolute(screenX, screenY);
                        screenX = coords.getX();
                        screenY = coords.getY();
                    }
                    BoardPos<ChessBoard> pos = this.getPosFromCoords(screenX, screenY, this.flip);
                    if (pos != null && pos.isOnBoard()) {
                        ChessFigure figure = pos.getFigure();
                        ChessBoardRenderer.this.validMoves = (figure == null || figure.getSide() == this.side) ? Collections.emptyList() : figure.getValidMovePositions();
                    }
                    return false;
                }

                @Override
                public boolean scrolled(int amount) {
                    return false;
                }
            });
        }

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
        if (false && (this.dragging != null)) {
            batch.setColor(1.0f, 1.0f, 0.0f, 1.0f);
            this.dragging.getValidMovePositions().forEach(pos -> batch.draw(whiteSquare, pos.x * 64, pos.y * 64, 64, 64));
        } else if (true)    {
            if (!this.validMoves.isEmpty()) {
                this.validMoves.forEach(pos -> batch.draw(whiteSquare, pos.x * 64, this.flip ? (7 - pos.y) * 64 : pos.y * 64, 64, 64));
            }
        }
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);

        for (int x = this.size - 1; x >= 0; x--) {
            for (int y = this.size - 1; y >= 0; y--) {
                ChessFigure figure = this.board.getFigure(x, y, this.flip);
                if (figure != null) {
                    Texture texture = this.textures.get(figure.getCode());
                    batch.draw(texture, x * 64, y * 64, 64, 64);
                }
            }
        }

        if (this.dragging != null) {
            Texture texture = this.textures.get(this.dragging.getCode());
            batch.draw(texture,
                    coordinateOffset.displayToLocalX(Gdx.input.getX()) - 32,
                    //coordinateOffset.offsetAndFlipY(Gdx.input.getY() - this.draggingRelativePos.getY()),
                    coordinateOffset.displayToLocalY(Gdx.input.getY()) - 32,
                    64, 64);
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
