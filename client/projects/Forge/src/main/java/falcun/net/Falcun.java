package falcun.net;

import falcun.net.api.fonts.Fonts;
import falcun.net.api.textures.FalcunTexture;
import falcun.net.managers.FalcunGuiManager;
import falcun.net.managers.FalcunKeyBindManager;
import falcun.net.managers.FalcunModuleManager;
import falcun.net.util.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod(modid = "Falcun", name = "Falcun", version = "6.0.0")
public final class Falcun {

	public static Minecraft minecraft = Minecraft.getMinecraft();

	public static Falcun instance;

	public Falcun() {
		instance = this;
	}

	@SubscribeEvent
	public void tick(TickEvent.ClientTickEvent e) {
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		FalcunTexture.setupArrays();
		MinecraftForge.EVENT_BUS.register(this);
		FalcunModuleManager.init();
		MinecraftForge.EVENT_BUS.register(FalcunKeyBindManager.instance);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		System.out.println(Fonts.Roboto.getStringWidth("1"));
	}

}
