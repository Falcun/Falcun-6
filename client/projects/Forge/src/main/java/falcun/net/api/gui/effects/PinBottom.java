package falcun.net.api.gui.effects;
import com.mojang.realmsclient.gui.ChatFormatting;
import falcun.net.api.gui.components.*;

import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.function.Consumer;

import falcun.net.api.gui.region.GuiRegion;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;

public class PinBottom extends Effect {

	Component pinner;

	public PinBottom(Component pinner) {
		this.pinner = pinner;
	}

	@Override
	public void draw(int mX, int mY, Component component, Phase phase) {
		if(phase == Phase.AFTER) {
			return;
		}
		component.region.y = pinner.region.getBottom();
	}
}