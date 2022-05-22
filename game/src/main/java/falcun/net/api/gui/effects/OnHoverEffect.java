package falcun.net.api.gui.effects;
import com.mojang.realmsclient.gui.ChatFormatting;
import falcun.net.api.gui.components.*;

import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.function.Consumer;

import falcun.net.api.gui.region.GuiRegion;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
public class OnHoverEffect extends Effect {
	BiConsumer<Component, Phase> onHover;

	public OnHoverEffect(BiConsumer<Component, Phase> onHover) {
		this.onHover = onHover;
	}

	@Override
	public void onHover(int mX, int mY, Component component, Phase phase) {
		onHover.accept(component, phase);
	}

}
