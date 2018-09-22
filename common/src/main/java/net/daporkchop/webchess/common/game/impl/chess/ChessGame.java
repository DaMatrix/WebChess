package net.daporkchop.webchess.common.game.impl.chess;

import net.daporkchop.webchess.common.game.AbstractGame;
import net.daporkchop.webchess.common.game.impl.Game;

/**
 * @author DaPorkchop_
 */
public class ChessGame extends AbstractGame<ChessBoard> {
    public ChessGame() {
        super(Game.CHESS, ChessBoard::new);
    }
}
