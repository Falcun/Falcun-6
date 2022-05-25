package falcun.net.api.events;

import falcun.net.Falcun;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.Event;

public class MinecraftInitEvent extends Event {
	public Minecraft getMinecraft() {
		return Falcun.minecraft;
	}
}