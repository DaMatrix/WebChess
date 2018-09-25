package net.daporkchop.webchess.client.render;

import net.daporkchop.webchess.client.util.Disposable;

/**
 * @author DaPorkchop_
 */
public interface IRenderer extends Disposable {
    void render(int tick, float partialTicks);
}
