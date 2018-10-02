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
