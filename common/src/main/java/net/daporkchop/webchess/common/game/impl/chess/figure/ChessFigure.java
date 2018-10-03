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

package net.daporkchop.webchess.common.game.impl.chess.figure;

import lombok.NonNull;
import net.daporkchop.webchess.common.game.AbstractFigure;
import net.daporkchop.webchess.common.game.impl.BoardPos;
import net.daporkchop.webchess.common.game.impl.Side;
import net.daporkchop.webchess.common.game.impl.chess.ChessBoard;

import java.util.Collection;

/**
 * @author DaPorkchop_
 */
public abstract class ChessFigure extends AbstractFigure<ChessBoard> {
    protected Collection<BoardPos<ChessBoard>> positions;

    public ChessFigure(ChessBoard board, Side side, int x, int y) {
        super(board, side, x, y);
    }

    public abstract int getValue();

    public abstract void updateValidMovePositions();

    public Collection<BoardPos<ChessBoard>> getValidMovePositions() {
        return this.positions;
    }

    public abstract char getCode();

    public boolean canAttack(@NonNull ChessFigure other) {
        return (other.board == this.board) && (other.side != this.side);
    }

    public boolean isValidMove(@NonNull BoardPos<ChessBoard> pos)  {
        if (pos.board != this.board)    {
            return false;
        }

        for (BoardPos<ChessBoard> p : this.positions)    {
            if (p.x == pos.x && p.y == pos.y)   {
                return true;
            }
        }

        return false;
    }
}
