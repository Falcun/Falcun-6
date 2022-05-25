package falcun.xyz.accswitcher.ru.vidtu.ias.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.google.gson.JsonObject;
import com.mojang.util.UUIDTypeAdapter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import falcun.xyz.accswitcher.ru.vidtu.ias.Config;
import falcun.xyz.accswitcher.ru.vidtu.ias.account.Account;
import falcun.xyz.accswitcher.the_fireplace.ias.IAS;

/**
 * Manager for loading player skins.
 * @author VidTu
 */
public class SkinRenderer {
	/**
	 * Asynchronously load all missing models and faces from players skins.
	 * @param mc Minecraft client instance
	 * @param forceRecache Should we force reload existing models and faces
	 * @param onEnd Action that will be run on end
	 * @see #loadSkinToModel(MinecraftClient, String, boolean)
	 */
	public static void loadAllAsync(Minecraft mc, boolean forceRecache, Runnable onEnd) {
		GuiScreen prev = mc.currentScreen;
		SkinsLoadingScreen sls = new SkinsLoadingScreen();
		mc.displayGuiScreen(sls);
		new Thread(() -> {
			List<Account> accs = Config.accounts;
			for (int i = 0; i < accs.size(); i++) {
				sls.progress = (float)(i + 1F) / (float)accs.size();
				Account data = accs.get(i);
				loadSkin(mc, data.alias(), data.uuid(), forceRecache);
			}
			sls.progress = 1F;
			mc.addScheduledTask(() -> mc.displayGuiScreen(prev));
			mc.addScheduledTask(onEnd);
		}, "IAS Skin Cache Thread").start();
	}
	
	/**
	 * Load model and face of the player to default locations.<br>
	 * Model: <code>/.minecraft/cachedImages/models/playername.png</code><br>
	 * Face: <code>/.minecraft/cachedImages/faces/playername.png</code>
	 * @param mc Minecraft client instance
	 * @param name Player name
	 * @param uuid Player UUID (May be <code>null</code> if unknown)
	 * @param forceRecache Should we force reload existing model and face
	 * @see #loadSkin(MinecraftClient, String, File, File, boolean)
	 */
	public static void loadSkin(Minecraft mc, String name, UUID uuid, boolean forceRecache) {
		loadSkin(mc, name, uuid, new File(new File(mc.mcDataDir, "cachedImages/models"), name + ".png"),
				new File(new File(mc.mcDataDir, "cachedImages/faces"), name + ".png"), forceRecache);
	}
	
	/**
	 * Load model and face of the player to files.
	 * @param mc Minecraft client instance
	 * @param name Player name
	 * @param uuid Player UUID (May be <code>null</code> if unknown)
	 * @param modelF File for model
	 * @param faceF File for face
	 * @param forceRecache Should we force reload existing model and face
	 */
	public static void loadSkin(Minecraft mc, String name, UUID uuid, File modelF, File faceF, boolean forceRecache) {
		if (!modelF.exists() || forceRecache) {
			BufferedImage img;
			boolean slimSkin = false;
			try {
				modelF.getParentFile().mkdirs();
				if (uuid == null) {
					try (InputStreamReader isr = new InputStreamReader(new URL("https://api.mojang.com/users/profiles/minecraft/" + name).openStream(), StandardCharsets.UTF_8)) {
						uuid = UUIDTypeAdapter.fromString(IAS.GSON.fromJson(isr, JsonObject.class).get("id").getAsString());
					}
				}
				String base64Data;
				try (InputStreamReader isr = new InputStreamReader(new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + UUIDTypeAdapter.fromUUID(uuid)).openStream(), StandardCharsets.UTF_8)) {
					base64Data = IAS.GSON.fromJson(isr, JsonObject.class).getAsJsonArray("properties").get(0).getAsJsonObject().get("value").getAsString();
				}
				JsonObject jo = IAS.GSON.fromJson(new String(Base64.getDecoder().decode(base64Data), StandardCharsets.UTF_8), JsonObject.class).getAsJsonObject("textures").getAsJsonObject("SKIN");
				if (jo.has("metadata") && jo.getAsJsonObject("metadata").has("model") && jo.getAsJsonObject("metadata").get("model").getAsString().equalsIgnoreCase("slim")) slimSkin = true;
				String skinUrl = jo.get("url").getAsString();
				img = ImageIO.read(new URL(skinUrl));
			} catch (Throwable t) {
				try {
					try (InputStream is = mc.getResourceManager().getResource(new ResourceLocation("textures/entity/steve.png")).getInputStream()) {
						img = ImageIO.read(is);
					}
				} catch (Throwable th) {
					th.addSuppressed(t);
					IAS.LOG.warn("Unable to load skin: " + name, th);
					return;
				}
			}
			try {
				BufferedImage model = new BufferedImage(16, 32, BufferedImage.TYPE_INT_ARGB); 
				if (img.getWidth() != 64 && (img.getHeight() != 64 || img.getHeight() != 32)) throw new IllegalStateException("Invalid skin size: " + img.getWidth() + "x" + img.getHeight());
				boolean oldSkin = img.getHeight() == 32;
				if (oldSkin) slimSkin = false;
				model.getGraphics().drawImage(img.getSubimage(8, 8, 8, 8), 4, 0, null); //Head
				model.getGraphics().drawImage(img.getSubimage(20, 20, 8, 12), 4, 8, null); //Body
				model.getGraphics().drawImage(img.getSubimage(44, 20, slimSkin?3:4, 12), slimSkin?1:0, 8, null); //Right Arm
				model.getGraphics().drawImage(img.getSubimage(oldSkin?44:36, oldSkin?20:52, slimSkin?3:4, 12), 12, 8, null); //Left Arm
				model.getGraphics().drawImage(img.getSubimage(4, 20, 4, 12), 4, 20, null); //Right Leg
				model.getGraphics().drawImage(img.getSubimage(oldSkin?4:20, oldSkin?20:52, 4, 12), 8, 20, null); //Left Leg
				model.getGraphics().drawImage(img.getSubimage(40, 8, 8, 8), 4, 0, null); //Head Overlay
				if (!oldSkin) {
					model.getGraphics().drawImage(img.getSubimage(20, 36, 8, 12), 4, 8, null); //Body Overlay
					model.getGraphics().drawImage(img.getSubimage(44, 36, 4, 12), 0, 8, null); //Right Arm Overlay
					model.getGraphics().drawImage(img.getSubimage(52, 52, 4, 12), 12, 8, null); //Left Arm Overlay
					model.getGraphics().drawImage(img.getSubimage(4, 36, 4, 12), 4, 20, null); //Right Leg Overlay
					model.getGraphics().drawImage(img.getSubimage(4, 52, 4, 12), 8, 20, null); //Left Leg Overlay
				}
				ImageIO.write(model, "png", modelF);
			} catch (Throwable t) {
				IAS.LOG.warn("Unable to make model of skin: " + name, t);
			}
		}
		if (!faceF.exists()) {
			faceF.getParentFile().mkdirs();
			try {
				ImageIO.write(ImageIO.read(modelF).getSubimage(4, 0, 8, 8), "png", faceF);
			} catch (Throwable t) {
				IAS.LOG.warn("Unable to make face from skin model: " + name, t);
			}
		}
	}
	
	/**
	 * Screen for async skin loading.
	 * @author VidTu
	 * @see SkinRenderer#loadAllAsync(MinecraftClient, boolean, Runnable)
	 */
	public static class SkinsLoadingScreen extends GuiScreen {
		private static final ResourceLocation PROGRESS = new ResourceLocation("ias", "textures/iaslogo.png");
		public float progress;
		public float smoothProgress;
		@Override
		public void drawScreen(int mouseX, int mouseY, float partialTicks) {
			if (smoothProgress < progress) smoothProgress += (progress - smoothProgress) / 16F;
		    drawRect(0, 0, width, height, 0xFF101010);
		    GlStateManager.color(1F, 1F, 1F, 1F);
		    mc.getTextureManager().bindTexture(PROGRESS);
		    drawModalRectWithCustomSizedTexture(width / 2 - 25, height / 2 - 25, 0F, 0F, 50, 50, 50, 50);
		    drawRect(30, height / 4 * 3, width - 30, height / 4 * 3 + 10, -1);
		    drawRect(31, height / 4 * 3 + 1, width - 31, height / 4 * 3 + 9, -16777216);
		    drawRect(31, height / 4 * 3 + 1, (int) (31 + (width - 61) * smoothProgress), height / 4 * 3 + 9, 0xFF00AA00);
			super.drawScreen(mouseX, mouseY, partialTicks);
		}
		
		@Override
		public void keyTyped(char typedChar, int keyCode) {}
	}
}
