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

package net.daporkchop.webchess.common.game.impl;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.daporkchop.webchess.common.game.AbstractGame;
import net.daporkchop.webchess.common.game.impl.chess.ChessGame;
import net.daporkchop.webchess.common.game.impl.go.GoGame;

import java.util.function.Supplier;

import static java.lang.Math.max;

/**
 * @author DaPorkchop_
 */
public enum Game {
    CHESS(ChessGame::new, Side.WHITE),
    GO(GoGame::new, Side.BLACK);

    @Getter
    private static int maxNameLength = 0;

    static {
        for (Game game : values()) {
            maxNameLength = max(maxNameLength, game.name().length());
            game.game = game.gameSupplier.get();
        }
    }

    @NonNull
    public final Side starter;

    @NonNull
    public final String localizationKey;

    @NonNull
    private final Supplier<AbstractGame> gameSupplier;

    public AbstractGame game;

    Game(@NonNull Supplier<AbstractGame> gameSupplier, @NonNull Side starter) {
        this.localizationKey = String.format("game.%s", this.name().toLowerCase());

        this.gameSupplier = gameSupplier;
        this.starter = starter;
    }
}
