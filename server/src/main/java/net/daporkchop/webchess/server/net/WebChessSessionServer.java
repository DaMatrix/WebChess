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

package net.daporkchop.webchess.server.net;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.daporkchop.webchess.common.net.WebChessSession;
import net.daporkchop.webchess.common.net.packet.LoginRequestPacket;
import net.daporkchop.webchess.common.net.packet.LoginResponsePacket;
import net.daporkchop.webchess.common.user.User;
import net.daporkchop.webchess.server.ServerMain;
import net.daporkchop.webchess.server.util.ServerConstants;

import java.util.Arrays;

/**
 * @author DaPorkchop_
 */
@RequiredArgsConstructor
public class WebChessSessionServer extends WebChessSession implements WebChessSession.ServerSession, ServerConstants {
    @NonNull
    public final ServerMain server;

    @Override
    public void handle(LoginRequestPacket packet) {
        LoginResponsePacket response = new LoginResponsePacket();
        switch (packet.type) {
            case LOG_IN: {
                if (this.user != null) {
                    response.type = LoginResponsePacket.LoginResponseType.LOGIN_FAILED_ALREADY_LOGGED_IN;
                } else if (!this.server.db.contains(packet.username)) {
                    response.type = LoginResponsePacket.LoginResponseType.LOGIN_FAILED_ACCOUNT_NOT_EXIST;
                } else if (this.server.db.isLoaded(packet.username)) {
                    response.type = LoginResponsePacket.LoginResponseType.LOGIN_FAILED_ALREADY_LOGGED_IN;
                } else {
                    User user = this.server.db.load(packet.username).getValue();
                    if (user == null || !Arrays.equals(user.getPassword(), packet.password)) {
                        response.type = LoginResponsePacket.LoginResponseType.LOGIN_FAILED_INVALID_CREDENTIALS;
                        this.server.db.unload(packet.username);
                    } else {
                        response.type = LoginResponsePacket.LoginResponseType.LOGIN_SUCCESS;
                        this.user = user;
                    }
                }
            }
            break;
            case REGISTER: {
                if (this.user != null) {
                    response.type = LoginResponsePacket.LoginResponseType.LOGIN_FAILED_ALREADY_LOGGED_IN;
                } else if (!this.ensureUsernameValid(packet.username)) {
                    response.type = LoginResponsePacket.LoginResponseType.REGISTER_FAILED_USERNAME_INVALID;
                } else if (this.server.db.contains(packet.username)) {
                    response.type = LoginResponsePacket.LoginResponseType.REGISTER_FAILED_USERNAME_TAKEN;
                } else {
                    User user = new User(packet.password, packet.username);
                    this.server.db.put(packet.username, user);
                    response.type = LoginResponsePacket.LoginResponseType.REGISTER_SUCCESS;
                }
            }
            break;
        }
        System.out.printf("%s\n", response.type.name());
        this.send(response);
    }
}
