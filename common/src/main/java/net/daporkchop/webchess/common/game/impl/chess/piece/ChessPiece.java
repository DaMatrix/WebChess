package net.daporkchop.webchess.common.game.impl.chess.piece;

import net.daporkchop.webchess.common.game.AbstractPiece;
import net.daporkchop.webchess.common.game.Side;
import net.daporkchop.webchess.common.game.impl.chess.ChessBoard;
import net.daporkchop.webchess.common.util.BoardPos;

/**
 * @author DaPorkchop_
 */
public abstract class ChessPiece extends AbstractPiece<ChessBoard> {
    public ChessPiece(BoardPos<ChessBoard> pos, Side side) {
        super(pos, side);
    }

    public abstract int getValue();
}
