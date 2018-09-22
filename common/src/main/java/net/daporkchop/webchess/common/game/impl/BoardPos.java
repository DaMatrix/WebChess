package net.daporkchop.webchess.common.game.impl;

import lombok.AllArgsConstructor;
import net.daporkchop.webchess.common.game.AbstractBoard;

/**
 * @author DaPorkchop_
 */
@AllArgsConstructor
public class BoardPos<B extends AbstractBoard> {
    public final B board;

    public final int x;
    public final int y;
}
