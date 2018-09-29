package net.daporkchop.webchess.client.util;

import com.badlogic.gdx.Gdx;
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
        screenY = (Gdx.graphics.getHeight() - screenY) - this.y;
        float x = (float) screenX / this.xScale;
        float y = (float) screenY / this.yScale;
        if (false) {
            System.out.printf("Out: (%d,%d) In: (%d,%d), settings: %d,%d,%d,%d,%f,%f\n",
                    (int) x, (int) y, screenX + this.x, screenY + this.y, this.width, this.height, this.x, this.y, this.xScale, this.yScale);
        }
        return new Vec2i((int) x, (int) y);
    }

    public int displayToLocalY(int screenY) {
        return (int) ((float) ((Gdx.graphics.getHeight() - screenY) - this.y) / this.yScale);
    }

    public int displayToLocalX(int screenX) {
        return (int) ((float) (screenX - this.x) / this.xScale);
    }
}
