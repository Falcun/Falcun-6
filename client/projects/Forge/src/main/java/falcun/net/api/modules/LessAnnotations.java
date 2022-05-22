package falcun.net.api.modules;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public final class LessAnnotations {
	public static void setup() {
		MinecraftForge.EVENT_BUS.register(new LessAnnotations());
	}
	@SubscribeEvent
	public void tick(TickEvent.ClientTickEvent e){

	}

}
