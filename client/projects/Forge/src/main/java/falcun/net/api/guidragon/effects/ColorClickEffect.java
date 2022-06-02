package falcun.net.api.guidragon.effects;
import falcun.net.api.guidragon.components.*;

import org.lwjgl.input.Mouse;

import java.util.function.BiConsumer;

public class ColorClickEffect extends Effect {

    private long cooldown = 0L;
    BiConsumer<Component, Integer> onChange;

    public ColorClickEffect(BiConsumer<Component, Integer> onChange) {
        this.onChange = onChange;
    }

    @Override
    public void onClick(int mX, int mY, int mouseButton, Component component, Phase phase) {
        if (phase == Phase.AFTER && Mouse.getEventButtonState() && cooldown <= System.currentTimeMillis()) {
            onChange.accept(component, mX);
            cooldown = System.currentTimeMillis() + 20;
        }
    }

}
