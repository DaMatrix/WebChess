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

    public Gui parent;

    protected final Collection<GuiElement> elements = new ArrayDeque<>();

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
