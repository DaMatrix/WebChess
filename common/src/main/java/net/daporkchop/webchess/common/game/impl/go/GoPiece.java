package net.daporkchop.webchess.common.game.impl.go;

import net.daporkchop.webchess.common.game.AbstractPiece;
import net.daporkchop.webchess.common.game.Side;
import net.daporkchop.webchess.common.util.BoardPos;

/**
 * @author DaPorkchop_
 */
public class GoPiece extends AbstractPiece<GoBoard> {
    public GoPiece(BoardPos<GoBoard> pos, Side side) {
        super(pos, side);
    }
}
