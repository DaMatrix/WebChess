package net.daporkchop.webchess.common.game.impl.chess;

import net.daporkchop.webchess.common.game.AbstractPlayer;
import net.daporkchop.webchess.common.game.Side;
import net.daporkchop.webchess.common.user.User;

/**
 * @author DaPorkchop_
 */
public class ChessPlayer extends AbstractPlayer<ChessBoard> {
    public ChessPlayer(User user, Side side, ChessBoard board) {
        super(user, side, board);
    }
}
