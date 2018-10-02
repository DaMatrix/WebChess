/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2018-2018 DaPorkchop_ and contributors
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it. Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from DaPorkchop_.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original authors of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

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
    protected final int size;
    protected final int sizeIntern;
    @Getter
    protected boolean flipped = false;

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

        if (this.flipped) {
            //x = this.sizeIntern - x;
            y = this.sizeIntern - y;
        }
        return this.figures[x * this.size + y];
    }

    public F setFigure(int x, int y, F f) {
        if (x < 0 || x >= this.size || y < 0 || y > this.size) {
            throw new IllegalArgumentException(String.format("Invalid board position (%d,%d) (board size: %d)", x, y, this.size));
        }

        if (f != null) {
            f.setX(x);
            f.setY(y);
        }

        int i = x * this.size + y;
        F f1 = this.figures[i];
        this.figures[i] = f;
        return f1;
    }

    public F addFigure(@NonNull F figure) {
        if (this.flipped) {
            //figure.setX(this.sizeIntern - figure.getX());
            figure.setY(this.sizeIntern - figure.getY());
        }
        return this.setFigure(figure.getX(), figure.getY(), figure);
    }

    public boolean flip() {
        return this.flipped = !this.flipped;
    }
}
