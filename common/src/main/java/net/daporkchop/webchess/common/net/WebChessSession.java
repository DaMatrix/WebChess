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

package net.daporkchop.webchess.common.net;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import net.daporkchop.lib.network.conn.Session;
import net.daporkchop.webchess.common.net.packet.*;
import net.daporkchop.webchess.common.user.User;
import net.daporkchop.webchess.common.util.Constants;

/**
 * @author DaPorkchop_
 */
@Getter
@Setter
public abstract class WebChessSession extends Session implements Constants {
    @NonNull
    protected User user;

    public boolean isLoggedIn() {
        return this.user != null;
    }

    public abstract void handle(@NonNull MoveFigurePacket packet);

    public abstract void handle(@NonNull PawnThingPacket packet);

    public interface ClientSession {
        void handle(@NonNull LoginResponsePacket packet);

        void handle(@NonNull UserDataPacket packet);

        void handle(@NonNull LocaleDataPacket packet);

        void handle(@NonNull BeginGamePacket packet);

        void handle(@NonNull SetNextTurnPacket packet);

        void handle(@NonNull OpponentLeftPacket packet);

        void handle(@NonNull EndGamePacket packet);

        void handle(@NonNull UpdateScorePacket packet);

        void handle(@NonNull PawnThingRequestPacket packet);
    }

    public interface ServerSession {
        void handle(@NonNull LoginRequestPacket packet);

        void handle(@NonNull StartGameRequestPacket packet);

        void handle(@NonNull UserDataRequestPacket packet);

        void handle(@NonNull InstantWinPacket packet);

        void handle(@NonNull MatePacket packet);
    }
}
