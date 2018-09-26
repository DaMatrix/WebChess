package net.daporkchop.webchess.client.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.daporkchop.lib.math.vector.i.Vec2i;

/**
 * @author DaPorkchop_
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CoordinateOffset {
    private int width;
    private int height;
    private int x;
    private int y;
    private float xScale = 1.0f;
    private float yScale = 1.0f;

    public Vec2i translateDisplayToAbsolute(int screenX, int screenY) {
        screenX -= this.x;
        screenY -= this.y;
        float x = (float) screenX / this.xScale;
        float y = (float) (this.height - screenY) / this.yScale;
        return new Vec2i((int) x, (int) y);
    }
}
