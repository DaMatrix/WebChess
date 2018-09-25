package net.daporkchop.webchess.client.util;

/**
 * @author DaPorkchop_
 */
public interface Disposable extends ClientConstants {
    void create();

    void dispose();
}
