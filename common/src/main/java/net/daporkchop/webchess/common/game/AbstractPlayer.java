package net.daporkchop.webchess.common.game;

import net.daporkchop.webchess.common.user.User;

/**
 * @author DaPorkchop_
 */
public abstract class AbstractPlayer<B extends AbstractBoard> {
    public final User user;
    public final Side side;
    public final B board;

    public AbstractPlayer(User user, Side side, B board) {
        assert user != null;
        assert side != null;
        assert board != null;

        this.user = user;
        this.side = side;
        this.board = board;
    }
}
