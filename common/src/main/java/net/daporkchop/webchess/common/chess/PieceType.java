package net.daporkchop.webchess.common.chess;

import net.daporkchop.webchess.common.chess.piece.ITypeInfo;

import java.util.Objects;

/**
 * @author DaPorkchop_
 */
public enum PieceType {
    ;

    public final ITypeInfo typeImpl;

    PieceType(ITypeInfo typeImpl)   {
        Objects.requireNonNull(typeImpl);

        this.typeImpl = typeImpl;
    }
}
