package net.daporkchop.webchess.common.game;

import net.daporkchop.webchess.common.GameType;
import net.daporkchop.webchess.common.user.User;

/**
 * @author DaPorkchop_
 */
public abstract class AbstractGame<B extends AbstractBoard, PL extends AbstractPlayer> {
    public abstract GameType getType();

    public abstract PL newPlayer(User user, Side side, B board);
}
