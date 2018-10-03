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
import net.daporkchop.webchess.client.gui.element.GuiTextField;
import net.daporkchop.webchess.client.gui.hud.Hud;
import net.daporkchop.webchess.client.util.ClientConstants;

public class GuiGameComplete extends Gui {
    @NonNull
    public final Hud hud;

    public GuiGameComplete(ClientMain client, @NonNull Gui parent, @NonNull Hud hud) {
        this(client, parent, hud, false, false);
    }

    public GuiGameComplete(ClientMain client, @NonNull Gui parent, @NonNull Hud hud, boolean completed, boolean victory) {
        super(client, parent);

        this.hud = hud;

        this.elements.add(new GuiButton(
                this,
                0.0f, 0.0f,
                4.0f, 1.0f,
                "menu.return",
                () -> this.client.setGui(parent)
        ));

        this.elements.add(new GuiTextField(
                this,
                ClientConstants.TARGET_WIDTH >> 1, ClientConstants.TARGET_HEIGHT >> 1,
                completed ? victory ? "menu.victory" : "menu.defeat" : "menu.opponentleft"
        ));

        if (completed)  {
            this.elements.add(new GuiButton(
                    this,
                    0.0f, 1.0f,
                    4.0f, 1.0f,
                    "menu.rematch",
                    () -> {
                        //TODO
                    }
            ));
        }
    }
}
