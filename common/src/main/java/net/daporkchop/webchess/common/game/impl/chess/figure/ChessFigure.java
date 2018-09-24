package net.daporkchop.webchess.common.game.impl.chess.figure;

import net.daporkchop.webchess.common.game.AbstractFigure;
import net.daporkchop.webchess.common.game.impl.BoardPos;
import net.daporkchop.webchess.common.game.impl.Side;
import net.daporkchop.webchess.common.game.impl.chess.ChessBoard;

import java.util.Collection;

/**
 * @author DaPorkchop_
 */
public abstract class ChessFigure extends AbstractFigure<ChessBoard> {
    public ChessFigure(ChessBoard board, Side side, int x, int y) {
        super(board, side, x, y);
    }

    public abstract int getValue();

    public abstract Collection<BoardPos<ChessBoard>> getValidMovePositions();

    public abstract char getCode();
}
