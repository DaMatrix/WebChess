package net.daporkchop.webchess.client.render.impl.board;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.daporkchop.webchess.client.ClientMain;
import net.daporkchop.webchess.client.input.board.BoardInputProcessor;
import net.daporkchop.webchess.client.render.IRenderer;
import net.daporkchop.webchess.common.game.AbstractBoard;

/**
 * @author DaPorkchop_
 */
@RequiredArgsConstructor
@Getter
public abstract class BoardRenderer<B extends AbstractBoard, R extends BoardRenderer<B, R>> implements IRenderer {
    protected final int size;
    @NonNull
    protected final B board;
    protected Texture square;
    @Setter
    protected BoardInputProcessor<B, R> inputProcessor;
    @NonNull
    protected final ClientMain client;

    @Override
    public void render(int tick, float partialTicks) {
        this.renderCheckerboard();
        this.renderBoard();
    }

    public void renderCheckerboard()    {
        batch.setColor(145.0f / 255.0f, 58.0f / 255.0f, 0.0f, 1.0f);
        for (int x = this.size - 1; x >= 0; x--)    {
            for (int y = this.size - (((x & 1) == 0) ? 2 : 1); y >= 0; y -= 2)    {
                batch.draw(this.square, x * 64, y * 64, 64, 64);
            }
        }
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public abstract void renderBoard();

    @Override
    public void create() {
        this.square = new Texture("tex/square.png");
        this.client.getInputProcessor().registerProcessor(this.inputProcessor);
    }

    @Override
    public void dispose() {
        this.square.dispose();
        this.client.getInputProcessor().removeProcessor(this.inputProcessor);
    }
}
