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

import lombok.NonNull;
import net.daporkchop.lib.math.vector.i.Vec2i;
import net.daporkchop.webchess.client.ClientMain;
import net.daporkchop.webchess.client.gui.element.GuiButton;
import net.daporkchop.webchess.client.gui.element.GuiTextFieldCentered;
import net.daporkchop.webchess.client.gui.hud.Hud;
import net.daporkchop.webchess.client.util.ChessTex;
import net.daporkchop.webchess.client.util.ClientConstants;
import net.daporkchop.webchess.common.game.impl.chess.figure.Bishop;
import net.daporkchop.webchess.common.game.impl.chess.figure.Knight;
import net.daporkchop.webchess.common.game.impl.chess.figure.Queen;
import net.daporkchop.webchess.common.game.impl.chess.figure.Rook;
import net.daporkchop.webchess.common.net.packet.PawnThingPacket;

import java.util.concurrent.atomic.AtomicInteger;

public class GuiPawnThing extends Gui {
    public GuiPawnThing(ClientMain client, @NonNull Gui parent, @NonNull Vec2i pos) {
        super(client, parent);

        this.elements.add(new GuiTextFieldCentered(
                this,
                0.0f,
                TARGET_HEIGHT - ChessTex.font.getLineHeight() * 1.5f,
                TARGET_WIDTH,
                "menu.pawnthing.header"
        ));

        AtomicInteger i = new AtomicInteger(0);
        for (Class clazz : new Class[] {
                Knight.class,
                Bishop.class,
                Rook.class,
                Queen.class
        }) {
            this.elements.add(new GuiButton(
                    this,
                    (TARGET_WIDTH >> 1 >> 6) - 1.5f,
                    (TARGET_HEIGHT >> 6) - 2.0f - 1.0f - i.getAndIncrement(),
                    String.format("figure.%s", clazz.getSimpleName().toLowerCase()),
                    () -> {
                        this.client.client.send(new PawnThingPacket(clazz, pos, ((Hud) parent).local.side));
                        this.client.setGui(parent);
                    }
            ));
        }
    }
}
