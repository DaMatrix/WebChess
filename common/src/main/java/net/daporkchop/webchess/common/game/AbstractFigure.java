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

package net.daporkchop.webchess.common.game;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.daporkchop.webchess.common.game.impl.Side;

/**
 * @author DaPorkchop_
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class AbstractFigure<B extends AbstractBoard> {
    @NonNull
    protected final B board;

    @NonNull
    protected final Side side;

    protected int x;
    protected int y;

    @SuppressWarnings("unchecked")
    public AbstractFigure(B board, Side side, int x, int y) {
        this(board, side);

        this.setX(x);
        this.setY(y);

        this.board.addFigure(this);
    }

    public void setX(int x) {
        if ((x < 0) || (x >= this.board.getSize())) {
            throw new IllegalArgumentException(String.format("Invalid X position %d (board size: %d)", x, this.board.getSize()));
        }

        this.x = x;
    }

    public void setY(int y) {
        if ((y < 0) || (y >= this.board.getSize())) {
            throw new IllegalArgumentException(String.format("Invalid Y position %d (board size: %d)", y, this.board.getSize()));
        }

        this.y = y;
    }
}
