package net.daporkchop.webchess.common.game.impl.chess;

import net.daporkchop.webchess.common.game.AbstractBoard;
import net.daporkchop.webchess.common.game.impl.Side;
import net.daporkchop.webchess.common.game.impl.chess.figure.*;

/**
 * @author DaPorkchop_
 */
public class ChessBoard extends AbstractBoard<ChessPlayer, ChessFigure> {
    public ChessBoard() {
        super(ChessPlayer.class, ChessFigure.class, 8);
    }

    @Override
    protected void initBoard() {
        Side.forEach(side -> {
            for (int i = 7; i >= 0; i--) {
                new Pawn(this, side, i, 1);
            }
            new Rook(this, side, 0, 0);
            new Knight(this, side, 1, 0);
            new Bishop(this, side, 2, 0);
            new Queen(this, side, 3, 0);
            new King(this, side, 4, 0);
            new Bishop(this, side, 5, 0);
            new Knight(this, side, 6, 0);
            new Rook(this, side, 7, 0);
            this.flip();
        });
        this.updateValidMoves();
    }

    public void updateValidMoves() {
        for (int x = this.sizeIntern; x >= 0; x--) {
            for (int y = this.sizeIntern; y >= 0; y--) {
                ChessFigure figure = this.getFigure(x, y);
                if (figure != null) {
                    figure.updateValidMovePositions();
                }
            }
        }
    }
}
