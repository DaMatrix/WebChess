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

package net.daporkchop.webchess.common.game.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.daporkchop.webchess.common.game.AbstractBoard;

import java.util.function.Consumer;

/**
 * @author DaPorkchop_
 */
@RequiredArgsConstructor
public enum Direction {
    UP(0, 1),
    DOWN(0, -1),
    LEFT(-1, 0),
    RIGHT(1, 0),
    UP_LEFT(-1, 1),
    UP_RIGHT(1, 1),
    DOWN_LEFT(-1, -1),
    DOWN_RIGHT(1, -1);

    private final int vecX;
    private final int vecY;

    public static void forEachAxis(@NonNull Consumer<Direction> consumer) {
        consumer.accept(UP);
        consumer.accept(RIGHT);
        consumer.accept(DOWN);
        consumer.accept(LEFT);
    }

    public static void forEachVert(@NonNull Consumer<Direction> consumer) {
        consumer.accept(UP);
        consumer.accept(DOWN);
    }

    public static void forEachHoriz(@NonNull Consumer<Direction> consumer) {
        consumer.accept(RIGHT);
        consumer.accept(LEFT);
    }

    public static void forEachPerpendicular(@NonNull Consumer<Direction> consumer, @NonNull Direction base) {
        switch (base) {
            case UP:
            case DOWN:
                forEachHoriz(consumer);
                break;
            case LEFT:
            case RIGHT:
                forEachVert(consumer);
                break;
            default:
                throw new IllegalArgumentException(String.format("Cannot get line perpendicular to %s", base.name()));
        }
    }

    public static void forEachNeighboringDiagonal(@NonNull Consumer<Direction> consumer, @NonNull Direction base) {
        switch (base) {
            case UP: {
                consumer.accept(UP_LEFT);
                consumer.accept(UP_RIGHT);
            }
            break;
            case DOWN: {
                consumer.accept(DOWN_LEFT);
                consumer.accept(DOWN_RIGHT);
            }
            break;
            case LEFT: {
                consumer.accept(UP_LEFT);
                consumer.accept(DOWN_LEFT);
            }
            break;
            case RIGHT: {
                consumer.accept(UP_RIGHT);
                consumer.accept(DOWN_RIGHT);
            }
            break;
            default:
                throw new IllegalArgumentException(String.format("Cannot get neoghboring diagonal to %s", base.name()));
        }
    }

    public static void forEachDiag(@NonNull Consumer<Direction> consumer) {
        consumer.accept(UP_LEFT);
        consumer.accept(UP_RIGHT);
        consumer.accept(DOWN_LEFT);
        consumer.accept(DOWN_RIGHT);
    }

    public static void forEach(@NonNull Consumer<Direction> consumer) {
        for (Direction dir : values()) {
            consumer.accept(dir);
        }
    }

    public <B extends AbstractBoard> BoardPos<B> offset(@NonNull BoardPos<B> input) {
        return new BoardPos<>(input.board, input.x + this.vecX, input.y + this.vecY);
    }

    public <B extends AbstractBoard> BoardPos<B> offset(@NonNull BoardPos<B> input, int mult) {
        return new BoardPos<>(input.board, input.x + (this.vecX * mult), input.y + (this.vecY * mult));
    }
}
