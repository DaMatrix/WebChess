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
import net.daporkchop.lib.math.vector.i.Vec2i;
import net.daporkchop.lib.network.packet.Codec;
import net.daporkchop.lib.network.packet.Packet;
import net.daporkchop.webchess.common.game.impl.BoardPos;
import net.daporkchop.webchess.common.game.impl.Side;
import net.daporkchop.webchess.common.game.impl.chess.ChessBoard;
import net.daporkchop.webchess.common.game.impl.chess.figure.Pawn;
import net.daporkchop.webchess.common.net.WebChessSession;

import java.io.IOException;

@NoArgsConstructor
@AllArgsConstructor
public class PawnThingPacket implements Packet {
    @NonNull
    public Class<?> figureClass;

    @NonNull
    public Vec2i pos;

    @NonNull
    public Side side;

    @Override
    public void read(DataIn in) throws IOException {
        try {
            this.figureClass = Class.forName(in.readUTF());
        } catch (ClassNotFoundException e)  {
            throw new RuntimeException(e);
        }

        this.pos = new Vec2i(in.readInt(), in.readInt());
        this.side = Side.valueOf(in.readUTF());
    }

    @Override
    public void write(DataOut out) throws IOException {
        out.writeUTF(this.figureClass.getCanonicalName());
        out.writeInt(this.pos.getX());
        out.writeInt(this.pos.getY());
        out.writeUTF(this.side.name());
    }

    public static class PawnThingCodec<S extends WebChessSession> implements Codec<PawnThingPacket, S>  {
        @Override
        public void handle(PawnThingPacket packet, S session) {
            session.handle(packet);
        }

        @Override
        public PawnThingPacket newPacket() {
            return new PawnThingPacket();
        }
    }
}
