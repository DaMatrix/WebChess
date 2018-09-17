package net.daporkchop.webchess.common.user;

import net.daporkchop.webchess.common.GameType;

import java.util.EnumMap;

/**
 * @author DaPorkchop_
 */
public class User {
    public final EnumMap<GameType, Integer> scorePerGame = new EnumMap<>(GameType.class);

    public final String name;

    public User(String name) {
        assert name != null;

        this.name = name;
    }
}
