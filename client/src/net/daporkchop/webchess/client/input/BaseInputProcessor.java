package net.daporkchop.webchess.client.input;

import com.badlogic.gdx.InputProcessor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.daporkchop.webchess.client.ClientMain;

import java.util.ArrayDeque;
import java.util.Collection;

/**
 * @author DaPorkchop_
 */
@RequiredArgsConstructor
public class BaseInputProcessor implements InputProcessor {
    @NonNull
    private final ClientMain client;

    private final Collection<InputProcessor> listeners = new ArrayDeque<>();

    public void registerProcessor(@NonNull InputProcessor processor)    {
        this.listeners.add(processor);
    }

    public void removeProcessor(@NonNull InputProcessor processor)    {
        this.listeners.remove(processor);
    }

    @Override
    public boolean keyDown(int keycode) {
        this.listeners.forEach(l -> l.keyDown(keycode));
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        this.listeners.forEach(l -> l.keyUp(keycode));
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        this.listeners.forEach(l -> l.keyTyped(character));
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        this.listeners.forEach(l -> l.touchDown(screenX, screenY, pointer, button));
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        this.listeners.forEach(l -> l.touchUp(screenX, screenY, pointer, button));
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        this.listeners.forEach(l -> l.touchDragged(screenX, screenY, pointer));
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        this.listeners.forEach(l -> l.mouseMoved(screenX, screenY));
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        this.listeners.forEach(l -> l.scrolled(amount));
        return false;
    }
}
