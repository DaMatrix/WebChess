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

package net.daporkchop.webchess.common.game.impl.chess;

import net.daporkchop.webchess.common.game.AbstractBoard;
import net.daporkchop.webchess.common.game.impl.Side;
import net.daporkchop.webchess.common.game.impl.chess.figure.*;

/**
 * @author DaPorkchop_
 */
public class ChessBoard extends AbstractBoard<ChessPlayer, ChessFigure> {
    public ChessBoard() {
        super(ChessPlayer.class, ChessFigure.class, 8);
    }

    @Override
    protected void initBoard() {
        Side.forEach(side -> {
            for (int i = 7; i >= 0; i--) {
                new Pawn(this, side, i, 1);
            }
            new Rook(this, side, 0, 0);
            new Knight(this, side, 1, 0);
            new Bishop(this, side, 2, 0);
            new Queen(this, side, 3, 0);
            new King(this, side, 4, 0);
            new Bishop(this, side, 5, 0);
            new Knight(this, side, 6, 0);
            new Rook(this, side, 7, 0);
            this.flip();
        });
        this.updateValidMoves();
    }

    public void updateValidMoves() {
        for (int x = this.sizeIntern; x >= 0; x--) {
            for (int y = this.sizeIntern; y >= 0; y--) {
                ChessFigure figure = this.getFigure(x, y);
                if (figure != null) {
                    figure.updateValidMovePositions();
                }
            }
        }
    }
}
