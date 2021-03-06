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

package net.daporkchop.webchess.client.gui.hud;

import lombok.NonNull;
import net.daporkchop.webchess.client.ClientMain;
import net.daporkchop.webchess.client.gui.Gui;
import net.daporkchop.webchess.client.render.impl.board.BoardRenderer;
import net.daporkchop.webchess.common.game.AbstractBoard;
import net.daporkchop.webchess.common.game.AbstractPlayer;

public abstract class Hud<B extends AbstractBoard, R extends BoardRenderer<B, R>, P extends AbstractPlayer<B>> extends Gui {
    @NonNull
    public final B board;

    @NonNull
    public final R renderer;

    public P local;
    public P opponent;

    @SuppressWarnings("unchecked")
    public Hud(ClientMain client, Gui parent, @NonNull B board, @NonNull R renderer) {
        super(client, parent);

        this.board = board;
        this.renderer = renderer;

        for (P player : (P[]) board.getPlayers()) {
            if (player.user == client.user) {
                this.local = player;
            } else {
                this.opponent = player;
            }
        }

        if (this.local == null) {
            throw new NullPointerException("local");
        } else if (this.opponent == null) {
            throw new NullPointerException("opponent");
        }
    }

    public Hud(ClientMain client, B board, R renderer) {
        this(client, null, board, renderer);
    }

    @Override
    public void create() {
        super.create();

        this.renderer.create();
    }

    @Override
    public void dispose() {
        super.dispose();

        this.renderer.dispose();
    }
}
