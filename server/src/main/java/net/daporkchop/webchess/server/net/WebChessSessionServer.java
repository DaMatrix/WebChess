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
import net.daporkchop.lib.binary.UTF8;
import net.daporkchop.lib.hash.helper.sha.Sha512Helper;
import net.daporkchop.webchess.common.game.impl.Side;
import net.daporkchop.webchess.common.net.WebChessSession;
import net.daporkchop.webchess.common.net.packet.*;
import net.daporkchop.webchess.common.user.User;
import net.daporkchop.webchess.server.ServerMain;
import net.daporkchop.webchess.server.util.ServerConstants;

import javax.rmi.CORBA.Util;
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
                    if ((user == null) || !Arrays.equals(user.getPassword(), Sha512Helper.sha512(packet.password, packet.username.getBytes(UTF8.utf8)))) {
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
                    this.user = new User(Sha512Helper.sha512(packet.password, packet.username.getBytes(UTF8.utf8)), packet.username);
                    this.server.db.put(packet.username, this.user);
                    response.type = LoginResponsePacket.LoginResponseType.REGISTER_SUCCESS;
                }
            }
            break;
        }
        System.out.printf("%s\n", response.type.name());
        this.send(response);
        if (this.user != null){
            this.send(new UserDataPacket(this.user.getName(), this.user));
        }
    }

    @Override
    public void handle(StartGameRequestPacket packet) {
        System.out.printf("User %s requesting to start new %s game (user score: %d)\n", this.user.getName(), packet.game, this.user.getScore(packet.game));
        if (true)   {
            //debug: make a game against nobody lol
            User tempUser = new User(Sha512Helper.sha512(new byte[2]), "jeff");
            this.send(new UserDataPacket("jeff", tempUser));
            this.send(new BeginGamePacket(packet.game, new String[]{this.user.getName(), "jeff"}, new Side[]{Side.BLACK, Side.WHITE}));
        }
    }

    @Override
    public void handle(UserDataRequestPacket packet) {
        User user = this.server.db.get(packet.name);
        this.send(new UserDataPacket(packet.name, user));
    }
}
