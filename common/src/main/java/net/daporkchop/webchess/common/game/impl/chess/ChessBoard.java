package net.daporkchop.webchess.common.game.impl.chess;

import net.daporkchop.webchess.common.game.AbstractBoard;
import net.daporkchop.webchess.common.game.Side;
import net.daporkchop.webchess.common.game.impl.chess.piece.*;
import net.daporkchop.webchess.common.user.User;
import net.daporkchop.webchess.common.util.BoardPos;

/**
 * @author DaPorkchop_
 */
public class ChessBoard extends AbstractBoard<ChessPiece, ChessPlayer, ChessBoard> {
    public ChessBoard(User user1, User user2) {
        super(ChessGame.INSTANCE, 8, user1, user2);
    }

    @Override
    protected void initBoard() {
        {
            //white
            for (int x = 7; x >= 0; x--) {
                this.setPiece(x, 1, new Pawn(new BoardPos<>(x, 1, this), Side.WHITE));
            }
            this.setPiece(0, 0, new Rook(new BoardPos<>(0, 0, this), Side.WHITE));
            this.setPiece(1, 0, new Knight(new BoardPos<>(1, 0, this), Side.WHITE));
            this.setPiece(2, 0, new Bishop(new BoardPos<>(2, 0, this), Side.WHITE));
            this.setPiece(3, 0, new Queen(new BoardPos<>(3, 0, this), Side.WHITE));
            this.setPiece(4, 0, new King(new BoardPos<>(4, 0, this), Side.WHITE));
            this.setPiece(5, 0, new Bishop(new BoardPos<>(5, 0, this), Side.WHITE));
            this.setPiece(6, 0, new Knight(new BoardPos<>(6, 0, this), Side.WHITE));
            this.setPiece(7, 0, new Rook(new BoardPos<>(7, 0, this), Side.WHITE));
        }
        {
            //black
            for (int x = 7; x >= 0; x--) {
                this.setPiece(x, 6, new Pawn(new BoardPos<>(x, 6, this), Side.BLACK));
            }
            this.setPiece(0, 7, new Rook(new BoardPos<>(0, 7, this), Side.BLACK));
            this.setPiece(1, 7, new Knight(new BoardPos<>(1, 7, this), Side.BLACK));
            this.setPiece(2, 7, new Bishop(new BoardPos<>(2, 7, this), Side.BLACK));
            this.setPiece(3, 7, new King(new BoardPos<>(3, 7, this), Side.BLACK));
            this.setPiece(4, 7, new Queen(new BoardPos<>(4, 7, this), Side.BLACK));
            this.setPiece(5, 7, new Bishop(new BoardPos<>(5, 7, this), Side.BLACK));
            this.setPiece(6, 7, new Knight(new BoardPos<>(6, 7, this), Side.BLACK));
            this.setPiece(7, 7, new Rook(new BoardPos<>(7, 7, this), Side.BLACK));
        }
    }
}
