package net.daporkchop.webchess.client.input;

import com.badlogic.gdx.InputProcessor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.daporkchop.webchess.client.ClientMain;

import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * @author DaPorkchop_
 */
@RequiredArgsConstructor
public class BaseInputProcessor implements InputProcessor {
    @NonNull
    private final ClientMain client;

    private final Collection<WeakReference<InputProcessor>> listeners = new ArrayDeque<>();

    public void registerProcessor(@NonNull InputProcessor processor)    {
        this.listeners.add(new WeakReference<>(processor));
    }

    public void removeProcessor(@NonNull InputProcessor processor)    {
        this.listeners.removeIf(r -> r.get() == processor);
    }

    @Override
    public boolean keyDown(int keycode) {
        return this.run(l -> l.keyDown(keycode));
    }

    @Override
    public boolean keyUp(int keycode) {
        return this.run(l -> l.keyUp(keycode));
    }

    @Override
    public boolean keyTyped(char character) {
        return this.run(l -> l.keyTyped(character));
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return this.run(l -> l.touchDown(screenX, screenY, pointer, button));
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return this.run(l -> l.touchUp(screenX, screenY, pointer, button));
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return this.run(l -> l.touchDragged(screenX, screenY, pointer));
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return this.run(l -> l.mouseMoved(screenX, screenY));
    }

    @Override
    public boolean scrolled(int amount) {
        return this.run(l -> l.scrolled(amount));
    }

    private boolean run(@NonNull Consumer<InputProcessor> consumer)  {
        this.listeners.removeIf(r -> {
            InputProcessor processor = r.get();
            if (processor == null)  {
                return true;
            } else {
                consumer.accept(processor);
                return false;
            }
        });
        return false;
    }

    public interface BaseInputHandler extends InputProcessor {
        @Override
        default boolean keyDown(int keycode) {
            return false;
        }

        @Override
        default boolean keyUp(int keycode) {
            return false;
        }

        @Override
        default boolean keyTyped(char character) {
            return false;
        }

        @Override
        default boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        default boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        default boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        default boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        default boolean scrolled(int amount) {
            return false;
        }
    }
}
