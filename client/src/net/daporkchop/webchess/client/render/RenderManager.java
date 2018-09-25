package net.daporkchop.webchess.client.render;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.daporkchop.webchess.client.ClientMain;
import net.daporkchop.webchess.client.render.impl.board.ChessBoardRenderer;
import net.daporkchop.webchess.client.render.impl.BackgroundRenderer;
import net.daporkchop.webchess.common.game.impl.chess.ChessBoard;

/**
 * @author DaPorkchop_
 */
@RequiredArgsConstructor
public class RenderManager implements IRenderer {
    private final IRenderer[] renderers = new IRenderer[RenderType.values().length];

    @NonNull
    @Getter
    private final ClientMain client;

    @SuppressWarnings("unchecked")
    public <T extends IRenderer> T setRenderer(@NonNull RenderType type, @NonNull IRenderer renderer) {
        T old = (T) this.renderers[type.ordinal()];
        this.renderers[type.ordinal()] = renderer;
        return old;
    }

    @SuppressWarnings("unchecked")
    public <T extends IRenderer> T getRenderer(@NonNull RenderType type) {
        return (T) this.renderers[type.ordinal()];
    }

    @Override
    public void render(int tick, float partialTicks) {
        for (IRenderer renderer : this.renderers) {
            if (renderer == null) {
                continue;
            }
            renderer.render(tick, partialTicks);
        }
    }

    @Override
    public void create() {
        this.setRenderer(RenderType.BACKGROUND, new BackgroundRenderer());
        this.setRenderer(RenderType.BOARD, new ChessBoardRenderer(new ChessBoard()));

        for (IRenderer renderer : this.renderers) {
            if (renderer == null) {
                continue;
            }
            renderer.create();
        }
    }

    @Override
    public void dispose() {
        for (IRenderer renderer : this.renderers) {
            if (renderer == null) {
                continue;
            }
            renderer.dispose();
        }
    }

    public enum RenderType {
        BACKGROUND,
        BOARD,
        GUI;
    }
}
