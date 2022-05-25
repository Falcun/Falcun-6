package falcun.net.api.gui.effects;
import com.mojang.realmsclient.gui.ChatFormatting;
import falcun.net.api.gui.components.*;

import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.function.Consumer;

import falcun.net.api.gui.region.GuiRegion;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;

public class OnClickEffect extends Effect {
	Consumer<Component> onClick;
	private long cooldown = 0L;

	public OnClickEffect(Consumer<Component> onClick) {
		this.onClick = onClick;
	}

	@Override
	public void onClick(int mX, int mY, int mouseButton, Component component, Phase phase) {
		if (phase == Phase.AFTER && Mouse.getEventButtonState() && cooldown <= System.currentTimeMillis()) {
			onClick.accept(component);
			cooldown = System.currentTimeMillis() + 20;
		}
	}

}