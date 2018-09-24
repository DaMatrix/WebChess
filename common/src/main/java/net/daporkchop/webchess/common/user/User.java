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
        this.scores.clear();
        for (int i = in.readInt() - 1; i >= 0; i--) {
            this.scores.put(Game.valueOf(in.readUTF()), new AtomicInteger(in.readInt()));
        }
        this.name = in.readUTF();
    }

    @Override
    public void write(DataOut out) throws IOException {
        for (Map.Entry<Game, AtomicInteger> entry : this.scores.entrySet()) {
            out.writeUTF(entry.getKey().name());
            out.writeInt(entry.getValue().get());
        }
        out.writeUTF(this.name);
    }

    public int getScore(@NonNull Game game) {
        return this.scores.computeIfAbsent(game, g -> new AtomicInteger(0)).get();
    }

    public int addScore(@NonNull Game game, int amount) {
        return this.scores.computeIfAbsent(game, g -> new AtomicInteger(0)).addAndGet(amount);
    }
}
