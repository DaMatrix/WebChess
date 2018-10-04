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

package net.daporkchop.webchess.common.game.impl.chess.figure;

import lombok.Getter;
import lombok.NonNull;
import net.daporkchop.webchess.common.game.impl.BoardPos;
import net.daporkchop.webchess.common.game.impl.Direction;
import net.daporkchop.webchess.common.game.impl.Side;
import net.daporkchop.webchess.common.game.impl.chess.ChessBoard;

import java.util.ArrayDeque;
import java.util.Collection;

/**
 * @author DaPorkchop_
 */
public class King extends ChessFigure {
    private final Collection<BoardPos<ChessBoard>> kingMoves = new ArrayDeque<>();
    @Getter
    private final Collection<BoardPos<ChessBoard>> threats = new ArrayDeque<>();

    public King(ChessBoard board, Side side, int x, int y) {
        super(board, side, x, y);
    }

    @Override
    public int getValue() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void updateValidMovePositions() {
        this.positions = new ArrayDeque<>();
        BoardPos<ChessBoard> pos = new BoardPos<>(this.board, this.x, this.y);
        Direction.forEach(dir -> {
            BoardPos<ChessBoard> pos1 = dir.offset(pos);
            if (pos1.isOnBoard()) {
                ChessFigure figure = pos1.getFigure();
                if ((figure == null) || this.canAttack(figure)) {
                    this.positions.add(pos1);
                }
            }
        });
    }

    public void scanCheck_doNotCall() {
        this.kingMoves.clear();
        this.kingMoves.addAll(this.positions);
        for (int x = 7; x >= 0; x--) {
            for (int y = 7; y >= 0; y--) {
                ChessFigure figure = this.board.getFigure(x, y);
                if (figure != null && figure.getSide() != this.side) {
                    this.kingMoves.removeAll(figure.positions);
                }
            }
        }
        this.scanThreats(this.threats, new BoardPos<>(this.board, this.x, this.y));
    }

    private void scanThreats(@NonNull Collection<BoardPos<ChessBoard>> threats, @NonNull BoardPos<ChessBoard> pos) {
        threats.clear();
        for (int x = 7; x >= 0; x--) {
            for (int y = 7; y >= 0; y--) {
                ChessFigure figure = this.board.getFigure(x, y);
                if (figure != null && figure.getSide() != this.side && figure.positions.contains(pos)) {
                    threats.add(new BoardPos<>(this.board, figure.getX(), figure.getY()));
                }
            }
        }
    }

    public void scanCheckFinal_doNotCall() {
        this.positions.clear();
        this.positions.addAll(this.kingMoves);
        //System.out.printf("kingMoves: %d\n", this.kingMoves.size());
    }

    @Override
    public char getCode() {
        return (this.side == Side.WHITE) ? 'K' : 'L';
    }

    @Override
    public boolean isValidMove(BoardPos<ChessBoard> pos) {
        return !this.isCheck(pos) && super.isValidMove(pos);
    }

    public boolean isCheck() {
        return !this.threats.isEmpty();
    }

    public boolean isCheck(BoardPos<ChessBoard> pos) {
        Collection<BoardPos<ChessBoard>> threats = new ArrayDeque<>();
        this.scanThreats(threats, pos);
        return !threats.isEmpty();
        /*if (!threats.isEmpty()) {
            return true;
        }
        //TODO: detect checkmate (attention: you can move pieces into the way or kill the threat! (difficult :/))
        throw new UnsupportedOperationException();*/
    }
}
