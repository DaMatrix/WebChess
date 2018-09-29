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
public class Knight extends ChessFigure {
    public Knight(ChessBoard board, Side side, int x, int y) {
        super(board, side, x, y);
    }

    @Override
    public int getValue() {
        return 3;
    }

    @Override
    public void updateValidMovePositions() {
        this.positions = new ArrayDeque<>();
        BoardPos<ChessBoard> pos = new BoardPos<>(this.board, this.x, this.y);
        Direction.forEachAxis(axis -> {
            BoardPos<ChessBoard> pos1 = axis.offset(pos, 2);
            Direction.forEachPerpendicular(perp -> {
                BoardPos<ChessBoard> pos2 = perp.offset(pos1);
                if (pos2.isOnBoard())   {
                    ChessFigure figure = this.board.getFigure(pos2.x, pos2.y);
                    if (figure == null || this.canAttack(figure))   {
                        this.positions.add(pos2);
                    }
                }
            }, axis);
        });
    }

    @Override
    public char getCode() {
        return this.side == Side.WHITE ? 'N' : 'M';
    }
}
