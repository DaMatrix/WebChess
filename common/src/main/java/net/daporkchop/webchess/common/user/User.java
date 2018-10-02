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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.daporkchop.lib.binary.Data;
import net.daporkchop.lib.binary.stream.DataIn;
import net.daporkchop.lib.binary.stream.DataOut;
import net.daporkchop.webchess.common.game.impl.Game;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author DaPorkchop_
 */
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
public class User implements Data {
    private final Map<Game, AtomicInteger> scores = new EnumMap<>(Game.class);
    @NonNull
    private byte[] password;

    @NonNull
    @Getter
    private String name;

    @Override
    public void read(DataIn in) throws IOException {
        this.read(in, true);
    }

    @Override
    public void write(DataOut out) throws IOException {
        this.write(out, true);
    }

    public void read(DataIn in, boolean password) throws IOException {
        this.scores.clear();
        for (int i = in.readInt() - 1; i >= 0; i--) {
            this.scores.put(Game.valueOf(in.readUTF()), new AtomicInteger(in.readInt()));
        }
        this.name = in.readUTF();

        if (password) {
            this.password = in.readBytesSimple();
        }
    }

    public void write(DataOut out, boolean password) throws IOException {
        out.writeInt(this.scores.size());
        for (Map.Entry<Game, AtomicInteger> entry : this.scores.entrySet()) {
            out.writeUTF(entry.getKey().name());
            out.writeInt(entry.getValue().get());
        }
        out.writeUTF(this.name);

        if (password) {
            out.writeBytesSimple(this.password);
        }
    }

    public int getScore(@NonNull Game game) {
        return this.scores.computeIfAbsent(game, g -> new AtomicInteger(0)).get();
    }

    public int addScore(@NonNull Game game, int amount) {
        return this.scores.computeIfAbsent(game, g -> new AtomicInteger(0)).addAndGet(amount);
    }
}
