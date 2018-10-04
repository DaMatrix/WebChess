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
import net.daporkchop.lib.primitive.tuple.ObjectObjectImmutableTuple;
import net.daporkchop.lib.primitive.tuple.ObjectObjectTuple;
import net.daporkchop.webchess.client.ClientMain;
import net.daporkchop.webchess.client.gui.element.GuiButton;
import net.daporkchop.webchess.client.util.ChessTex;
import net.daporkchop.webchess.client.util.Localization;
import net.daporkchop.webchess.common.game.impl.Game;
import net.daporkchop.webchess.common.user.User;
import net.daporkchop.webchess.common.user.UserGameStats;

import java.util.concurrent.atomic.AtomicInteger;

public class GuiUserProfile extends Gui {
    private static final float color = 0.5f;
    private static final float light_color = 0.39f;

    @NonNull
    public final User user;

    public GuiUserProfile(ClientMain client, @NonNull Gui parent, @NonNull User user) {
        super(client, parent);

        this.user = user;

        this.elements.add(new GuiButton(
                this,
                0.0f, 0.0f,
                "menu.back",
                () -> this.client.setGui(parent)
        ));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void render(int tick, float partialTicks) {
        super.render(tick, partialTicks);

        int lineHeight = (int) ChessTex.font.getLineHeight();
        this.drawCentered(this.user.getName(), TARGET_WIDTH >> 1, TARGET_HEIGHT - (0.5f * lineHeight));

        this.drawString(Localization.localize("menu.user.score"), 0.0f, TARGET_HEIGHT - (2.0f * lineHeight), color, color, color);
        AtomicInteger y = new AtomicInteger(TARGET_HEIGHT - (3 * lineHeight));
        for (Game game : Game.values()) {
            this.drawString(Localization.localize(game.localizationKey), 32, y.get(), color, color, color);
            UserGameStats stats = this.user.getStats(game);
            for (ObjectObjectTuple<String, AtomicInteger> tuple : new ObjectObjectTuple[] {
                    new ObjectObjectImmutableTuple<>("", stats.score),
                    new ObjectObjectImmutableTuple<>("menu.user.wins", stats.wins),
                    new ObjectObjectImmutableTuple<>("menu.user.losses", stats.losses)}) {
                if (!tuple.getK().isEmpty())    {
                    this.drawString(Localization.localize(tuple.getK()), 64, y.get(), light_color, light_color, light_color);
                }
                String score = String.valueOf(tuple.getV().get());
                this.drawString(score, TARGET_WIDTH - this.getWidth(score) - 8, y.getAndAdd(-lineHeight), color, color, color);
            }
            /*String score = String.valueOf(stats.score.get());
            this.drawString(score, TARGET_WIDTH - this.getWidth(score) - 8, y.getAndAdd(-lineHeight), color, color, color);
            score = String.valueOf(stats.wins.get());
            this.drawString(score, TARGET_WIDTH - this.getWidth(score) - 8, y.getAndAdd(-lineHeight), color, color, color);*/
        }
    }
}
