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

package net.daporkchop.webchess.common.user;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.daporkchop.lib.binary.Data;
import net.daporkchop.lib.binary.stream.DataIn;
import net.daporkchop.lib.binary.stream.DataOut;
import net.daporkchop.webchess.common.game.GameOutcome;
import net.daporkchop.webchess.common.game.impl.Game;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class UserGameStats implements Data {
    private static final int OUTCOME_STORAGE_LENGTH = 256;

    @NonNull
    public final Game game;

    public final AtomicInteger wins = new AtomicInteger(0);
    public final AtomicInteger losses = new AtomicInteger(0);
    public final AtomicInteger score = new AtomicInteger(0);

    public final GameOutcome[] lastOutcomes = new GameOutcome[OUTCOME_STORAGE_LENGTH];

    {
        Arrays.fill(this.lastOutcomes, GameOutcome.UNKNOWN);
    }

    @Override
    public void read(DataIn in) throws IOException {
        this.wins.set(in.readInt());
        this.losses.set(in.readInt());
        this.score.set(in.readInt());
        /*for (int i = (OUTCOME_STORAGE_LENGTH >> 5) - 1; i >= 0; i--)  {
            long l = in.readLong();
            for (int j = 31; j >= 0; j--)   {
                GameOutcome o = GameOutcome.values()[(int) ((l >> (j << 1)) & (4L - 1L))];
                this.lastOutcomes[(i << 5) | j] = o;
            }
        }*/
        //screw efficient storage
        for (int i = OUTCOME_STORAGE_LENGTH - 1; i >= 0; i--)   {
            this.lastOutcomes[i] = GameOutcome.values()[in.read()];
        }
    }

    @Override
    public void write(DataOut out) throws IOException {
        out.writeInt(this.wins.get());
        out.writeInt(this.losses.get());
        out.writeInt(this.score.get());
        /*for (int i = (OUTCOME_STORAGE_LENGTH >> 5) - 1; i >= 0; i--)  {
            long l = 0L;
            for (int j = 31; j >= 0; j--)   {
                long o = this.lastOutcomes[(i << 5) | j].ordinal();
                l |= o << (j << 1);
            }
            out.writeLong(l);
        }*/
        for (int i = OUTCOME_STORAGE_LENGTH - 1; i >= 0; i--)    {
            out.write(this.lastOutcomes[i].ordinal());
        }
    }

    public synchronized void update(@NonNull GameOutcome outcome)    {
        switch (outcome)    {
            case VICTORY: {
                this.wins.addAndGet(1);
            }
            break;
            case DEFEAT: {
                this.losses.addAndGet(1);
            }
            break;
            default: {
            }
            break;
        }
        System.arraycopy(this.lastOutcomes, 0, this.lastOutcomes, 1, this.lastOutcomes.length - 1);
        this.lastOutcomes[0] = outcome;
        int score = 0;
        int diff = outcome == GameOutcome.VICTORY ? 10 : -10;
        for (int i = 1; i < OUTCOME_STORAGE_LENGTH; i++)    {
            score += diff;
            GameOutcome next = this.lastOutcomes[i];
            if (next == outcome)    {
                diff += ((outcome == GameOutcome.VICTORY) ? 1 : -1);
            } else {
                diff = ((next == GameOutcome.VICTORY) ? 10 : -10);
            }
            if (next == GameOutcome.UNKNOWN)    {
                break;
            }
            outcome = next;
        }
        this.score.set(score);
        /*this.wins.set(0);
        this.losses.set(0);
        for (int i = 1; i < OUTCOME_STORAGE_LENGTH; i++)    {
            GameOutcome next = this.lastOutcomes[i];
            if (next == GameOutcome.UNKNOWN)    {
                return;
            }
            (next == GameOutcome.VICTORY ? this.wins : this.losses).addAndGet(1);
        }*/
    }
}
