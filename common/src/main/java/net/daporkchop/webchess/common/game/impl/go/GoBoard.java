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
import net.daporkchop.webchess.common.game.AbstractBoard;
import net.daporkchop.webchess.common.game.impl.BoardPos;
import net.daporkchop.webchess.common.game.impl.Direction;
import net.daporkchop.webchess.common.game.impl.Game;
import net.daporkchop.webchess.common.game.impl.Side;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Math.max;

public class GoBoard extends AbstractBoard<GoPlayer, GoFigure> {
    public int turn = 0;

    final Map<Side, Collection<Collection<BoardPos<GoBoard>>>> heldAreasMap = new EnumMap<>(Side.class);
    final Collection<BoardPos<GoBoard>> valid = new ArrayDeque<>();

    {
        Side.forEach(side -> this.heldAreasMap.put(side, new ArrayDeque<>()));
    }

    public GoBoard() {
        super(GoPlayer.class, GoFigure.class, 9, Game.GO);
    }

    @Override
    protected void initBoard() {
    }

    @Override
    public void updateValidMoves() {
        if (false)   {
            return;
        }
        for (GoPlayer player : this.players)    {
            synchronized (player.heldAreas) {
                player.heldAreas.clear();
            }
        }
        for (int x = 8; x >= 0; x--)   {
            for (int y = 8; y >= 0; y--)   {
                GoFigure figure = this.getFigure(x, y);
                if (figure != null) {
                    //if (false) {
                        figure.updateValidMovePositions();
                    //}
                }
            }
        }
        AtomicBoolean cont = new AtomicBoolean(true);
        for (Side side : Side.values()) {
            Collection<Collection<BoardPos<GoBoard>>> areas = this.heldAreasMap.get(side);
            areas.forEach(area -> {
                AtomicBoolean b = new AtomicBoolean(false);
                area.forEach(pos -> {
                    if (b.get())    {
                        return;
                    }
                    GoFigure figure = pos.getFigure();
                    if (figure != null && figure.getSide() != side) {
                        b.set(true);
                    }
                });
                if (b.get()) {
                    AtomicInteger iO = new AtomicInteger(Integer.MIN_VALUE);
                    AtomicInteger iI = new AtomicInteger(Integer.MIN_VALUE);
                    area.forEach(pos -> {
                        GoFigure figure = pos.getFigure();
                        if (figure != null && figure.getSide() != side) {
                            iI.updateAndGet(i -> max(i, figure.placedOn));
                        }
                        Direction.forEach(dir -> {
                            BoardPos<GoBoard> pos1 = dir.offset(pos);
                            if (!pos1.isOnBoard())  {
                                return;
                            }
                            GoFigure figure1 = pos1.getFigure();
                            if (figure1 != null && figure1.getSide() == side)   {
                                iO.updateAndGet(i -> max(i, figure1.placedOn));
                            }
                        });
                    });
                    if (iO.get() > iI.get())    {
                        AtomicReference<GoPlayer> reference = new AtomicReference<>(null);
                        for (GoPlayer player : this.players)    {
                            if (player.side == side)     {
                                reference.set(player);
                            }
                        }
                        if (reference.get() == null)    {
                            throw new IllegalStateException();
                        }
                        area.forEach(pos -> {
                            pos.removeFigure();
                            reference.get().score.incrementAndGet();
                        });
                        //this.updateValidMoves();
                        cont.set(false);
                        return;
                    }
                }
            });
            if (!cont.get())    {
                this.updateValidMoves();
                return;
            }
        }
    }

    @Override
    public Side changeUp() {
        this.turn++;
        return super.changeUp();
    }

    @Override
    public Side changeUp(Side upNow) {
        this.turn++;
        return super.changeUp(upNow);
    }

    public boolean canPlace(@NonNull BoardPos<GoBoard> pos)   {
        if (pos.getFigure() != null)    {
            return false;
        }
        if (this.turn == 0) {
            return pos.isOnBoard();
        }
        AtomicBoolean b = new AtomicBoolean(false);
        Direction.forEachAxis(d -> {
            if (b.get())    {
                return;
            }
            BoardPos<GoBoard> p = d.offset(pos);
            if (p.isOnBoard() && p.getFigure() != null)  {
                b.set(true);
            }
        });
        return b.get();
    }
}
