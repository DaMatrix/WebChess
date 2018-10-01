package net.daporkchop.webchess.client.gui.element;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.daporkchop.webchess.client.gui.Gui;
import net.daporkchop.webchess.client.input.BaseInputProcessor;
import net.daporkchop.webchess.client.render.IRenderer;

@RequiredArgsConstructor
public abstract class GuiElement implements IRenderer, BaseInputProcessor.BaseInputHandler {
    @NonNull
    public Gui gui;

    public final int x;
    public final int y;

    @Override
    public void create() {
        this.gui.client.getInputProcessor().registerProcessor(this);
    }

    @Override
    public void dispose() {
        this.gui.client.getInputProcessor().removeProcessor(this);
    }
}
