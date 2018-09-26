package net.daporkchop.webchess.client.input.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.daporkchop.webchess.client.render.impl.board.BoardRenderer;
import net.daporkchop.webchess.common.game.AbstractBoard;
import net.daporkchop.webchess.common.game.impl.BoardPos;

/**
 * @author DaPorkchop_
 */
@RequiredArgsConstructor
@Getter
public abstract class BoardInputProcessor<B extends AbstractBoard, R extends BoardRenderer<B, R>> implements InputProcessor {
    @NonNull
    protected final B board;

    @NonNull
    protected final R renderer;

    protected BoardPos<B> downPos;

    protected BoardPos<B> getPosFromCoords(int x, int y) {
        return new BoardPos<>(this.board, x / 64, y / 64);
    }
}
