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

public class GoBoard extends AbstractBoard<GoPlayer, GoFigure> {
    public int turn = 0;
    public int lastSkipped = 0;

    final Map<Side, Collection<Collection<BoardPos<GoBoard>>>> heldAreasMap = new EnumMap<>(Side.class);

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
        for (GoPlayer player : this.players)    {
            player.heldAreas.clear();
        }
        for (int x = 8; x >= 0; x--)   {
            for (int y = 8; y >= 0; y--)   {
                GoFigure figure = this.getFigure(x, y);
                if (figure != null) {
                    figure.updateValidMovePositions();
                }
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
