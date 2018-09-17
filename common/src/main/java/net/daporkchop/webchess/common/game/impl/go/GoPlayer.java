package net.daporkchop.webchess.common.game.impl.go;

import net.daporkchop.webchess.common.game.AbstractPlayer;
import net.daporkchop.webchess.common.game.Side;
import net.daporkchop.webchess.common.user.User;

/**
 * @author DaPorkchop_
 */
public class GoPlayer extends AbstractPlayer<GoBoard> {
    public GoPlayer(User user, Side side, GoBoard board) {
        super(user, side, board);
    }
}
