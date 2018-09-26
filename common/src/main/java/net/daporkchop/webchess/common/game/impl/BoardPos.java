package net.daporkchop.webchess.common.game.impl;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import net.daporkchop.webchess.common.game.AbstractBoard;
import net.daporkchop.webchess.common.game.AbstractFigure;

/**
 * @author DaPorkchop_
 */
@AllArgsConstructor
public class BoardPos<B extends AbstractBoard> {
    @NonNull
    public final B board;

    public final int x;
    public final int y;

    public boolean isOnBoard() {
        return this.x >= 0 && this.y >= 0 && this.x < this.board.getSize() && this.y < this.board.getSize();
    }

    public BoardPos<B> clone() {
        return new BoardPos<>(this.board, this.x, this.y);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BoardPos)) {
            return false;
        }
        BoardPos pos = (BoardPos) obj;
        return pos.x == this.x && pos.y == this.y && pos.board == this.board;
    }

    @SuppressWarnings("unchecked")
    public <F extends AbstractFigure> F getFigure() {
        if (!this.isOnBoard()) {
            throw new IllegalStateException("Position not on board!");
        }
        return (F) this.board.getFigure(this.x, this.y);
    }

    @SuppressWarnings("unchecked")
    public <F extends AbstractFigure> F removeFigure() {
        if (!this.isOnBoard()) {
            throw new IllegalStateException("Position not on board!");
        }
        return (F) this.board.setFigure(this.x, this.y, null);
    }

    @SuppressWarnings("unchecked")
    public <F extends AbstractFigure> F setFigure(AbstractFigure<B> figure)    {
        if (!this.isOnBoard()) {
            throw new IllegalStateException("Position not on board!");
        }
        return (F) this.board.setFigure(this.x, this.y, figure);
    }
}
