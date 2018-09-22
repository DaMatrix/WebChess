package net.daporkchop.webchess.common.game;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author DaPorkchop_
 */
@Getter
@RequiredArgsConstructor
public abstract class AbstractPlayer<B extends AbstractBoard> {
    @NonNull
    protected final B board;
}
