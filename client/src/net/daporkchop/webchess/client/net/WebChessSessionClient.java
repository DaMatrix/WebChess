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

package net.daporkchop.webchess.client.net;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.daporkchop.webchess.client.ClientMain;
import net.daporkchop.webchess.client.gui.GuiWaiting;
import net.daporkchop.webchess.client.gui.hud.ChessHud;
import net.daporkchop.webchess.client.render.RenderManager;
import net.daporkchop.webchess.client.render.impl.board.ChessBoardRenderer;
import net.daporkchop.webchess.client.util.ClientConstants;
import net.daporkchop.webchess.client.util.Localization;
import net.daporkchop.webchess.common.game.AbstractBoard;
import net.daporkchop.webchess.common.game.AbstractGame;
import net.daporkchop.webchess.common.game.impl.Side;
import net.daporkchop.webchess.common.game.impl.chess.ChessBoard;
import net.daporkchop.webchess.common.game.impl.chess.ChessGame;
import net.daporkchop.webchess.common.game.impl.chess.ChessPlayer;
import net.daporkchop.webchess.common.net.WebChessSession;
import net.daporkchop.webchess.common.net.packet.BeginGamePacket;
import net.daporkchop.webchess.common.net.packet.LocaleDataPacket;
import net.daporkchop.webchess.common.net.packet.LoginResponsePacket;
import net.daporkchop.webchess.common.net.packet.UserDataPacket;
import net.daporkchop.webchess.common.user.User;

/**
 * @author DaPorkchop_
 */
@RequiredArgsConstructor
public class WebChessSessionClient extends WebChessSession implements WebChessSession.ClientSession, ClientConstants {
    @NonNull
    public final ClientMain client;

    @Override
    public void handle(LoginResponsePacket packet) {
        this.client.loginData.handle(packet);
    }

    @Override
    public void handle(UserDataPacket packet) {
        this.client.cachedUsers.put(packet.user.getName(), packet.user);
        this.client.loginData.handle(packet);
    }

    @Override
    public void handle(LocaleDataPacket packet) {
        Localization.receiveLocales(packet);
    }

    @Override
    public void handle(BeginGamePacket packet) {
        this.client.runOnRenderThread(() -> {
            GuiWaiting gui = this.client.getGui();
            AbstractBoard board = packet.game.game.createBoard();
            switch (packet.game) {
                case CHESS: {
                    ChessPlayer[] players = (ChessPlayer[]) board.getPlayers();
                    for (int i = 1; i >= 0; i--) {
                        String playerName = packet.playerNames[i];
                        Side playerSide = packet.playerSides[i];
                        User user = this.client.cachedUsers.get(playerName);
                        if (user == null) {
                            throw new IllegalStateException(String.format("Unknown user %s!", playerName));
                        }
                        players[i] = new ChessPlayer((ChessBoard) board, playerSide, user);
                    }
                    this.client.setGui(new ChessHud(this.client, gui.parent, (ChessBoard) board));
                    //this.client.getRenderManager().setRenderer(RenderManager.RenderType.BOARD, new ChessBoardRenderer((ChessBoard) board, this.client));
                }
                break;
                case GO: {
                    throw new UnsupportedOperationException();
                }
                //break;
            }
        });
    }
}
