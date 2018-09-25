package net.daporkchop.webchess.client.render.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.daporkchop.webchess.client.render.IRenderer;

import java.awt.*;

/**
 * @author DaPorkchop_
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BackgroundRenderer implements IRenderer {
    private float r = 0.75f;
    private float g = 0.85f;
    private float b = 0.75f;

    public void setRgb(int rgb) {
        this.r = (float) ((rgb >> 16) & 0xFF) / 255.0f;
        this.g = (float) ((rgb >> 8) & 0xFF) / 255.0f;
        this.b = (float) (rgb & 0xFF) / 255.0f;
    }

    public void setRgb(float r, float g, float b)   {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    @Override
    public void render(int tick, float partialTicks) {
        Gdx.gl.glClearColor(this.r, this.g, this.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void create() {
        this.setRgb(0xFFDFC2);
    }

    @Override
    public void dispose() {
    }
}
