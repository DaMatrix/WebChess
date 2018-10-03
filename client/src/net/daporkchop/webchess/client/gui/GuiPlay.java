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

package net.daporkchop.webchess.client.gui;

import net.daporkchop.lib.primitiveutil.VoidFunction;
import net.daporkchop.webchess.client.ClientMain;
import net.daporkchop.webchess.client.gui.element.GuiButton;
import net.daporkchop.webchess.client.gui.hud.ChessHud;
import net.daporkchop.webchess.client.render.RenderManager;
import net.daporkchop.webchess.client.render.impl.board.ChessBoardRenderer;
import net.daporkchop.webchess.client.util.Localization;
import net.daporkchop.webchess.common.game.impl.Game;
import net.daporkchop.webchess.common.game.impl.chess.ChessBoard;
import net.daporkchop.webchess.common.net.packet.StartGameRequestPacket;
import net.daporkchop.webchess.common.util.locale.Locale;

public class GuiPlay extends Gui {
    public GuiPlay(ClientMain client, Gui parent) {
        super(client, parent);

        int gameCount = Locale.values().length;
        float y = (TARGET_HEIGHT >> 1 >> 6) - (gameCount * 0.75f);
        for (int i = 0; i < gameCount; i++) {
            Game game = Game.values()[i];
            this.elements.add(new GuiButton(
                    this,
                    (TARGET_WIDTH >> 1 >> 6) - 3.0f,
                    y,
                    6.0f,
                    1.48f,
                    game.localizationKey,
                    () -> {
                        this.client.setGui(new GuiWaiting(this.client, this, "menu.waiting.match"));
                        this.client.client.send(new StartGameRequestPacket(game));
                    }
            ));
            y += 1.5f;
        }

        this.elements.add(new GuiButton(
                this,
                0.0f, 0.0f,
                "menu.back",
                () -> this.client.setGui(parent)
        ));
    }
}
