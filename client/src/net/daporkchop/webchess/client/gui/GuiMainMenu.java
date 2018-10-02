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

import net.daporkchop.webchess.client.ClientMain;
import net.daporkchop.webchess.client.gui.element.GuiButton;

public class GuiMainMenu extends Gui {
    public GuiMainMenu(ClientMain client) {
        super(client);

        this.elements.add(new GuiButton(
                this,
                (TARGET_WIDTH >> 1 >> 6) - 2.0f,
                TARGET_HEIGHT >> 1 >> 6,
                4.0f, 1.0f,
                "menu.play",
                () -> this.client.setGui(new GuiPlay(this.client, this))
        ));

        this.elements.add(new GuiButton(
                this,
                0.0f, 0.0f,
                "menu.chooselanguage",
                () -> this.client.setGui(new GuiLanguageSelector(this.client, this))
        ));

        this.elements.add(new GuiButton(
                this,
                0.0f, (TARGET_HEIGHT >> 6) - 1.0f,
                "menu.profile",
                () -> this.client.setGui(new GuiUserProfile(this.client, this, this.client.user))
        ));
    }
}
