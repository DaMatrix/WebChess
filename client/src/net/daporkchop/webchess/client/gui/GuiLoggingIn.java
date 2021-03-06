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

import com.badlogic.gdx.utils.Align;
import lombok.NonNull;
import net.daporkchop.webchess.client.ClientMain;
import net.daporkchop.webchess.client.util.ChessTex;

public class GuiLoggingIn extends Gui {
    @NonNull
    private String message;

    @NonNull
    private Object[] args;

    public GuiLoggingIn(ClientMain client, String message) {
        super(client);

        this.setMessage(message);
    }

    public void setMessage(String message, Object... args) {
        this.message = message;
        this.args = args;
    }

    @Override
    public void render(int tick, float partialTicks) {
        ChessTex.font.draw(
                batch,
                this.localize(this.message, this.args),
                0.0f,
                TARGET_HEIGHT - ChessTex.font.getLineHeight(),
                TARGET_WIDTH,
                Align.center,
                true
        );
        //this.drawCentered(this.message, TARGET_HEIGHT * 0.5f, TARGET_WIDTH * 0.5f);
    }

    @Override
    public void create() {
    }

    @Override
    public void dispose() {
    }
}
