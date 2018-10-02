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

package net.daporkchop.webchess.common.game.impl;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import net.daporkchop.webchess.common.game.AbstractBoard;
import net.daporkchop.webchess.common.game.AbstractFigure;

/**
 * @author DaPorkchop_
 */
@AllArgsConstructor
public class BoardPos<B extends AbstractBoard> {
    @NonNull
    public final B board;

    public final int x;
    public final int y;

    public boolean isOnBoard() {
        return this.x >= 0 && this.y >= 0 && this.x < this.board.getSize() && this.y < this.board.getSize();
    }

    public BoardPos<B> clone() {
        return new BoardPos<>(this.board, this.x, this.y);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BoardPos)) {
            return false;
        }
        BoardPos pos = (BoardPos) obj;
        return pos.x == this.x && pos.y == this.y && pos.board == this.board;
    }

    @SuppressWarnings("unchecked")
    public <F extends AbstractFigure> F getFigure() {
        this.ensureOnBoard();
        return (F) this.board.getFigure(this.x, this.y);
    }

    @SuppressWarnings("unchecked")
    public <F extends AbstractFigure> F removeFigure() {
        this.ensureOnBoard();
        return (F) this.board.setFigure(this.x, this.y, null);
    }

    @SuppressWarnings("unchecked")
    public <F extends AbstractFigure> F setFigure(AbstractFigure<B> figure)    {
        this.ensureOnBoard();
        return (F) this.board.setFigure(this.x, this.y, figure);
    }

    public void ensureOnBoard() {
        if (!this.isOnBoard())  {
            throw new IllegalStateException(String.format("Position (%d,%d) is not on board!", this.x, this.y));
        }
    }
}
