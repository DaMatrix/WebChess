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

    @Getter
    protected boolean flipped = false;

    @Getter
    protected final int size;
    protected final int sizeIntern;

    @SuppressWarnings("unchecked")
    public AbstractBoard(@NonNull Class<P> playerClass, @NonNull Class<F> figureClass, int size) {
        this.size = size;
        this.sizeIntern = size - 1;
        
        this.players = (P[]) Array.newInstance(playerClass, 2);
        this.figures = (F[]) Array.newInstance(figureClass, this.size * this.size);

        this.initBoard();
    }

    protected abstract void initBoard();

    public F getFigure(int x, int y) {
        if (x < 0 || x >= this.size || y < 0 || y > this.size) {
            throw new IllegalArgumentException(String.format("Invalid board position (%d,%d) (board size: %d)", x, y, this.size));
        }

        if (this.flipped)   {
            //x = this.sizeIntern - x;
            y = this.sizeIntern - y;
        }
        return this.figures[x * this.size + y];
    }

    public F setFigure(int x, int y, F f) {
        if (x < 0 || x >= this.size || y < 0 || y > this.size) {
            throw new IllegalArgumentException(String.format("Invalid board position (%d,%d) (board size: %d)", x, y, this.size));
        }

        int i = x * this.size + y;
        F f1 = this.figures[i];
        this.figures[i] = f;
        return f1;
    }

    public F addFigure(@NonNull F figure)    {
        if (this.flipped) {
            //figure.setX(this.sizeIntern - figure.getX());
            figure.setY(this.sizeIntern - figure.getY());
        }
        return this.setFigure(figure.getX(), figure.getY(), figure);
    }

    public boolean flip()   {
        return this.flipped = !this.flipped;
    }
}
