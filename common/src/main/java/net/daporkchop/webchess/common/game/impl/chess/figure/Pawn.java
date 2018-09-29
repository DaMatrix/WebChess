package net.daporkchop.webchess.common.game.impl.chess.figure;

import net.daporkchop.webchess.common.game.impl.BoardPos;
import net.daporkchop.webchess.common.game.impl.Direction;
import net.daporkchop.webchess.common.game.impl.Side;
import net.daporkchop.webchess.common.game.impl.chess.ChessBoard;

import java.util.ArrayDeque;
import java.util.Collection;

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
        for (int i = this.isInStartingPos() ? 2 : 1; i > 0; i--)    {
            BoardPos<ChessBoard> pos1 = dir.offset(pos, i);
            if (pos1.isOnBoard())   {
                ChessFigure figure = this.board.getFigure(pos1.x, pos1.y);
                if (figure == null || this.canAttack(figure))   {
                    this.positions.add(pos1);
                    if (figure != null) {
                        break;
                    }
                }
            }
        }
        Direction.forEachNeighboringDiagonal(diag -> {
            BoardPos<ChessBoard> pos1 = diag.offset(pos);
            if (pos1.isOnBoard())   {
                ChessFigure figure = this.board.getFigure(pos1.x, pos1.y);
                if (figure != null && this.canAttack(figure))   {
                    this.positions.add(pos1);
                }
            }
        }, dir);
    }

    public boolean isInStartingPos()    {
        return this.y == (this.side == Side.WHITE ? 1 : 6);
    }

    public Direction getMoveDirection() {
        if (this.side == Side.WHITE)    {
            return Direction.UP;
        } else {
            return Direction.DOWN;
        }
    }

    @Override
    public char getCode() {
        return this.side == Side.WHITE ? 'P' : 'O';
    }
}
