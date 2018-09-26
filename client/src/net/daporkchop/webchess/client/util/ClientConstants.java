package net.daporkchop.webchess.client.util;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import net.daporkchop.webchess.common.util.Constants;

/**
 * @author DaPorkchop_
 */
public interface ClientConstants extends Constants {
    SpriteBatch batch = new SpriteBatch();

    float ASPECT_W = 2;
    float ASPECT_H = 3;

    int TARGET_WIDTH = 8 * 64;
    int TARGET_HEIGHT = 12 * 64;

    CoordinateOffset coordinateOffset = new CoordinateOffset();
}
