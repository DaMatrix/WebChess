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

package net.daporkchop.webchess.server;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.daporkchop.webchess.common.game.AbstractBoard;
import net.daporkchop.webchess.common.game.impl.Game;
import net.daporkchop.webchess.common.game.impl.Side;
import net.daporkchop.webchess.common.net.packet.BeginGamePacket;
import net.daporkchop.webchess.server.net.WebChessSessionServer;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Math.abs;

@RequiredArgsConstructor
public class Matcher {
    private static final int MAX_SCORE_VARIATION = 200;

    @NonNull
    public final ServerMain server;

    private final Map<Game, Map<WebChessSessionServer, AtomicInteger>> waitingList = new EnumMap<>(Game.class);
    private final AtomicBoolean running = new AtomicBoolean(false);

    {
        for (Game game : Game.values()) {
            this.waitingList.computeIfAbsent(game, g -> new Hashtable<>());
        }
    }

    @SuppressWarnings("unchecked")
    public void start() {
        if (this.running.getAndSet(true)) {
            throw new IllegalStateException("Already started!");
        }

        new Thread(() -> {
            Collection<WebChessSessionServer> alreadyProcessed = new ArrayDeque<>();
            Map<WebChessSessionServer, AtomicInteger> dupeMap = new Hashtable<>();
            while (this.running.get()) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                this.waitingList.forEach((game, map) -> {
                    alreadyProcessed.clear();
                    dupeMap.clear();
                    dupeMap.putAll(this.waitingList.get(game));
                    if (dupeMap.size() <= 1)    {
                        return;
                    }
                    for (Iterator<Map.Entry<WebChessSessionServer, AtomicInteger>> iter = dupeMap.entrySet().iterator(); iter.hasNext(); ) {
                        Map.Entry<WebChessSessionServer, AtomicInteger> entry = iter.next();
                        int score = entry.getValue().get();
                        WebChessSessionServer p1 = entry.getKey();
                        if (p1.isIngame() || alreadyProcessed.contains(p1))  {
                            continue;
                        }
                        AtomicInteger minDiff = new AtomicInteger(Integer.MAX_VALUE);
                        AtomicReference<WebChessSessionServer> bestOpponent = new AtomicReference<>();
                        map.entrySet().stream()
                                .filter(e -> e.getKey() != p1 && !alreadyProcessed.contains(e.getKey()) && !e.getKey().isIngame() && abs(score - e.getValue().get()) <= MAX_SCORE_VARIATION)
                                .forEach(e -> {
                                    if (abs(score - e.getValue().get()) < minDiff.get()) {
                                        minDiff.set(abs(score - e.getValue().get()));
                                        bestOpponent.set(e.getKey());
                                    }
                                });
                        if (bestOpponent != null) {
                            Side[] sides = ThreadLocalRandom.current().nextBoolean() ?
                                    new Side[]{Side.BLACK, Side.WHITE} :
                                    new Side[]{Side.WHITE, Side.BLACK};
                            WebChessSessionServer p2 = bestOpponent.get();

                            BeginGamePacket packet = new BeginGamePacket(
                                    game,
                                    new String[]{
                                            p1.getUser().getName(),
                                            p2.getUser().getName()
                                    },
                                    sides
                            );
                            AbstractBoard board = game.game.createBoard();
                            p1.beginGame(packet, game, board, p2, board.getPlayers()[0] = game.game.createPlayer(board, sides[0], p1.getUser()));
                            p2.beginGame(packet, game, board, p1, board.getPlayers()[1] = game.game.createPlayer(board, sides[1], p2.getUser()));

                            alreadyProcessed.add(p1);
                            alreadyProcessed.add(p2);

                            System.out.printf("Started game: %s (%s) vs. %s (%s)\n", p1.getUser().getName(), sides[0].name(), p2.getUser().getName(), sides[1].name());
                        }
                    }
                    alreadyProcessed.forEach(this.waitingList.get(game)::remove);
                });
            }
        }).start();
    }

    public void stop() {
        if (!this.running.getAndSet(false)) {
            throw new IllegalStateException("Already stopped!");
        }
    }

    public void submit(@NonNull Game game, @NonNull WebChessSessionServer session) {
        Map<WebChessSessionServer, AtomicInteger> map = this.waitingList.get(game);
        if (map.containsKey(session))   {
            throw new IllegalStateException("Already in waiting list!");
        } else {
            map.put(session, session.getUser().getStats(game).score);
        }
    }
}
