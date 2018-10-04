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
import com.badlogic.gdx.graphics.Texture;
import lombok.NonNull;
import lombok.Setter;
import net.daporkchop.lib.math.vector.i.Vec2i;
import net.daporkchop.webchess.client.ClientMain;
import net.daporkchop.webchess.client.gui.hud.GoHud;
import net.daporkchop.webchess.client.input.board.BoardInputProcessor;
import net.daporkchop.webchess.common.game.impl.BoardPos;
import net.daporkchop.webchess.common.game.impl.Side;
import net.daporkchop.webchess.common.game.impl.go.GoBoard;
import net.daporkchop.webchess.common.game.impl.go.GoFigure;
import net.daporkchop.webchess.common.game.impl.go.GoPlayer;
import net.daporkchop.webchess.common.net.packet.MoveFigurePacket;

import java.util.EnumMap;
import java.util.Map;

public class GoBoardRenderer extends BoardRenderer<GoBoard, GoBoardRenderer> {
    //public static final int PIXELS_PER_SQUARE = (int) ((TARGET_WIDTH - 64.0f) / 9.0f);
    public static final int PIXELS_PER_SQUARE = (int) ((TARGET_WIDTH - 70.0f) / 8.0f);

    @NonNull
    @Setter
    private GoHud hud;

    private final Map<Side, Texture> textures = new EnumMap<>(Side.class);

    public GoBoardRenderer(GoBoard board, ClientMain client) {
        super(13, board, client);

        Side.forEach(side -> this.textures.computeIfAbsent(side, s -> new Texture(String.format("tex/go/%s.png", s.name().toLowerCase()))));

        GoPlayer localPlayer = null;
        for (GoPlayer player : board.getPlayers()) {
            if (player.user == client.user) {
                localPlayer = player;
                break;
            }
        }

        {
            GoPlayer localPlayer_a = localPlayer;
            this.setInputProcessor(new BoardInputProcessor<GoBoard, GoBoardRenderer>(this.board, this) {
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
                    if (this.board.upNow == this.side && pointer == 0 && button == Input.Buttons.LEFT) {
                        {
                            Vec2i coords = coordinateOffset.translateDisplayToAbsolute(screenX, screenY);
                            screenX = coords.getX();
                            screenY = coords.getY();
                        }
                        if ((this.downPos = this.getPosFromCoords(screenX, screenY)) != null && this.board.canPlace(this.downPos)) {
                            this.downPos.setFigure(new GoFigure(this.board, this.side, this.downPos.x, this.downPos.y));
                            GoBoardRenderer.this.client.client.send(new MoveFigurePacket(
                                    new Vec2i(this.side.ordinal(), 0),
                                    new Vec2i(this.downPos.x, this.downPos.y)
                            ));
                            this.board.upNow = this.board.upNow.getOpposite(); //don't use correct method, it'll be called when the server responds
                        }
                    }
                    return false;
                }

                @Override
                public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                    return false;
                }

                @Override
                public boolean touchDragged(int screenX, int screenY, int pointer) {
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
        }
    }

    @Override
    public void renderBoard() {
        {
            batch.setColor(0.0f, 0.0f, 0.0f, 1.0f);
            batch.draw(whiteSquare, 32, 32, TARGET_WIDTH - 64 - 6, TARGET_WIDTH - 64 - 6);
        }
        {
            batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            for (int x = 7; x >= 0; x--) {
                for (int y = 7; y >= 0; y--) {
                    batch.draw(whiteSquare,
                            32 + 3 + x * (int) ((TARGET_WIDTH - 70.0f) / 8.0f),
                            32 + 3 + y * (int) ((TARGET_WIDTH - 70.0f) / 8.0f),
                            (int) ((TARGET_WIDTH - 70.0f) / 8.0f) - 6,
                            (int) ((TARGET_WIDTH - 70.0f) / 8.0f) - 6);
                }
            }
        }
        {
            batch.setColor(1.0f, 1.0f, 0.0f, 0.7f);
            synchronized (this.hud.local.heldAreas) {
                this.hud.local.heldAreas.forEach(area -> area.forEach(p -> {
                    batch.draw(
                            whiteSquare,
                            8 - 2 + p.x * PIXELS_PER_SQUARE,
                            8 - 2 + p.y * PIXELS_PER_SQUARE,
                            PIXELS_PER_SQUARE,
                            PIXELS_PER_SQUARE
                    );
                }));
            }
            batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
        {
            for (int x = 8; x >= 0; x--) {
                for (int y = 8; y >= 0; y--) {
                    GoFigure figure = this.board.getFigure(x, y);
                    if (figure != null) {
                        batch.draw(
                                this.textures.get(figure.getSide()),
                                8 - 2 + x * PIXELS_PER_SQUARE,
                                8 - 2 + y * PIXELS_PER_SQUARE,
                                PIXELS_PER_SQUARE,
                                PIXELS_PER_SQUARE
                        );
                    }
                }
            }
        }
        if (this.hud.local.side == this.board.upNow){
            int screenX = Gdx.input.getX();
            int screenY = Gdx.input.getY();
            {
                Vec2i coords = coordinateOffset.translateDisplayToAbsolute(screenX, screenY);
                screenX = coords.getX();
                screenY = coords.getY();
            }
            BoardPos<GoBoard> hoverPos = this.getInputProcessor().getPosFromCoords(screenX, screenY);
            if (hoverPos != null && this.board.canPlace(hoverPos)) {
                batch.setColor(1.0f, 1.0f, 1.0f, 0.5f);
                batch.draw(
                        this.textures.get(this.hud.local.side),
                        //0*32 + 0*3 + 0*16 + 8 - 2 + hoverPos.x * PIXELS_PER_SQUARE,
                        //0*32 + 0*3 + 0*16 + 8 - 2+ hoverPos.y * PIXELS_PER_SQUARE,
                        //i have literally no idea why this works but i'm not touching it
                        8 - 2 + hoverPos.x * PIXELS_PER_SQUARE,
                        8 - 2 + hoverPos.y * PIXELS_PER_SQUARE,
                        PIXELS_PER_SQUARE,// - 6,
                        PIXELS_PER_SQUARE// - 6
                );
                batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        this.textures.values().forEach(Texture::dispose);
    }
}
