package net.daporkchop.webchess.common.game;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.daporkchop.webchess.common.game.impl.Side;

/**
 * @author DaPorkchop_
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class AbstractFigure<B extends AbstractBoard> {
    @NonNull
    protected final B board;

    @NonNull
    protected final Side side;

    protected int x;
    protected int y;

    @SuppressWarnings("unchecked")
    public AbstractFigure(B board, Side side, int x, int y)    {
        this(board, side);

        this.setX(x);
        this.setY(y);

        this.board.addFigure(this);
    }

    public void setX(int x) {
        if (x < 0 || x >= this.board.getSize()) {
            throw new IllegalArgumentException(String.format("Invalid X position %d (board size: %d)", x, this.board.getSize()));
        }

        this.x = x;
    }

    public void setY(int y) {
        if (y < 0 || y >= this.board.getSize()) {
            throw new IllegalArgumentException(String.format("Invalid Y position %d (board size: %d)", y, this.board.getSize()));
        }

        this.y = y;
    }
}
