package net.daporkchop.webchess.common.game.impl.chess.figure;

import net.daporkchop.webchess.common.game.impl.BoardPos;
import net.daporkchop.webchess.common.game.impl.Side;
import net.daporkchop.webchess.common.game.impl.chess.ChessBoard;

import java.util.Collection;

/**
 * @author DaPorkchop_
 */
public class Queen extends ChessFigure {
    public Queen(ChessBoard board, Side side, int x, int y) {
        super(board, side, x, y);
    }

    @Override
    public int getValue() {
        return 9;
    }

    @Override
    public Collection<BoardPos<ChessBoard>> getValidMovePositions() {
        return null;
    }
}
