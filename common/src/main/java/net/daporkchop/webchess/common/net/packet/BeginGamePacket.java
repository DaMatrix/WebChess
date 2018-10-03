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

package net.daporkchop.webchess.common.net.packet;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import net.daporkchop.lib.binary.stream.DataIn;
import net.daporkchop.lib.binary.stream.DataOut;
import net.daporkchop.lib.network.packet.Codec;
import net.daporkchop.lib.network.packet.Packet;
import net.daporkchop.webchess.common.game.AbstractPlayer;
import net.daporkchop.webchess.common.game.impl.Game;
import net.daporkchop.webchess.common.game.impl.Side;
import net.daporkchop.webchess.common.net.WebChessSession;

import java.io.IOException;

@NoArgsConstructor
@AllArgsConstructor
public class BeginGamePacket implements Packet {
    @NonNull
    public Game game; //could be done without, but i'm lazy and would rather just send it again than caching on the client

    @NonNull
    public String[] playerNames;

    @NonNull
    public Side[] playerSides;

    @Override
    public void read(DataIn in) throws IOException {
        this.game = Game.valueOf(in.readUTF());
        this.playerNames = new String[2];
        this.playerSides = new Side[2];
        for (int i = 1; i >= 0; i--)    {
            this.playerNames[i] = in.readUTF();
            this.playerSides[i] = Side.valueOf(in.readUTF());
        }
    }

    @Override
    public void write(DataOut out) throws IOException {
        out.writeUTF(this.game.name());
        for (int i = 1; i >= 0; i--)    {
            out.writeUTF(this.playerNames[i]);
            out.writeUTF(this.playerSides[i].name());
        }
    }

    public static class BeginGameCodec<S extends WebChessSession> implements Codec<BeginGamePacket, S>  {
        @Override
        public void handle(BeginGamePacket packet, S session) {
            ((WebChessSession.ClientSession) session).handle(packet);
        }

        @Override
        public BeginGamePacket newPacket() {
            return new BeginGamePacket();
        }
    }
}
