package falcun.net.api.gui.effects;

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
