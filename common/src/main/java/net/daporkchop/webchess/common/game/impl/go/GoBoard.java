package net.daporkchop.webchess.common.game.impl.go;

import net.daporkchop.webchess.common.game.AbstractBoard;
import net.daporkchop.webchess.common.game.AbstractGame;
import net.daporkchop.webchess.common.user.User;

/**
 * @author DaPorkchop_
 */
public class GoBoard extends AbstractBoard<GoPiece, GoPlayer, GoBoard> {
    public GoBoard(User user1, User user2) {
        super(GoGame.INSTANCE, 0, user1, user2); //TODO: size
    }

    @Override
    protected void initBoard() {
        //TODO: init
    }
}
