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
import net.daporkchop.lib.binary.stream.DataIn;
import net.daporkchop.lib.binary.stream.DataOut;
import net.daporkchop.lib.network.packet.Codec;
import net.daporkchop.lib.network.packet.Packet;
import net.daporkchop.webchess.common.net.WebChessSession;

import java.io.IOException;

@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestPacket implements Packet {
    public LoginRequestType type;
    public String username;
    public byte[] password;

    @Override
    public void read(DataIn in) throws IOException {
        this.type = LoginRequestType.values()[in.read()];
        this.username = in.readUTF();
        this.password = in.readBytesSimple();
    }

    @Override
    public void write(DataOut out) throws IOException {
        out.write(this.type.ordinal());
        out.writeUTF(this.username);
        out.writeBytesSimple(this.password);
    }

    public enum LoginRequestType {
        LOG_IN,
        REGISTER;
    }

    public static class LoginRequestCodec<S extends WebChessSession> implements Codec<LoginRequestPacket, S> {
        @Override
        public void handle(LoginRequestPacket packet, S session) {
            ((WebChessSession.ServerSession) session).handle(packet);
        }

        @Override
        public LoginRequestPacket newPacket() {
            return new LoginRequestPacket();
        }
    }
}
