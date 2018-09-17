package net.daporkchop.webchess.common.game.impl.go;

import net.daporkchop.webchess.common.GameType;
import net.daporkchop.webchess.common.game.AbstractGame;
import net.daporkchop.webchess.common.game.Side;
import net.daporkchop.webchess.common.user.User;

/**
 * @author DaPorkchop_
 */
public class GoGame extends AbstractGame<GoBoard, GoPlayer> {
    public static GoGame INSTANCE = new GoGame();

    private GoGame()    {
    }

    @Override
    public GameType getType() {
        return GameType.GO;
    }

    @Override
    public GoPlayer newPlayer(User user, Side side, GoBoard board) {
        return new GoPlayer(user, side, board);
    }
}
