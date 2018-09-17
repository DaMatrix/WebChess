package net.daporkchop.webchess.common.game.impl.chess.piece;

import net.daporkchop.webchess.common.game.Side;
import net.daporkchop.webchess.common.game.impl.chess.ChessBoard;
import net.daporkchop.webchess.common.util.BoardPos;

/**
 * @author DaPorkchop_
 */
public class Rook extends ChessPiece {
    public Rook(BoardPos<ChessBoard> pos, Side side) {
        super(pos, side);
    }

    @Override
    public int getValue() {
        return 5;
    }
}
