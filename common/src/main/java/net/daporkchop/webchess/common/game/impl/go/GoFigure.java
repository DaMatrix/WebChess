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

package net.daporkchop.webchess.common.game.impl.go;

import lombok.NonNull;
import net.daporkchop.webchess.common.game.AbstractFigure;
import net.daporkchop.webchess.common.game.impl.BoardPos;
import net.daporkchop.webchess.common.game.impl.Direction;
import net.daporkchop.webchess.common.game.impl.Side;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class GoFigure extends AbstractFigure<GoBoard> {
    public final int placedOn;
    @NonNull
    public final GoPlayer player;
    private final AtomicBoolean open = new AtomicBoolean();
    //private final Collection<BoardPos<GoBoard>> occupiedFields = new ArrayDeque<>();
    private final Collection<BoardPos<GoBoard>> cache = new ArrayDeque<>();

    public GoFigure(GoBoard board, Side side, int x, int y) {
        super(board, side, x, y);

        this.placedOn = board.turn;

        GoPlayer player = null;
        for (GoPlayer p : board.getPlayers()) {
            if (p.side == side) {
                player = p;
                break;
            }
        }
        if (player == null) {
            throw new NullPointerException();
        }
        this.player = player;

        this.board.updateValidMoves();
    }

    @Override
    public void updateValidMovePositions() {
        //this.positions.clear();
        BoardPos<GoBoard> pos = new BoardPos<>(this.board, this.x, this.y);
        /*//check if this can be a valid area
        AtomicInteger i = new AtomicInteger(0);
        Direction.forEach(d -> {
            BoardPos<GoBoard> p = d.offset(pos);
            GoFigure figure = p.getFigure();
            if (figure != null && figure.side == this.player.side)  {
                i.incrementAndGet();
            }
        });
        if (i.get() < 2)    {
            return;
        }
        this.board.valid.add(pos);*/

        synchronized (this.player.heldAreas) {
            Direction.forEachAxis(d -> {
                this.cache.clear();
                this.open.set(false);
                BoardPos<GoBoard> pos1 = d.offset(pos);
                for (Collection<BoardPos<GoBoard>> area : this.player.heldAreas) {
                    if (area.contains(pos1)) {
                        return;
                    }
                }
                this.recursiveSearch(pos1);
                if (!this.open.get()) {
                    AtomicReference<Collection<BoardPos<GoBoard>>> existing = new AtomicReference<>(null);
                    this.cache.forEach(p -> {
                        if (existing.get() != null) {
                            return;
                        }
                        for (Collection<BoardPos<GoBoard>> area : this.player.heldAreas) {
                            if (area.contains(p)) {
                                existing.set(area);
                                return;
                            }
                        }
                    });
                    if (existing.get() == null) {
                        //new area
                        Collection<BoardPos<GoBoard>> c = new ArrayDeque<>(this.cache);
                        this.player.heldAreas.add(c);
                        existing.set(c);
                    }
                    Collection<BoardPos<GoBoard>> c = existing.get();
                    this.cache.forEach(p -> {
                        if (!c.contains(p)) {
                            c.add(p);
                        }
                    });
                }
            });
        }
    }

    private void recursiveSearch(@NonNull BoardPos<GoBoard> pos) {
        if (this.open.get()) {
            return;
        }
        if (false && this.cache.size() >= 20) {
            this.open.set(true);
        }
        //BoardPos<GoBoard> pos = d.offset(p);
        if (this.cache.contains(pos)) {
            return;
        }
        if (!pos.isOnBoard()) {
            return;
        }
        GoFigure figure = pos.getFigure();
        if (figure != null && figure.side == this.side) {
            return;
        } else if (figure == null)  {
            this.open.set(true);
        }
        this.cache.add(pos);
        Direction.forEachAxis(d -> this.recursiveSearch(d.offset(pos)));
    }
}
