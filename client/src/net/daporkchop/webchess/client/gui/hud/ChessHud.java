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
import net.daporkchop.webchess.client.render.impl.board.ChessBoardRenderer;
import net.daporkchop.webchess.client.util.ChessTex;
import net.daporkchop.webchess.common.game.impl.chess.ChessBoard;
import net.daporkchop.webchess.common.game.impl.chess.ChessPlayer;

public class ChessHud extends Hud<ChessBoard, ChessBoardRenderer, ChessPlayer> {
    public ChessHud(ClientMain client, Gui parent, ChessBoard board) {
        super(client, parent, board, new ChessBoardRenderer(board, client));
    }

    @Override
    public void render(int tick, float partialTicks) {
        super.render(tick, partialTicks);

        this.renderer.render(tick, partialTicks);

        this.drawString(this.local.user.getName(), 0.0f, TARGET_HEIGHT - ChessTex.font.getLineHeight(), 0.5f, 1.0f, 0.5f);
        this.drawString(this.opponent.user.getName(), TARGET_WIDTH - this.getWidth(this.opponent.user.getName()) - 8, TARGET_HEIGHT - ChessTex.font.getLineHeight(), 1.0f, 0.5f, 0.5f);
    }
}
