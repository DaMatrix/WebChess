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

import net.daporkchop.lib.math.vector.i.Vec2i;
import net.daporkchop.webchess.client.ClientMain;
import net.daporkchop.webchess.client.gui.Gui;
import net.daporkchop.webchess.client.gui.element.GuiButton;
import net.daporkchop.webchess.client.render.impl.board.ChessBoardRenderer;
import net.daporkchop.webchess.client.render.impl.board.GoBoardRenderer;
import net.daporkchop.webchess.client.util.ChessTex;
import net.daporkchop.webchess.common.game.impl.chess.ChessBoard;
import net.daporkchop.webchess.common.game.impl.chess.ChessPlayer;
import net.daporkchop.webchess.common.game.impl.go.GoBoard;
import net.daporkchop.webchess.common.game.impl.go.GoPlayer;
import net.daporkchop.webchess.common.net.packet.InstantWinPacket;
import net.daporkchop.webchess.common.net.packet.MoveFigurePacket;

public class GoHud extends Hud<GoBoard, GoBoardRenderer, GoPlayer> {
    public GoHud(ClientMain client, Gui parent, GoBoard board) {
        super(client, parent, board, new GoBoardRenderer(board, client));
        this.renderer.setHud(this);

        if (IDE) {
            this.elements.add(new GuiButton(
                    this,
                    (TARGET_WIDTH >> 1 >> 6) - 1.5f,
                    (TARGET_HEIGHT >> 6) - 4.0f,
                    "menu.instantwin",
                    () -> this.client.client.send(new InstantWinPacket())
            ));
        }
        this.elements.add(new GuiButton(
                this,
                0.0f,
                (TARGET_HEIGHT >> 6) - 4.0f,
                "menu.go.skipturn",
                () -> this.client.client.send(new MoveFigurePacket(new Vec2i(-1, 0), new Vec2i(0, 0)))
        ));
    }

    @Override
    public void render(int tick, float partialTicks) {
        super.render(tick, partialTicks);

        this.renderer.render(tick, partialTicks);

        float lineHeight = ChessTex.font.getLineHeight();

        batch.setColor(0.0f, 0.0f, 1.0f, 0.3f);
        if (this.board.upNow == this.local.side) {
            batch.draw(whiteSquare, 0.0f, TARGET_HEIGHT - 2 * lineHeight, TARGET_WIDTH >> 1, 2 * lineHeight);
        } else {
            batch.draw(whiteSquare, TARGET_WIDTH >> 1, TARGET_HEIGHT - 2 * lineHeight, TARGET_WIDTH >> 1, 2 * lineHeight);
        }
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        this.drawString(this.local.user.getName(), 4.0f, TARGET_HEIGHT - lineHeight, 0.5f, 1.0f, 0.5f);
        this.drawString(this.opponent.user.getName(), TARGET_WIDTH - this.getWidth(this.opponent.user.getName()) - 4.0f, TARGET_HEIGHT - lineHeight, 1.0f, 0.5f, 0.5f);
        String text = String.valueOf(this.local.getScore());
        this.drawString(text, 4.0f, TARGET_HEIGHT - 2.0f * lineHeight, 0.5f, 0.5f, 0.5f);
        text = String.valueOf(this.opponent.getScore());
        this.drawString(text, TARGET_WIDTH - this.getWidth(text) - 4.0f, TARGET_HEIGHT - 2.0f * lineHeight, 0.5f, 0.5f, 0.5f);
    }
}
