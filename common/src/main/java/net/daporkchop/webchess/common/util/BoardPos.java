package net.daporkchop.webchess.common.util;

import net.daporkchop.webchess.common.game.AbstractBoard;

/**
 * @author DaPorkchop_
 */
public class BoardPos<B extends AbstractBoard> {
    public int x;
    public int y;
    public B board;

    public BoardPos(int x, int y, B board) {
        assert board != null;
        assert x >= 0;
        assert y >= 0;
        assert x < board.size;
        assert y < board.size;

        this.x = x;
        this.y = y;
        this.board = board;
    }
}
