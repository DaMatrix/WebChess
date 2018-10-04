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

package net.daporkchop.webchess.client.input.board;

import com.badlogic.gdx.InputProcessor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.daporkchop.webchess.client.render.impl.board.BoardRenderer;
import net.daporkchop.webchess.client.render.impl.board.GoBoardRenderer;
import net.daporkchop.webchess.common.game.AbstractBoard;
import net.daporkchop.webchess.common.game.impl.BoardPos;

import static java.lang.Math.max;

/**
 * @author DaPorkchop_
 */
@RequiredArgsConstructor
@Getter
public abstract class BoardInputProcessor<B extends AbstractBoard, R extends BoardRenderer<B, R>> implements InputProcessor {
    @NonNull
    protected final B board;

    @NonNull
    protected final R renderer;

    protected BoardPos<B> downPos;

    private int PIXELS_PER_SQUARE = -1;

    public BoardPos<B> getPosFromCoords(int x, int y) {
        return this.getPosFromCoords(x, y, false);
    }

    protected BoardPos<B> getPosFromCoords(int x, int y, boolean flip) {
        if (this.PIXELS_PER_SQUARE == -1)   {
            switch (this.board.game)    {
                case CHESS: {
                    this.PIXELS_PER_SQUARE = 64;
                }
                break;
                case GO: {
                    this.PIXELS_PER_SQUARE = GoBoardRenderer.PIXELS_PER_SQUARE;
                }
            }
        }
        if ((x < 0) || (y < 0) || (max(x, y) >= (this.board.getSize() * this.PIXELS_PER_SQUARE))) {
            return null;
        }
        return new BoardPos<>(this.board, x / this.PIXELS_PER_SQUARE, flip ? this.board.getSize() - 1 - y / this.PIXELS_PER_SQUARE : y / this.PIXELS_PER_SQUARE);
    }
}
