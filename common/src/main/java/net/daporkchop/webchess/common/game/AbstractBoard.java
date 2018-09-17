package net.daporkchop.webchess.common.game;

import net.daporkchop.webchess.common.user.User;

/**
 * @author DaPorkchop_
 */
public abstract class AbstractBoard<P extends AbstractPiece, PL extends AbstractPlayer, B extends AbstractBoard> {
    public final AbstractGame<B, PL> game;
    public final int size;
    public final PL player1;
    public final PL player2;
    protected final Object[] board;

    @SuppressWarnings("unchecked")
    public AbstractBoard(AbstractGame<B, PL> game, int size, User user1, User user2) {
        assert game != null;
        assert size >= 0;
        assert user1 != null;
        assert user2 != null;

        this.game = game;
        this.size = size;
        this.board = new Object[size * size];
        this.player1 = game.newPlayer(user1, Side.BLACK, (B) this);
        this.player2 = game.newPlayer(user2, Side.WHITE, (B) this);
    }

    protected abstract void initBoard();

    @SuppressWarnings("unchecked")
    public P getPiece(int x, int y) {
        assert x >= 0;
        assert y >= 0;
        assert x < this.size;
        assert y < this.size;

        return (P) this.board[x * this.size + y];
    }

    public void setPiece(int x, int y, P piece) {
        assert x >= 0;
        assert y >= 0;
        assert x < this.size;
        assert y < this.size;
        assert piece != null;

        this.board[x * this.size + y] = piece;
    }
}
