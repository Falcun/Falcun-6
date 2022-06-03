package falcun.net;

import falcun.net.api.events.TickingEvent;
import falcun.net.api.fonts.Fonts;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.hud.FalcunHudModule;
import falcun.net.api.textures.FalcunTexture;
import falcun.net.guidragonclient.ingame.hud.FalcunHudEditor;
import falcun.net.managers.FalcunConfigManager;
import falcun.net.managers.FalcunKeyBindManager;
import falcun.net.managers.FalcunScoreBoardManager;
import falcun.net.modules.hypixel.skyblock.Checks;
import falcun.net.modules.hypixel.skyblock.utils.SkyblockPlayer;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.api.DanCoreAPI;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.api.textures.TextureManager;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.compute.LineRenderer;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.events.RenderHook;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.util.FutureCallBack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.Display;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Mod(modid = "Falcun", name = "Falcun", version = "6.0.0")
public final class Falcun extends DanCoreAPI {

	public static Minecraft minecraft = Minecraft.getMinecraft();

	public static Falcun instance;

	public Falcun() {
		instance = this;
	}

	@SubscribeEvent
	public void tick(TickEvent.ClientTickEvent e) {
		if (Falcun.minecraft.theWorld != null && Falcun.minecraft.thePlayer != null && e.phase == TickEvent.Phase.START) {
//			FalcunScoreBoardManager.tick();
			TickingEvent event = new TickingEvent();
			MinecraftForge.EVENT_BUS.post(event);
		}
	}

	public static native void process();

	public static native void count();

	public static native void render();

	public static native void run();


	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		try {
			Field field = DanCoreAPI.class.getDeclaredField("API");
			field.setAccessible(true);
			field.set(null, this);
		} catch (Throwable err) {
			err.printStackTrace();
		}
		try {
			InputStream is = Falcun.minecraft.getResourceManager().getResource(new ResourceLocation("falcun:falcun16x16.png")).getInputStream();
			InputStream is1 = Falcun.minecraft.getResourceManager().getResource(new ResourceLocation("falcun:falcun32x32.png")).getInputStream();
			ByteBuffer a = readImageToBuffer(is);
			ByteBuffer b = readImageToBuffer(is1);
			Display.setIcon(new ByteBuffer[]{a, b});
		} catch (Throwable err) {
			err.printStackTrace();
		}
		FalcunTexture.setupArrays();
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(FalcunKeyBindManager.instance);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		System.out.println(Fonts.Roboto.getStringWidth("Falcun Client Loading..."));
		FalcunConfigManager.init();
		LineRenderer.init();
		new SkyblockPlayer();
		Checks.init();
	}

	@SubscribeEvent
	public void renderOverlay(RenderGameOverlayEvent e) {
		if (e.type == RenderGameOverlayEvent.ElementType.HOTBAR && !(Falcun.minecraft.currentScreen instanceof FalcunHudEditor)) {
			int old = minecraft.gameSettings.guiScale;
			minecraft.gameSettings.guiScale = 1;
			minecraft.entityRenderer.setupOverlayRendering();
			minecraft.gameSettings.guiScale = old;
			for (FalcunModule value : FalcunConfigManager.modules.values()) {
				if (!value.isEnabled()) continue;
				if (value instanceof FalcunHudModule) {
					FalcunHudModule hudElement = (FalcunHudModule) value;
					float scale = (float) (hudElement.scale.getValue()) / 200.0F;
					GlStateManager.pushMatrix();
					GlStateManager.translate((scale - 1.0F) * -hudElement.screenPosition.getValue().first, (scale - 1.0F) * -hudElement.screenPosition.getValue().second, 0.0F);
					GlStateManager.scale(scale, scale, 1.0F);
					hudElement.render();
					GlStateManager.scale(1.0F / scale, 1.0F / scale, 1.0F);
					GlStateManager.translate((scale - 1.0F) * hudElement.screenPosition.getValue().first, (scale - 1.0F) * hudElement.screenPosition.getValue().second, 0.0F);
					GlStateManager.popMatrix();
				}
			}
			minecraft.entityRenderer.setupOverlayRendering();
		}
	}

	private static ByteBuffer readImageToBuffer(InputStream imageStream) throws IOException {
		BufferedImage bufferedimage = ImageIO.read(imageStream);
		int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), (int[]) null, 0, bufferedimage.getWidth());
		ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);

		for (int i : aint) {
			bytebuffer.putInt(i << 8 | i >> 24 & 255);
		}

		bytebuffer.flip();
		imageStream.close();
		return bytebuffer;
	}

	private static final ExecutorService workerPool = Executors.newFixedThreadPool(4);

	private transient final RenderHook renderHook = phase -> {
		if (phase == RenderHook.Phase.SETUP) {
			GlStateManager.disableDepth();
		} else {
			GlStateManager.enableDepth();
		}
	};


	public static void saveConfig() {
		submitJob(() -> {
			try {
				FalcunConfigManager.saveAllModules();
				return true;
			} catch (Throwable err) {
				err.printStackTrace();
			}
			return false;
		}, bool -> {
			if (!bool) {

			}
		});
	}

	public static <T> void submitJob(Callable<T> job, Consumer<T> callBack) {
		workerPool.submit(new FutureCallBack<>(job, callBack));
	}

	public static void runLater(Runnable r, long delayInMillis) { // TODO: MAKE A SYNC METHOD FOR RUNNING A TASK LATER ON MAIN THREAD WITHOUT USE OF SCHEDULER

	}

	private final TextureManager textureManager = new TextureManager();

	@Override
	public TextureManager getTextureManager() {
		return textureManager;
	}
}
