package falcun.net.modules.hypixel.skyblock;

import falcun.net.Falcun;
import falcun.net.api.events.TickingEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.regex.Pattern;

public final class Checks {
	public static final Checks instance = new Checks();

	public static void init() {
		MinecraftForge.EVENT_BUS.register(instance);

	}

	private Checks() {
	}

	public boolean onHypixel = false, onSkyblock = false;

	private long time = 0;

	public boolean isOnHypixel() {
		EntityPlayerSP player = Falcun.minecraft.thePlayer;
		if (player == null) {
			return false;
		}
		String brand = player.getClientBrand();
		if (brand != null) {
			if (brand.toLowerCase().contains("hypixel")) {
				return true;
			}
//			for (Pattern p : main.getOnlineData().getHypixelBrands()) {
//				if (p.matcher(brand).matches()) {
//					return true;
//				}
//			}
		}
		return false;
	}

	@SubscribeEvent
	public void ticker(TickEvent.ClientTickEvent e) {
//		System.out.println("client");
	}


	@SubscribeEvent
	public void ticks(TickingEvent e) {
		if (++time % 7 != 0) return;
		onHypixel = isOnHypixel();
		if (!onHypixel) return;
		scoreboard();
	}

	void scoreboard() {
		boolean foundScoreboard = false;

		boolean foundActiveSoup = false;
		boolean foundLocation = false;
		boolean foundJerryWave = false;
		boolean foundAlphaIP = false;
		boolean foundInDungeon = false;
		boolean foundSlayerQuest = false;
		boolean foundBossAlive = false;
		boolean foundSkyblockTitle = false;


	}

}
