package net.daporkchop.webchess.common.game;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.daporkchop.webchess.common.game.impl.Game;

import java.util.function.Supplier;

/**
 * @author DaPorkchop_
 */
@RequiredArgsConstructor
public abstract class AbstractGame<B extends AbstractBoard> {
    @Getter
    @NonNull
    protected final Game game;

    @NonNull
    protected final Supplier<B> boardSupplier;

    public B createBoard()  {
        return this.boardSupplier.get();
    }
}
