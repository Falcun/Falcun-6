package falcun.net.api.gui.effects;
import com.mojang.realmsclient.gui.ChatFormatting;
import falcun.net.api.gui.components.*;

import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.function.Consumer;

import falcun.net.api.gui.region.GuiRegion;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class OpenGuiEffect extends OnClickEffect {

	public OpenGuiEffect(GuiScreen gui) {
		super(comp -> {
			Minecraft minecraft = Minecraft.getMinecraft();
			if (minecraft.currentScreen != null && minecraft.currentScreen.getClass() == gui.getClass()) return;
			Minecraft.getMinecraft().displayGuiScreen(gui);
		});
	}

}
