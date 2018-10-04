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
import net.daporkchop.webchess.client.ClientMain;
import net.daporkchop.webchess.client.gui.element.GuiButton;
import net.daporkchop.webchess.client.gui.element.GuiTextFieldCentered;
import net.daporkchop.webchess.common.net.packet.RematchCancelPacket;
import net.daporkchop.webchess.common.net.packet.RematchPacket;

public abstract class GuiRematch extends Gui {
    @NonNull
    public final String opponent;

    public GuiRematch(ClientMain client, @NonNull Gui parent, @NonNull String opponent) {
        super(client, parent);

        this.opponent = opponent;

        this.elements.add(new GuiButton(
                this,
                0.0f, 0.0f,
                "menu.back",
                () -> {
                    this.client.client.send(new RematchCancelPacket());
                    this.client.setGui(parent);
                }
        ));
    }

    public static class GuiRematchWaiting extends GuiRematch {
        public GuiRematchWaiting(ClientMain client, Gui parent, String opponent) {
            super(client, parent, opponent);

            this.elements.add(new GuiTextFieldCentered(
                    this,
                    0.0f,
                    TARGET_HEIGHT >> 1,
                    TARGET_WIDTH,
                    this.localize("menu.rematch.waiting", opponent)
            ));
        }
    }

    public static class GuiRematchPrompt extends GuiRematch {
        public GuiRematchPrompt(ClientMain client, Gui parent, String opponent) {
            super(client, parent, opponent);

            this.elements.add(new GuiTextFieldCentered(
                    this,
                    0.0f,
                    TARGET_HEIGHT >> 1,
                    TARGET_WIDTH,
                    this.localize("menu.rematch.prompt", opponent)
            ));

            this.elements.add(new GuiButton(
                    this,
                    (TARGET_WIDTH >> 1 >> 6) - 1.5f,
                    (TARGET_HEIGHT >> 1 >> 6) - 3.0f,
                    "menu.rematch.accept",
                    () -> this.client.client.send(new RematchPacket(opponent))
            ));
        }
    }
}
