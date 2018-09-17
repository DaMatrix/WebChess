package net.daporkchop.webchess.common;

import net.daporkchop.webchess.common.game.AbstractGame;
import net.daporkchop.webchess.common.game.impl.chess.ChessGame;
import net.daporkchop.webchess.common.game.impl.go.GoGame;

/**
 * @author DaPorkchop_
 */
public enum GameType {
    CHESS(ChessGame.INSTANCE),
    GO(GoGame.INSTANCE)
    ;

    public final AbstractGame impl;

    GameType(AbstractGame impl) {
        assert impl != null;

        this.impl = impl;
    }
}
