package net.daporkchop.webchess.common.game.impl;

import lombok.NonNull;

import java.util.function.Consumer;

/**
 * @author DaPorkchop_
 */
public enum Side {
    BLACK,
    WHITE
    ;

    public static void forEach(@NonNull Consumer<Side> consumer)    {
        consumer.accept(WHITE);
        consumer.accept(BLACK);
    }
}
