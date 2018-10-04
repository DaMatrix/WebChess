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
import net.daporkchop.lib.math.vector.i.Vec2i;
import net.daporkchop.webchess.common.game.AbstractBoard;
import net.daporkchop.webchess.common.game.AbstractPlayer;
import net.daporkchop.webchess.common.game.GameOutcome;
import net.daporkchop.webchess.common.game.impl.BoardPos;
import net.daporkchop.webchess.common.game.impl.Game;
import net.daporkchop.webchess.common.game.impl.Side;
import net.daporkchop.webchess.common.game.impl.chess.ChessBoard;
import net.daporkchop.webchess.common.game.impl.chess.figure.ChessFigure;
import net.daporkchop.webchess.common.game.impl.chess.figure.King;
import net.daporkchop.webchess.common.game.impl.chess.figure.Rook;
import net.daporkchop.webchess.common.game.impl.go.GoBoard;
import net.daporkchop.webchess.common.game.impl.go.GoFigure;
import net.daporkchop.webchess.common.net.WebChessSession;
import net.daporkchop.webchess.common.net.packet.*;
import net.daporkchop.webchess.common.user.User;
import net.daporkchop.webchess.common.user.UserGameStats;
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
    public AbstractBoard currentBoard;
    public Game currentGame;
    public WebChessSessionServer currentOpponent;
    public AbstractPlayer currentPlayer;

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
        if (this.user != null) {
            this.send(new UserDataPacket(this.user.getName(), this.user));
        }
    }

    @Override
    public void handle(StartGameRequestPacket packet) {
        System.out.printf("User %s requesting to start new %s game (user score: %d)\n", this.user.getName(), packet.game, this.user.getStats(packet.game).score.get());

        if (false) {
            //debug: make a game against nobody lol
            //User tempUser = new User(Sha512Helper.sha512(new byte[2]), "jeff");
            //this.send(new UserDataPacket("jeff", tempUser));
            //this.send(new BeginGamePacket(packet.game, new String[]{this.user.getName(), "jeff"}, new Side[]{Side.BLACK, Side.WHITE}));
            //this.currentBoard = new ChessBoard();
        } else {
            this.server.matcher.submit(packet.game, this);
        }
    }

    @Override
    public void handle(UserDataRequestPacket packet) {
        User user = this.server.db.get(packet.name);
        this.send(new UserDataPacket(packet.name, user));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handle(MoveFigurePacket packet) {
        if (!this.isIngame()) {
            throw new IllegalStateException("not ingame!");
        }
        switch (this.currentGame) {
            case CHESS: {
                ChessBoard board = (ChessBoard) this.currentBoard;
                System.out.printf("%s (%s) is moving, up now: %s\n", this.user.getName(), this.currentPlayer.side.name(), this.currentBoard.upNow.name());
                if (this.currentPlayer.side != board.upNow) {
                    throw new IllegalStateException("cannot move dummy");
                }
                board.updateValidMoves();
                ChessFigure figure = board.setFigure(packet.src.getX(), packet.src.getY(), null);
                if (figure != null) {
                    BoardPos<ChessBoard> dst = new BoardPos<>(board, packet.dst.getX(), packet.dst.getY());
                    if (/*figure.getValidMovePositions().contains(dst) && */figure.isValidMove(dst)) {
                        ChessFigure current = dst.getFigure();
                        if (current != null) {
                            //piece killed
                            if (current instanceof King) {
                                this.endGame(current.getSide() != this.currentPlayer.side);
                                return;
                            }
                            UpdateScorePacket updateScorePacket = new UpdateScorePacket(this.currentPlayer.points.addAndGet(current.getValue()), this.user.getName());
                            this.send(updateScorePacket);
                            this.currentOpponent.send(updateScorePacket);
                        }
                        dst.setFigure(figure);

                        this.send(packet);
                        this.currentOpponent.send(packet);

                        if (figure instanceof King && ((King) figure).getCastlePositions().contains(dst))    {
                            dst.setFigure(figure);
                            int diff = (packet.src.getX() - packet.dst.getX() < 0 ? 1 : -1);
                            int x = packet.src.getX();
                            x += diff;
                            do {
                                if (this.currentBoard.getFigure(x, dst.y) instanceof Rook)  {
                                    Rook rook = (Rook) this.currentBoard.setFigure(x, dst.y, null);
                                    this.currentBoard.setFigure(dst.x - diff, dst.y, rook);
                                    packet = new MoveFigurePacket(
                                            new Vec2i(x, dst.y),
                                            new Vec2i(dst.x - diff, dst.y)
                                    );
                                    this.send(packet);
                                    this.currentOpponent.send(packet);
                                }
                                x += diff;
                            }while (x < 8 && x >= 0);
                        }

                        //TODO: scan for checkmate
                        if (true) {
                            SetNextTurnPacket nextTurnPacket = new SetNextTurnPacket(board.changeUp());
                            this.send(nextTurnPacket);
                            this.currentOpponent.send(nextTurnPacket);
                        }
                        //System.out.printf("Next up: %s\n", nextTurnPacket.side.name());
                    } else {
                        throw new IllegalArgumentException("invalid destination coordinates!");
                    }
                }
            }
            break;
            case GO: {
                //TODO: implement moving on server
                GoBoard board = (GoBoard) this.currentBoard;
                BoardPos<GoBoard> pos = new BoardPos<>(board, packet.dst.getX(), packet.dst.getY());
                if (board.canPlace(pos))    {
                    pos.setFigure(new GoFigure(board, Side.values()[packet.src.getX()], pos.x, pos.y));

                    //this.send(packet);
                    this.currentOpponent.send(packet);

                    //TODO: everything else related to game logic
                    //both people skip => end of game
                    //surround pieces => removed + points for area covered
                    board.updateValidMoves();
                    if (true) {
                        SetNextTurnPacket nextTurnPacket = new SetNextTurnPacket(board.changeUp());
                        this.send(nextTurnPacket);
                        this.currentOpponent.send(nextTurnPacket);
                    }
                }
            }
            break;
        }
    }

    public WebChessSessionServer challenged;

    @Override
    public void handle(InstantWinPacket packet) {
        if (!IDE)   {
            throw new IllegalStateException("not in IDE mode!");
        }
        if (!this.isIngame())   {
            throw new IllegalStateException("not ingame!");
        }
        this.endGame(true);
    }

    public boolean isIngame() {
        return this.currentBoard != null;
    }

    public void beginGame(@NonNull BeginGamePacket packet, @NonNull Game game, @NonNull AbstractBoard board, @NonNull WebChessSessionServer other, @NonNull AbstractPlayer currentPlayer) {
        /*if (this.currentGame != null) {
            throw new IllegalStateException();
        } else if (this.currentBoard != null) {
            throw new IllegalStateException();
        }*/
        if (this.currentBoard != null)   {
            throw new IllegalStateException();
        }
        this.currentGame = game;
        this.currentBoard = board;
        this.currentOpponent = other;
        this.currentPlayer = currentPlayer;
        this.send(new UserDataPacket(other.user.getName(), other.user));
        this.send(packet);
    }

    public void opponentLeft() {
        this.send(new OpponentLeftPacket());
    }

    public void endGame(boolean victory) {
        this.updateRank(victory, this.currentGame);
        this.currentBoard = null;
        if (this.currentOpponent.isIngame()) {
            this.currentOpponent.endGame(!victory);
        }
        this.send(new EndGamePacket(victory ? this.user.getName() : this.currentOpponent.user.getName()));
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this;
    }

    public void updateRank(boolean victory, @NonNull Game game) {
        UserGameStats stats = this.user.getStats(game);
        stats.update(victory ? GameOutcome.VICTORY : GameOutcome.DEFEAT);
        this.send(new UserDataPacket(this.user.getName(), this.user));
    }
}
