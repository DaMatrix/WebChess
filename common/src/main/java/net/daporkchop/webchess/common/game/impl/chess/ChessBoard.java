package net.daporkchop.webchess.common.game.impl.chess;

import net.daporkchop.webchess.common.game.AbstractBoard;
import net.daporkchop.webchess.common.game.impl.Side;
import net.daporkchop.webchess.common.game.impl.chess.figure.Bishop;
import net.daporkchop.webchess.common.game.impl.chess.figure.ChessFigure;
import net.daporkchop.webchess.common.game.impl.chess.figure.King;
import net.daporkchop.webchess.common.game.impl.chess.figure.Knight;
import net.daporkchop.webchess.common.game.impl.chess.figure.Pawn;
import net.daporkchop.webchess.common.game.impl.chess.figure.Queen;
import net.daporkchop.webchess.common.game.impl.chess.figure.Rook;

/**
 * @author DaPorkchop_
 */
public class ChessBoard extends AbstractBoard<ChessPlayer, ChessFigure> {
    public ChessBoard() {
        super(ChessPlayer.class, ChessFigure.class);
    }

    @Override
    protected void initBoard() {
        Side.forEach(side -> {
            for (int i = 7; i >= 0; i--) {
                this.addFigure(new Pawn(this, side, i, 1));
            }
            this.addFigure(new Rook(this, side, 0, 0));
            this.addFigure(new Knight(this, side, 1, 0));
            this.addFigure(new Bishop(this, side, 2, 0));
            this.addFigure(new Queen(this, side, 3, 0));
            this.addFigure(new King(this, side, 4, 0));
            this.addFigure(new Bishop(this, side, 5, 0));
            this.addFigure(new Knight(this, side, 6, 0));
            this.addFigure(new Rook(this, side, 7, 0));
            this.flip();
        });
    }

    @Override
    public int getSize() {
        return 8;
    }
}
