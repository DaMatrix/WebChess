package net.daporkchop.webchess.common.game;

import net.daporkchop.webchess.common.util.BoardPos;

/**
 * @author DaPorkchop_
 */
public abstract class AbstractPiece<B extends AbstractBoard> {
    public final BoardPos<B> pos;
    public final Side side;

    public AbstractPiece(BoardPos<B> pos, Side side) {
        assert pos != null;
        assert side != null;

        this.pos = pos;
        this.side = side;
    }
}
