package falcun.net;

import com.google.common.util.concurrent.FutureCallback;
import falcun.net.api.fonts.Fonts;
import falcun.net.api.textures.FalcunTexture;
import falcun.net.managers.FalcunKeyBindManager;
import falcun.net.managers.FalcunModuleManager;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.util.FutureCallBack;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Mod(modid = "Falcun", name = "Falcun", version = "6.0.0")
public final class Falcun {

	public static Minecraft minecraft = Minecraft.getMinecraft();

	public static Falcun instance; // r

	public Falcun() {
		instance = this;
	}

	@SubscribeEvent
	public void tick(TickEvent.ClientTickEvent e) {
	}

	public static native void process();

	public static native void count();

	public static native void render();

	public static native void run();
	

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		FalcunTexture.setupArrays();
		MinecraftForge.EVENT_BUS.register(this);
		FalcunModuleManager.init();
		MinecraftForge.EVENT_BUS.register(FalcunKeyBindManager.instance);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		System.out.println(Fonts.Roboto.getStringWidth("aaAA"));
	}

	private static final ExecutorService workerPool = Executors.newFixedThreadPool(4);

	public static <T> void submitJob(Callable<T> job, Consumer<T> callBack) {
		workerPool.submit(new FutureCallBack<>(job, callBack));
	}

}
