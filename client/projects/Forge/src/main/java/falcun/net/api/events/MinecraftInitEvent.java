package falcun.net.api.events;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.Event;

public class MinecraftInitEvent extends Event {
	private Minecraft minecraft;

	public MinecraftInitEvent(Minecraft minecraft) {
		this.minecraft = minecraft;
	}

	public Minecraft getMinecraft() {
		return minecraft;
	}
}