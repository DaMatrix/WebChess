package net.daporkchop.webchess.common.game.impl.chess;

import net.daporkchop.webchess.common.GameType;
import net.daporkchop.webchess.common.game.AbstractGame;
import net.daporkchop.webchess.common.game.Side;
import net.daporkchop.webchess.common.user.User;

/**
 * @author DaPorkchop_
 */
public class ChessGame extends AbstractGame<ChessBoard, ChessPlayer> {
    public static final ChessGame INSTANCE = new ChessGame();

    private ChessGame() {
    }

    @Override
    public GameType getType() {
        return GameType.CHESS;
    }

    @Override
    public ChessPlayer newPlayer(User user, Side side, ChessBoard board) {
        return new ChessPlayer(user, side, board);
    }
}
