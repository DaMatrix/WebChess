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
public class LoginResponsePacket implements Packet {
    public LoginResponseType type;

    @Override
    public void read(DataIn in) throws IOException {
        this.type = LoginResponseType.values()[in.read()];
    }

    @Override
    public void write(DataOut out) throws IOException {
        out.write(this.type.ordinal());
    }

    public enum LoginResponseType {
        LOGIN_SUCCESS,
        LOGIN_FAILED_INVALID_CREDENTIALS,
        LOGIN_FAILED_ALREADY_LOGGED_IN,
        LOGIN_FAILED_ACCOUNT_NOT_EXIST,
        REGISTER_SUCCESS,
        REGISTER_FAILED_USERNAME_TAKEN,
        REGISTER_FAILED_USERNAME_INVALID,
        REGISTER_FAILED_PASSWORD_INVALID
        ;
    }

    public static class LoginResponseCodec<S extends WebChessSession> implements Codec<LoginResponsePacket, S> {
        @Override
        public void handle(LoginResponsePacket packet, S session) {
            ((WebChessSession.ClientSession) session).handle(packet);
        }

        @Override
        public LoginResponsePacket newPacket() {
            return new LoginResponsePacket();
        }
    }
}
