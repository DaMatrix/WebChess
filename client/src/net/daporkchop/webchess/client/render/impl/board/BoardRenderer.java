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

package net.daporkchop.webchess.client.render.impl.board;

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
    @NonNull
    protected final ClientMain client;
    @Setter
    protected BoardInputProcessor<B, R> inputProcessor;

    @Override
    public void render(int tick, float partialTicks) {
        this.renderCheckerboard();
        this.renderBoard();
    }

    public void renderCheckerboard() {
        batch.setColor(145.0f / 255.0f, 58.0f / 255.0f, 0.0f, 1.0f);
        for (int x = this.size - 1; x >= 0; x--) {
            for (int y = this.size - (((x & 1) == 0) ? 2 : 1); y >= 0; y -= 2) {
                batch.draw(whiteSquare, x * 64, y * 64, 64, 64);
            }
        }
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public abstract void renderBoard();

    @Override
    public void create() {
        this.client.getInputProcessor().registerProcessor(this.inputProcessor);
    }

    @Override
    public void dispose() {
        this.client.getInputProcessor().removeProcessor(this.inputProcessor);
    }
}
