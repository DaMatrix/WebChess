package net.daporkchop.webchess.common.util;

import net.daporkchop.lib.binary.stream.DataIn;
import net.daporkchop.lib.binary.stream.DataOut;

import java.io.IOException;

/**
 * @author DaPorkchop_
 */
public interface Data {
    void read(DataIn in) throws IOException;

    void write(DataOut out) throws IOException;
}
