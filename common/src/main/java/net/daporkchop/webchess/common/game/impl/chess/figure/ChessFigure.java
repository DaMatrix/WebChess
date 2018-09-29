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

    public boolean canAttack(@NonNull ChessFigure other)    {
        return other.board == this.board && other.side != this.side;
    }
}
