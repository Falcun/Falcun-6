package falcun.xyz.dev.boredhuman.dancore.falcunfork.chunks;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.Credits;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.Field;

@Credits("https://github.com/boredhuman/Dancore/blob/master/src/main/java/dev/boredhuman/mods/chunks/ChunkMod.java")
public class DanChunkMod {
	private Field target;
	private Field target2;

	public DanChunkMod() {
		try {
			for (Field field : WorldClient.class.getDeclaredFields()) {
				if (field.getType().equals(ChunkProviderClient.class)) {
					field.setAccessible(true);
					this.target = field;
					break;
				}
			}
			for (Field field : World.class.getDeclaredFields()) {
				if (field.getType().equals(IChunkProvider.class)) {
					field.setAccessible(true);
					this.target2 = field;
					break;
				}
			}
		} catch (Throwable err) {
			err.printStackTrace();
		}
	}

	@SubscribeEvent
	public void loadWorldEvent(WorldEvent.Load loadWorld) {
		if (loadWorld.world.getClass().equals(WorldClient.class)) {
			if (this.target != null) {
				try {
					IChunkProvider provider = new FastChunkProvider(loadWorld.world);
					this.target.set(loadWorld.world, provider);
					this.target2.set(loadWorld.world, provider);
				} catch (Throwable err) {
					err.printStackTrace();
				}
			}
		}
	}

	@SubscribeEvent
	public void tickEvent(TickEvent.ClientTickEvent event) {
		if (event.phase != TickEvent.Phase.END) {
			return;
		}
		World world = Minecraft.getMinecraft().theWorld;
		if (world != null) {
			if (world.getChunkProvider() instanceof FastChunkProvider) {
				((FastChunkProvider) world.getChunkProvider()).onTick();
			}
		}
	}
}
