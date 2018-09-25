package net.daporkchop.webchess.client.render.impl.board;

import com.badlogic.gdx.graphics.Texture;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.daporkchop.webchess.client.render.IRenderer;
import net.daporkchop.webchess.common.game.AbstractBoard;

/**
 * @author DaPorkchop_
 */
@RequiredArgsConstructor
@Getter
public abstract class BoardRenderer<B extends AbstractBoard> implements IRenderer {
    protected final int size;
    @NonNull
    protected final B board;
    protected Texture square;

    @Override
    public void render(int tick, float partialTicks) {
        this.renderCheckerboard();
        this.renderBoard();
    }

    public void renderCheckerboard()    {
        for (int x = this.size - 1; x >= 0; x--)    {
            for (int y = this.size - (((x & 1) == 0) ? 2 : 1); y >= 0; y -= 2)    {
                batch.draw(this.square, x * 64, y * 64, 64, 64);
            }
        }
    }

    public abstract void renderBoard();

    @Override
    public void create() {
        this.square = new Texture("tex/square.png");
    }

    @Override
    public void dispose() {
        this.square.dispose();
    }
}
