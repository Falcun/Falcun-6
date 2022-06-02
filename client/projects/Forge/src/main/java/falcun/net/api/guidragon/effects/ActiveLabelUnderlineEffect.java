package falcun.net.api.guidragon.effects;
import com.mojang.realmsclient.gui.ChatFormatting;
import falcun.net.api.guidragon.components.*;
import falcun.net.api.guidragon.components.text.Label;

import java.util.function.Supplier;

public class ActiveLabelUnderlineEffect extends Effect {

    private final Label label;
    private final Supplier<Boolean> isActive;
    private final String constantText;

    public ActiveLabelUnderlineEffect(Label label, String constantText, Supplier<Boolean> isActive) {
        this.label = label;
        this.constantText = constantText;
        this.isActive = isActive;
    }

    @Override
    public void draw(int mX, int mY, Component component, Phase phase) {
        boolean active = isActive.get();
        if (active) label.text = () -> ChatFormatting.UNDERLINE + constantText;

        if (!active && !label.isOver(mX, mY) &&
                label.text.get().equalsIgnoreCase(ChatFormatting.UNDERLINE + constantText))
            label.text = () -> constantText;
    }

}
