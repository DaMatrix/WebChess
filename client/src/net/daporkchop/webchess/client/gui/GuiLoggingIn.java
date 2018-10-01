package net.daporkchop.webchess.client.gui;

import lombok.NonNull;
import net.daporkchop.webchess.client.ClientMain;

import static net.daporkchop.webchess.client.util.Localization.localize;

public class GuiLoggingIn extends Gui {
    @NonNull
    private String message;

    public GuiLoggingIn(ClientMain client, String message) {
        super(client);

        this.setMessage(message);
    }

    public void setMessage(@NonNull String message) {
        this.message = localize(message);
    }

    @Override
    public void render(int tick, float partialTicks) {
        drawCentered(this.message, TARGET_HEIGHT * 0.5f, TARGET_WIDTH * 0.5f);
    }

    @Override
    public void create() {
    }

    @Override
    public void dispose() {
    }
}
