package falcun.net.api.gui.effects;
import com.mojang.realmsclient.gui.ChatFormatting;
import falcun.net.api.gui.components.*;

import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.function.Consumer;

import falcun.net.api.gui.region.GuiRegion;
import falcun.net.api.gui.util.ScissorManager;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
public class ScissorEffect extends Effect {

	public GuiRegion custom;

	public ScissorEffect(GuiRegion custom) {
		this.custom = custom;
	}

	@Override
	public void draw(int mX, int mY, Component component, Phase phase) {
		if (phase == Phase.BEFORE) {
			ScissorManager.startScissor(custom == null ? component.region : custom);
		} else {
			ScissorManager.finishScissor();
		}
	}

}