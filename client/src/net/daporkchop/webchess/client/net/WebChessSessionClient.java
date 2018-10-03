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
import net.daporkchop.webchess.client.gui.GuiGameComplete;
import net.daporkchop.webchess.client.gui.GuiWaiting;
import net.daporkchop.webchess.client.gui.hud.ChessHud;
import net.daporkchop.webchess.client.gui.hud.Hud;
import net.daporkchop.webchess.client.util.ClientConstants;
import net.daporkchop.webchess.client.util.Localization;
import net.daporkchop.webchess.common.game.AbstractBoard;
import net.daporkchop.webchess.common.game.AbstractPlayer;
import net.daporkchop.webchess.common.game.impl.Side;
import net.daporkchop.webchess.common.game.impl.chess.ChessBoard;
import net.daporkchop.webchess.common.game.impl.chess.ChessPlayer;
import net.daporkchop.webchess.common.net.WebChessSession;
import net.daporkchop.webchess.common.net.packet.*;
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

    @Override
    public void handle(MoveFigurePacket packet) {
        Hud hud = this.client.getGui();
        if (hud instanceof ChessHud)    {
            ChessBoard board = (ChessBoard) hud.board;
            board.setFigure(packet.dst.getX(), packet.dst.getY(), board.setFigure(packet.src.getX(), packet.src.getY(), null));
            board.updateValidMoves();
        }
    }

    @Override
    public void handle(SetNextTurnPacket packet) {
        Hud hud = this.client.getGui();
        hud.board.changeUp(packet.side);
    }

    @Override
    public void handle(UpdateScorePacket packet) {
        Hud hud = this.client.getGui();
        for (AbstractPlayer player : hud.board.getPlayers()) {
            if (player.user.getName().equals(packet.playerName)) {
                player.points.set(packet.score);
                return;
            }
        }

        throw new IllegalStateException(String.format("Unknown player %s", packet.playerName));
    }

    @Override
    public void handle(OpponentLeftPacket packet) {
        Hud hud = this.client.getGui();
        this.client.setGui(new GuiGameComplete(this.client, hud.parent.parent, hud));
    }

    @Override
    public void handle(EndGamePacket packet) {
        Hud hud = this.client.getGui();
        this.client.setGui(new GuiGameComplete(this.client, hud.parent.parent, hud, true, packet.victor.equals(this.client.user.getName())));
    }
}
