package net.daporkchop.webchess.common.user;

import net.daporkchop.lib.binary.stream.DataIn;
import net.daporkchop.lib.binary.stream.DataOut;
import net.daporkchop.webchess.common.GameType;
import net.daporkchop.webchess.common.util.Data;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author DaPorkchop_
 */
public class User implements Data {
    public final EnumMap<GameType, Integer> scorePerGame = new EnumMap<>(GameType.class);

    public String name;

    public User(String name) {
        assert name != null;

        this.name = name;
    }

    @Override
    public void read(DataIn in) throws IOException {
        this.scorePerGame.clear();
        for (int i = in.readInt() - 1; i >= 0; i--) {
            this.scorePerGame.put(GameType.valueOf(in.readUTF()), in.readInt());
        }
        this.name = in.readUTF();
    }

    @Override
    public void write(DataOut out) throws IOException {
        out.writeInt(this.scorePerGame.size());
        for (Map.Entry<GameType, Integer> entry : scorePerGame.entrySet())  {
            out.writeUTF(entry.getKey().name());
            out.writeInt(entry.getValue());
        }
        out.writeUTF(this.name);
    }
}
