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

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.daporkchop.webchess.client.ClientMain;
import net.daporkchop.webchess.client.gui.element.GuiElement;
import net.daporkchop.webchess.client.render.IRenderer;

import java.util.ArrayDeque;
import java.util.Collection;

@RequiredArgsConstructor
@AllArgsConstructor
public abstract class Gui implements IRenderer {
    @NonNull
    public final ClientMain client;
    protected final Collection<GuiElement> elements = new ArrayDeque<>();
    public Gui parent;

    @Override
    public void render(int tick, float partialTicks) {
        this.elements.forEach(e -> e.render(tick, partialTicks));
    }

    @Override
    public void create() {
        this.elements.forEach(GuiElement::create);
    }

    @Override
    public void dispose() {
        this.elements.forEach(GuiElement::dispose);
    }
}
