package net.daporkchop.webchess.common.game;

import lombok.Getter;
import lombok.NonNull;

import java.lang.reflect.Array;

/**
 * @author DaPorkchop_
 */
public abstract class AbstractBoard<P extends AbstractPlayer, F extends AbstractFigure> {
    @Getter
    protected final P[] players;

    protected final F[] figures;

    protected boolean flipped = false;

    @SuppressWarnings("unchecked")
    public AbstractBoard(@NonNull Class<P> playerClass, @NonNull Class<F> figureClass) {
        this.players = (P[]) Array.newInstance(playerClass, 2);
        this.figures = (F[]) Array.newInstance(figureClass, this.getSize() * this.getSize());

        this.initBoard();
    }

    protected abstract void initBoard();

    public abstract int getSize();

    public F getFigure(int x, int y) {
        if (x < 0 || x >= this.getSize() || y < 0 || y > this.getSize()) {
            throw new IllegalArgumentException(String.format("Invalid board position (%d,%d) (board size: %d)", x, y, this.getSize()));
        }

        if (this.flipped)   {
            x = this.getSize() - x;
            y = this.getSize() - y;
        }
        return this.figures[x * this.getSize() + y];
    }

    public F setFigure(int x, int y, F f) {
        if (x < 0 || x >= this.getSize() || y < 0 || y > this.getSize()) {
            throw new IllegalArgumentException(String.format("Invalid board position (%d,%d) (board size: %d)", x, y, this.getSize()));
        }

        if (this.flipped)   {
            x = this.getSize() - x;
            y = this.getSize() - y;
        }
        int i = x * this.getSize() + y;
        F f1 = this.figures[i];
        this.figures[i] = f;
        return f1;
    }

    public F addFigure(@NonNull F figure)    {
        F f = this.setFigure(figure.getX(), figure.getY(), figure);
        figure.setX(this.getSize() - figure.getX());
        figure.setY(this.getSize() - figure.getY());
        return f;
    }

    public boolean flip()   {
        return this.flipped = !this.flipped;
    }
}
