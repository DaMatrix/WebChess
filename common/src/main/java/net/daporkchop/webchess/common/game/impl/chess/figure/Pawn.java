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

import net.daporkchop.webchess.common.game.impl.BoardPos;
import net.daporkchop.webchess.common.game.impl.Direction;
import net.daporkchop.webchess.common.game.impl.Side;
import net.daporkchop.webchess.common.game.impl.chess.ChessBoard;

import java.util.ArrayDeque;

/**
 * @author DaPorkchop_
 */
public class Pawn extends ChessFigure {
    public Pawn(ChessBoard board, Side side, int x, int y) {
        super(board, side, x, y);
    }

    @Override
    public int getValue() {
        return 1;
    }

    @Override
    public void updateValidMovePositions() {
        this.positions = new ArrayDeque<>();
        Direction dir = this.getMoveDirection();
        BoardPos<ChessBoard> pos = new BoardPos<>(this.board, this.x, this.y);
        for (int i = this.isInStartingPos() ? 2 : 1; i > 0; i--) {
            BoardPos<ChessBoard> pos1 = dir.offset(pos, i);
            if (pos1.isOnBoard()) {
                ChessFigure figure = pos1.getFigure();
                if (figure == null) {
                    this.positions.add(pos1);
                }
            }
        }
        Direction.forEachNeighboringDiagonal(diag -> {
            BoardPos<ChessBoard> pos1 = diag.offset(pos);
            if (pos1.isOnBoard()) {
                ChessFigure figure = pos1.getFigure();
                if ((figure != null) && this.canAttack(figure)) {
                    this.positions.add(pos1);
                }
            }
        }, dir);
    }

    public boolean isInStartingPos() {
        return this.y == ((this.side == Side.WHITE) ? 1 : 6);
    }

    public Direction getMoveDirection() {
        return (this.side == Side.WHITE) ? Direction.UP : Direction.DOWN;
    }

    @Override
    public char getCode() {
        return (this.side == Side.WHITE) ? 'P' : 'O';
    }
}
