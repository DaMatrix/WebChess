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
public class King extends ChessFigure {
    public King(ChessBoard board, Side side, int x, int y) {
        super(board, side, x, y);
    }

    @Override
    public int getValue() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Collection<BoardPos<ChessBoard>> getValidMovePositions() {
        Collection<BoardPos<ChessBoard>> positions = new ArrayDeque<>();
        BoardPos<ChessBoard> pos = new BoardPos<>(this.board, this.x, this.y);
        Direction.forEach(dir -> {
            BoardPos<ChessBoard> pos1 = dir.offset(pos);
            if (pos1.isOnBoard()) {
                ChessFigure figure = this.board.getFigure(pos.x, pos.y);
                if (figure == null || this.canAttack(figure)) {
                    //TODO: check if the king'd be in check
                    positions.add(pos1);
                }
            }
        });
        return positions;
    }

    @Override
    public char getCode() {
        return 'K';
    }
}
