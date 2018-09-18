package net.daporkchop.webchess.common.net.packet;

/**
 * @author DaPorkchop_
 */
public class ColorUpdatePacket {
    public int color;

    public ColorUpdatePacket()  {
    }

    public ColorUpdatePacket(int color) {
        this.color = color;
    }
}
