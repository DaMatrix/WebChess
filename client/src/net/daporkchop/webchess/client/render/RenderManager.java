/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2018-2018 DaPorkchop_ and contributors
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it. Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from DaPorkchop_.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original authors of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package net.daporkchop.webchess.client.render;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.daporkchop.webchess.client.ClientMain;
import net.daporkchop.webchess.client.render.impl.BackgroundRenderer;
import net.daporkchop.webchess.client.render.impl.GuiRenderer;

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
        renderer.create();
        T old = (T) this.renderers[type.ordinal()];
        this.renderers[type.ordinal()] = renderer;
        if (old != null) {
            old.dispose();
        }
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
        //this.setRenderer(RenderType.BOARD, new ChessBoardRenderer(new ChessBoard(), this.client));
        this.setRenderer(RenderType.GUI, new GuiRenderer(this.client));
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
