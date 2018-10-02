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

package net.daporkchop.webchess.client.gui.element;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Align;
import lombok.NonNull;
import net.daporkchop.lib.math.vector.i.Vec2i;
import net.daporkchop.lib.primitiveutil.VoidFunction;
import net.daporkchop.webchess.client.gui.Gui;
import net.daporkchop.webchess.client.util.ChessTex;
import net.daporkchop.webchess.client.util.Localization;

public class GuiButton extends GuiElement {
    @NonNull
    public final String text;

    @NonNull
    public final VoidFunction clickHandler;

    public final float w;
    public final float h;

    public final float textScale;

    public volatile boolean selected;
    public volatile boolean clickDown;

    public GuiButton(Gui gui, float x, float y, @NonNull String text, @NonNull VoidFunction clickHandler) {
        this(gui, x, y, 3.0f, 1.0f, text, clickHandler);
    }

    public GuiButton(Gui gui, float x, float y, float w, float h, @NonNull String text, @NonNull VoidFunction clickHandler) {
        this(gui, x, y, w, h, text, clickHandler, 0.25f);
    }

    public GuiButton(Gui gui, float x, float y, float w, float h, @NonNull String text, @NonNull VoidFunction clickHandler, float textScale) {
        super(x * 64.0f, y * 64.0f, gui);

        this.text = text;
        this.clickHandler = clickHandler;

        this.w = w * 64.0f;
        this.h = h * 64.0f;

        this.textScale = textScale;
    }

    @Override
    public void render(int tick, float partialTicks) {
        if (this.selected) {
            batch.setColor(1.0f, 0.0f, 0.0f, 1.0f);
            batch.draw(whiteSquare, this.x, this.y, this.w, this.h);
            batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        } else {
            batch.draw(whiteSquare, this.x, this.y, this.w, this.h);
        }
        ChessTex.font.draw(batch, Localization.localize(this.text), this.x, this.y + (0.5f * (this.h + ChessTex.font.getLineHeight())), this.w, Align.center, false);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Vec2i vec = coordinateOffset.translateDisplayToAbsolute(screenX, screenY);
        this.selected = (vec.getX() >= this.x)
                && (vec.getX() <= (this.x + this.w))
                && (vec.getY() >= this.y)
                && (vec.getY() <= (this.y + this.h));
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (pointer == 0)   {
            this.mouseMoved(screenX, screenY);
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if ((pointer == 0) && (button == Input.Buttons.LEFT)) {
            this.mouseMoved(screenX, screenY);
            if (this.selected) {
                this.clickDown = true;
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if ((pointer == 0) && (button == Input.Buttons.LEFT) && this.clickDown) {
            this.mouseMoved(screenX, screenY);
            if (this.selected) {
                this.clickHandler.run();
            }
        }
        this.clickDown = false;
        return false;
    }
}
