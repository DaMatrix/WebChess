package net.daporkchop.webchess.common.game.impl.chess.figure;

import net.daporkchop.webchess.common.game.impl.BoardPos;
import net.daporkchop.webchess.common.game.impl.Direction;
import net.daporkchop.webchess.common.game.impl.Side;
import net.daporkchop.webchess.common.game.impl.chess.ChessBoard;

import java.util.ArrayDeque;
import java.util.Collection;

/**
 * @author DaPorkchop_
 */
public class Bishop extends ChessFigure {
    public Bishop(ChessBoard board, Side side, int x, int y) {
        super(board, side, x, y);
    }

    @Override
    public int getValue() {
        return 3;
    }

    @Override
    public void updateValidMovePositions() {
        this.positions = new ArrayDeque<>();
        BoardPos<ChessBoard> pos = new BoardPos<>(this.board, this.x, this.y);
        Direction.forEachDiag(dir -> {
            BoardPos<ChessBoard> pos1 = pos.clone();
            while (true)    {
                pos1 = dir.offset(pos1);
                if (pos1.isOnBoard())   {
                    ChessFigure figure = this.board.getFigure(pos1.x, pos1.y);
                    if (figure != null && !this.canAttack(figure))  {
                        break;
                    }
                    this.positions.add(pos1);
                    if (figure != null) {
                        break;
                    }
                } else {
                    break;
                }
            }
        });
    }

    @Override
    public char getCode() {
        return this.side == Side.WHITE ? 'B' : 'V';
    }
}
