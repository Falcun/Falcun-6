package falcun.net;

import falcun.net.api.fonts.Fonts;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.hud.FalcunHudModule;
import falcun.net.api.textures.FalcunTexture;
import falcun.net.managers.FalcunConfigManager;
import falcun.net.managers.FalcunKeyBindManager;
import falcun.net.modules.hypixel.skyblock.utils.SkyblockPlayer;
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
import java.nio.ByteBuffer;
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
		new SkyblockPlayer();
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
	}

	@SubscribeEvent
	public void renderOverlay(RenderGameOverlayEvent e) {

		if (e.type == RenderGameOverlayEvent.ElementType.HOTBAR) {
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

}
