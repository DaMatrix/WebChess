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

package net.daporkchop.webchess.client.gui.hud;

import lombok.NonNull;
import net.daporkchop.webchess.client.ClientMain;
import net.daporkchop.webchess.client.gui.Gui;
import net.daporkchop.webchess.common.game.AbstractBoard;
import net.daporkchop.webchess.common.game.impl.Game;

import java.util.EnumMap;
import java.util.Map;

public abstract class Hud<B extends AbstractBoard> extends Gui {
    private static final Map<Game, HudSupplier> suppliers = new EnumMap<>(Game.class);
    @NonNull
    public final B board;

    public Hud(ClientMain client, Gui parent, @NonNull B board) {
        super(client, parent);

        this.board = board;
    }

    public Hud(ClientMain client, B board) {
        this(client, null, board);
    }

    protected static <B extends AbstractBoard, H extends Hud<B>> void registerHud(@NonNull Game game, @NonNull HudSupplier<B, H> supplier) {
        suppliers.put(game, supplier);
    }

    @SuppressWarnings("unchecked")
    public static synchronized <B extends AbstractBoard, H extends Hud<B>> HudSupplier<B, H> getSupplier(@NonNull Game game) {
        if (!suppliers.containsKey(game)) {
            registerHud(Game.CHESS, ChessHud::new);
        }
        return (HudSupplier<B, H>) suppliers.get(game);
    }

    public interface HudSupplier<B extends AbstractBoard, H extends Hud<B>> {
        H get(ClientMain client, Gui parent, B board);
    }
}
