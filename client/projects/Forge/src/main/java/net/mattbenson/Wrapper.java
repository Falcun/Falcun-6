package net.mattbenson;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import org.lwjgl.opengl.GL11;

import mapwriter.Mw;
import mapwriter.map.Marker;
import mapwriter.util.Utils;
import net.mattbenson.events.Event;
import net.mattbenson.events.types.MinecraftInitEvent;
import net.mattbenson.events.types.render.RenderEvent;
import net.mattbenson.events.types.render.RenderType;
import net.mattbenson.events.types.render.gui.GuiActionPerformedEvent;
import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.menu.pages.FPSPage;
import net.mattbenson.modules.ModuleManager;
import net.mattbenson.modules.types.factions.CaneHelper;
import net.mattbenson.modules.types.factions.Patchcrumbs;
import net.mattbenson.modules.types.factions.Patchcrumbs.PatchCrumb;
import net.mattbenson.modules.types.fpssettings.FPSSettings;
import net.mattbenson.modules.types.fpssettings.cruches.RenderItemHook;
import net.mattbenson.modules.types.general.Settings;
import net.mattbenson.modules.types.general.ToggleSneak;
import net.mattbenson.modules.types.mods.Hitbox;
import net.mattbenson.modules.types.mods.OldAnimations;
import net.mattbenson.modules.types.other.BossBar;
import net.mattbenson.modules.types.other.Chat;
import net.mattbenson.modules.types.other.ConsoleClient;
import net.mattbenson.modules.types.other.Hits18;
import net.mattbenson.modules.types.other.InventoryAllignment;
import net.mattbenson.modules.types.other.MouseBindFix;
import net.mattbenson.modules.types.other.MouseDelay;
import net.mattbenson.modules.types.other.PerspectiveMod;
import net.mattbenson.modules.types.other.PortalFix;
import net.mattbenson.modules.types.other.SwordFOV;
import net.mattbenson.modules.types.other.VoidFlicker;
import net.mattbenson.modules.types.other.Zoom;
import net.mattbenson.modules.types.render.ColoredRedstone;
import net.mattbenson.modules.types.render.Cooldowns;
import net.mattbenson.modules.types.render.ScoreboardHUD;
import net.mattbenson.network.common.GroupData;
import net.mattbenson.network.common.GroupSetting;
import net.mattbenson.network.common.UserInfo;
import net.mattbenson.network.common.WaypointMarker;
import net.mattbenson.network.network.packets.groups.GroupList;
import net.mattbenson.network.network.packets.misc.UserList;
import net.mattbenson.network.network.packets.play.Patchcrumb;
import net.mattbenson.utils.DrawUtils;
import net.mattbenson.utils.NetworkUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.command.ICommand;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.EnumParticleTypes;

public class Wrapper {

	private static Wrapper instance;
	private static Minecraft minecraft;

	public void init() {
		instance = this;
	}
	
	public void FalcunInit() {
		Falcun falcun = new Falcun();
	}
	
	
	public static Wrapper getInstance() {
		if(instance == null) {
	        Wrapper wrapper = new Wrapper();
		   	wrapper.init();
		}
		return instance;
	}
	
	public ModuleManager getModuleManager() {
		return Falcun.getInstance().moduleManager;
	}

	public boolean isRedstoneColor() {
		if (Falcun.getInstance().moduleManager.getModule(ColoredRedstone.class).isEnabled()) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isPortalFix() {
		if (Falcun.getInstance().moduleManager.getModule(PortalFix.class).isEnabled()) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isMouseBindFix() {
		if (Falcun.getInstance().moduleManager.getModule(MouseBindFix.class).isEnabled()) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isOldAnimations() {
		if (Falcun.getInstance().moduleManager.getModule(OldAnimations.class).isEnabled()) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isHitbox() {
		if (Falcun.getInstance().moduleManager.getModule(Hitbox.class).isEnabled()) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isCaneHelper() {
		if (Falcun.getInstance().moduleManager.getModule(CaneHelper.class).isEnabled()) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isMouseDelay() {
		if (Falcun.getInstance().moduleManager.getModule(MouseDelay.class).isEnabled()) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean is18Hits() {
		if (Falcun.getInstance().moduleManager.getModule(Hits18.class).isEnabled()) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isInventoryAlign() {
		if (Falcun.getInstance().moduleManager.getModule(InventoryAllignment.class).isEnabled()) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isOldAnimationsDisableHealthFlash() {
		if (Falcun.getInstance().moduleManager.getModule(OldAnimations.class).DISABLE_HEALTH_FLASH) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isHitboxEnabled() {
		return (Falcun.getInstance().moduleManager.getModule(Hitbox.class).enabled);
	}
	
	public String getHitBoxMode() {
		return Falcun.getInstance().moduleManager.getModule(Hitbox.class).hitboxMode;
	}
	
	public int getHitBoxWidth() {
		return Falcun.getInstance().moduleManager.getModule(Hitbox.class).lineWidth;
	}
	
	public Color getHitBoxColor() {
		return Falcun.getInstance().moduleManager.getModule(Hitbox.class).lineColor;
	}
	
	public boolean isOldAnimationsOldBlockHitting() {
		if (Falcun.getInstance().moduleManager.getModule(OldAnimations.class).OLD_BLOCKING_HITTING) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isOldAnimationsOldBow() {
		if (Falcun.getInstance().moduleManager.getModule(OldAnimations.class).OLD_BOW) {
			return true;
		} else {
			return false;
		}
	}
	
	
	public boolean isOldAnimationsOldEat() {
		if (Falcun.getInstance().moduleManager.getModule(OldAnimations.class).OLD_EAT_USE_ANIMATION) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isOldAnimationsOldHoldItem() {
		if (Falcun.getInstance().moduleManager.getModule(OldAnimations.class).OLD_ITEM_HELD) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isOldAnimationsOldBlocking() {
		if (Falcun.getInstance().moduleManager.getModule(OldAnimations.class).OLD_BLOCKING) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isRemoveTNT() {
		if (Falcun.getInstance().moduleManager.getModule(FPSSettings.class).REMOVE_TNT) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isLiquidVision() {
		if (Falcun.getInstance().moduleManager.getModule(FPSSettings.class).noLagLiquidVision) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isSkinLoading() {
		if (Falcun.getInstance().moduleManager.getModule(FPSSettings.class).BETTER_SKIN_LOADING) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isItemSearching() {
		if (Falcun.getInstance().moduleManager.getModule(FPSSettings.class).ITEM_SEARCHING) {
			return true;
		} else {
			return false;
		}
	}
	
	public int getChunkLimiter() {
		return Falcun.getInstance().moduleManager.getModule(FPSSettings.class).CHUNK_UPDATE_LIMITER;
	}
	
	public int getEntityDistance() {
		return Falcun.getInstance().moduleManager.getModule(FPSSettings.class).noLagEntityDistance;
	}
	
	public int getBlockDistance() {
		return Falcun.getInstance().moduleManager.getModule(FPSSettings.class).noLagBlockDistance;
	}
	
	public FPSSettings getFPSSettings() {
		return Falcun.getInstance().moduleManager.getModule(FPSSettings.class);
	}
	
	public boolean isVoidFlicker() {
		if (Falcun.getInstance().moduleManager.getModule(VoidFlicker.class).isEnabled()) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isNoCane() {
		if (Falcun.getInstance().moduleManager.getModule(FPSSettings.class).noLagCane) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isClearWater() {
		if (Falcun.getInstance().moduleManager.getModule(FPSSettings.class).noLagClearWater) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isNoHolograms() {
		if (Falcun.getInstance().moduleManager.getModule(FPSSettings.class).noLagHolograms) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isNoFoliage() {
		if (Falcun.getInstance().moduleManager.getModule(FPSSettings.class).noFoliage) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isNoMobs() {
		if (Falcun.getInstance().moduleManager.getModule(FPSSettings.class).noMobs) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isNoHitRed() {
		if (Falcun.getInstance().moduleManager.getModule(FPSSettings.class).noHitRed) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isRemoveTint() {
		if (Falcun.getInstance().moduleManager.getModule(FPSSettings.class).REMOVE_ITEM_GLINT) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isStaticDrop() {
		if (Falcun.getInstance().moduleManager.getModule(FPSSettings.class).STATIC_DROPS) {
			return true;
		} else {
			return false;
		}
	}
	
	public PerspectiveMod perspectiveMod() {
		return Falcun.getInstance().moduleManager.getModule(PerspectiveMod.class);
	}
	
	public Zoom zoom() {
		return Falcun.getInstance().moduleManager.getModule(Zoom.class);
	}
	
	public SwordFOV swordFOV() {
		return Falcun.getInstance().moduleManager.getModule(SwordFOV.class);
	}
	
	public boolean isF5Nametags() {
		if (Falcun.getInstance().moduleManager.getModule(Settings.class).F5Nametags) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isInventorySearch() {
		if (Falcun.getInstance().moduleManager.getModule(Settings.class).inventorySearch) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isCullingFix() {
		return Falcun.getInstance().moduleManager.getModule(FPSSettings.class).CULLING_FIX;
	}
	
	public boolean isStaticParticleColor() {
		if (Falcun.getInstance().moduleManager.getModule(FPSSettings.class).STATIC_PARTICLE_COLOR) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isBossBar() {
		if (getFalcun().moduleManager.getModule(BossBar.class).isEnabled()) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isToggleSneak() {
		if (getFalcun().moduleManager.getModule(ToggleSneak.class).isEnabled()) {
			return true;
		} else {
			return false;
		}
	}
	
	public Color getRedstoneColor() {
		return getFalcun().moduleManager.getModule(ColoredRedstone.class).redstoneColor;
	}

	public Color getInventorySearchColor() {
		return getFalcun().moduleManager.getModule(Settings.class).inventorySearchColor;
	}
	
	public static void attemptSwing() {
		Falcun.getInstance().moduleManager.getModule(OldAnimations.class).attemptSwing();
	}
	

	public boolean isRemoveLightCalculations() {
		if(getFalcun().moduleManager.getModule(FPSSettings.class).LIGHT_CALCULATION_REMOVAL) {
			return true;
		} else {
			return false;
		}
	}
	
	public static Falcun getFalcun() {
		return Falcun.getInstance();
	}
	
	public String getVersion() {
		return getFalcun().VERSION;
	}
	
	public boolean isRemoveTextShadows() {
		if(Falcun.getInstance().moduleManager.getModule(FPSSettings.class).REMOVE_TEXT_SHADOWS) {
			return true;
		} else {
			return false;
		}
	}

	public ResourceLocation getLocationCape(AbstractClientPlayer player) {
		return getFalcun().capeManager.getLocationCape(player);
	}

	
	public ResourceLocation getBandana(AbstractClientPlayer player) {
		return getFalcun().bandanaManager.getLocationBandana(player);
	}

	public static ICommand getCommandMap(String s) {
		return getFalcun().commandHandler.commandMap.get(s);
	}
	
	public boolean isCommandMap(EntityPlayerSP thePlayer, String[] astring, ICommand icommand, String message) {
		if (getFalcun().commandHandler.tryExecute(thePlayer, astring, icommand, message)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean RenderEvent(RenderType renderType, float partialTicks) {
		return Falcun.EVENT_BUS.post(new RenderEvent(RenderType.INGAME_OVERLAY, partialTicks));
	}
	

	public boolean isSortTab() {
		if(getFalcun().moduleManager.getModule(Settings.class).sortTab) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isShowLogo() {
		if(getFalcun().moduleManager.getModule(Settings.class).showFalcunLogoOnTab) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isRemoveSpawner() {
		if(getFalcun().moduleManager.getModule(FPSSettings.class).REMOVE_ENTITY_SPAWNER) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isPistonExtention() {
		if(getFalcun().moduleManager.getModule(FPSSettings.class).REMOVE_PISTON_EXTENTION) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isFog() {
		if(getFalcun().moduleManager.getModule(FPSSettings.class).noFog) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isBetterChests() {
		if(getFalcun().moduleManager.getModule(FPSSettings.class).noBetterChests) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isFastWorldLoading() {
		if(getFalcun().moduleManager.getModule(FPSSettings.class).FAST_WORLD_LOADING) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isTNTExpand() {
		if(getFalcun().moduleManager.getModule(FPSSettings.class).noLagTNTExpand) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isTNTFlash() {
		if(getFalcun().moduleManager.getModule(FPSSettings.class).noLagTNTFlash) {
			return true;
		} else {
			return false;
		}
	}
	
	public void GuiActionPerformedEvent(GuiScreen guiScreen, GuiButton guibutton) {
		Falcun.EVENT_BUS.post(new GuiActionPerformedEvent(guiScreen, guibutton));
		
	}

	public boolean MinecraftInitEvent(Minecraft minecraft2) {
		return Falcun.EVENT_BUS.post(new MinecraftInitEvent(minecraft2));
	}

	public boolean post(Event event) {
		return Falcun.EVENT_BUS.post(event);
	}
	
	public void renderModelEnd() {
		Falcun.getInstance().moduleManager.getModule(FPSSettings.class).renderItemHook.renderModelEnd();
	}

	public RenderItemHook getRenderItemHook() {
		return Wrapper.getInstance().getModuleManager().getModule(FPSSettings.class).renderItemHook;
	}

	public String langFiles(String translateKey) {
		return Falcun.getInstance().languageUtils.get(translateKey);
	}

	public boolean isInPerspectiveMode() {
		return isPerspectiveModEnable() && Wrapper.getInstance().getModuleManager().getModule(PerspectiveMod.class).perspectiveToggled;
	}
	
	public void overrideMouse() {
		Wrapper.getInstance().getModuleManager().getModule(PerspectiveMod.class).overrideMouse();
	}

	public boolean isPerspectiveModEnable() {
		if(Wrapper.getFalcun() == null || Wrapper.getInstance().getModuleManager() == null) {
			return false;
		}
		return Wrapper.getInstance().getModuleManager().getModule(PerspectiveMod.class).enabled || Wrapper.getInstance().getModuleManager().getModule(PerspectiveMod.class).isEmoting || Wrapper.getInstance().getModuleManager().getModule(PerspectiveMod.class).isCrating;
	}
	
	public float getCameraYaw() {
		return Wrapper.getInstance().getModuleManager().getModule(PerspectiveMod.class).getCameraYaw();
	}

	public float getCameraPitch() {
		return Wrapper.getInstance().getModuleManager().getModule(PerspectiveMod.class).getCameraPitch();
	}
	
	public boolean isPerspectiveOwnName() {
		return Wrapper.getInstance().getModuleManager().getModule(PerspectiveMod.class).ownName;
	}

	public void drawRoundedRect(int x0, int y0, int x1, int y1, float radius, int color)
	{
		DrawUtils.drawRoundedRect(x0, y0, x1, y1, radius, color);
	}

	public void drawCenteredString(String text, float x, float y, int color) {
		Fonts.RalewayExtraBold.drawString(text, x - getStringWidth(text) / 2 , y, color);
	}
	
	public float getStringWidth(String text) {
		return Fonts.RalewayExtraBold.getStringWidth(text);
	}
	
	public boolean getOptionToggleSprint() {
		return Wrapper.getInstance().getModuleManager().getModule(ToggleSneak.class).optionToggleSprint;
	}

	public boolean getOptionFlyboost() {
		return Wrapper.getInstance().getModuleManager().getModule(ToggleSneak.class).optionEnableFlyBoost;
	}

	public boolean getOptionDoubleTap() {
		return Wrapper.getInstance().getModuleManager().getModule(ToggleSneak.class).optionDoubleTap;
	}
	
	public boolean wasSprintDisabled() {
		return Wrapper.getInstance().getModuleManager().getModule(ToggleSneak.class).wasSprintDisabled;
	}
	
	public void setSprintDisabled(boolean wasSprintDisabled) {
		Wrapper.getInstance().getModuleManager().getModule(ToggleSneak.class).wasSprintDisabled = wasSprintDisabled;
	}

	public void updateSprint(boolean newValue, boolean doubleTapped) {
		Wrapper.getInstance().getModuleManager().getModule(ToggleSneak.class).UpdateSprint(newValue, doubleTapped);
	}

	public boolean getSprintState() {
		return Wrapper.getInstance().getModuleManager().getModule(ToggleSneak.class).sprint;
	}

	public boolean getSprintHeldAndReleased() {
		return Wrapper.getInstance().getModuleManager().getModule(ToggleSneak.class).sprintHeldAndReleased;
	}

	public boolean getSprintDoubleTapped() {
		return Wrapper.getInstance().getModuleManager().getModule(ToggleSneak.class).sprintDoubleTapped;
	}

	public double getFlyBoostSpeedVertical() {
		return Wrapper.getInstance().getModuleManager().getModule(ToggleSneak.class).enabled && Wrapper.getInstance().getModuleManager().getModule(ToggleSneak.class).optionEnableFlyBoost ? Wrapper.getInstance().getModuleManager().getModule(ToggleSneak.class).flyboostspeedVertical : 1;
	}
	
	public double getFlyBoostSpeedHorizontal() {
		return Wrapper.getInstance().getModuleManager().getModule(ToggleSneak.class).enabled ? Wrapper.getInstance().getModuleManager().getModule(ToggleSneak.class).flyboostspeedHorizontal : 1;
	}
	
	public void updateMovement(MovementInputFromOptions options, EntityPlayerSP thisPlayer) {
		ToggleSneak.update(getMinecraft(), options, thisPlayer);
	}
	

	public boolean getOptionToggleFlyboost() {
		return (isToggleSneak() && Wrapper.getInstance().getModuleManager().getModule(ToggleSneak.class).optionEnableFlyBoost);
	}
	
	public static void drawImage(ResourceLocation image, int x, int y, int width, int height) {
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		Minecraft.getMinecraft().getTextureManager().bindTexture(image);
		
	
		//GL11.glColor4f(1.0F, 0.0F, 0.0F, 0.5F); // red, 50% alpha
		
	
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, width, height, width, height);
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
	}
	
	public static void drawImage(ResourceLocation image, int x, int y, int width, int height, Color color, int Opacity) {
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		Minecraft.getMinecraft().getTextureManager().bindTexture(image);
		
		float opa = 1.0F;
		if (Opacity == 100) {
			opa = 1.0F;
		} else if (Opacity == 90) {
			opa = 0.9F;
		} else if (Opacity == 80) {
			opa = 0.8F;
		} else if (Opacity == 70) {
			opa = 0.7F;
		} else if (Opacity == 60) {
			opa = 0.6F;
		} else if (Opacity == 50) {
			opa = 0.5F;
		} else if (Opacity == 40) {
			opa = 0.4F;
		} else if (Opacity == 30) {
			opa = 0.3F;
		} else if (Opacity == 20) {
			opa = 0.2F;
		} else if (Opacity == 10) {
			opa = 0.1F;
		}
		GL11.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), opa); // red, 50% alpha
		
	
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, width, height, width, height);
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
	}
	
	public static void setColor(Color c) {
		GL11.glColor4f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
	}
	
	public boolean isParticleCulling() {
		return (Wrapper.getInstance().getModuleManager().getModule(FPSSettings.class).PARTICLE_CULLING);
	}
	
	public boolean isSwordOnly() {
		return Wrapper.getInstance().getModuleManager().getModule(SwordFOV.class).fovSwordOnly;
	}
	
	public boolean isHandZoom() {
		return Wrapper.getInstance().getModuleManager().getModule(SwordFOV.class).enabled;
	}
	
	public double getHandZoom() {
		return Wrapper.getInstance().getModuleManager().getModule(SwordFOV.class).handFOVScale;
	}
	
	public boolean isSmoothZoom() {
		if(!Wrapper.getInstance().getModuleManager().getModule(Zoom.class).enabled) return true;
		return Wrapper.getInstance().getModuleManager().getModule(Zoom.class).smoothZoom;
	}

	public boolean isScrollZoom() {
		return Wrapper.getInstance().getModuleManager().getModule(Zoom.class).enabled && Wrapper.getInstance().getModuleManager().getModule(Zoom.class).scrollZoom;
	}

	public float getScrollZoom() {
		return Wrapper.getInstance().getModuleManager().getModule(Zoom.class).scrollAmount;
	}

	public void setScrollZoom(float amount) {
		Wrapper.getInstance().getModuleManager().getModule(Zoom.class).scrollAmount = amount;
	}
	
	public static Minecraft getMinecraft() {
		return minecraft;
	}

	public void drawString(String text, float x, float y, int color) {
		Fonts.RalewayExtraBold.drawString(text, x, y, color);
	}
	
	public static void addChat(String msg) {
		if(Minecraft.getMinecraft() == null || Minecraft.getMinecraft().ingameGUI == null) {
			return;
		}
		
		ChatComponentText cpt = new ChatComponentText(
				EnumChatFormatting.BLUE + "Falcun" + EnumChatFormatting.GOLD + " > " + EnumChatFormatting.YELLOW + msg);
		Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(cpt);
	}
	
	public List<EnumParticleTypes> getParticles() {
		List<EnumParticleTypes> list = new ArrayList<>();
		
		if(Wrapper.getFalcun() == null || Wrapper.getInstance().getModuleManager() == null) {
			return list;
		}
		
		List<EnumParticleTypes> newList = FPSPage.PARTICLES;
		
		if(newList == null) {
			return list;
		}
		
		return newList;
	}
	
	public boolean isChatBackground() {
		return (Wrapper.getInstance().getModuleManager().getModule(FPSSettings.class).REMOVE_CHAT_BACKGROUND);
	}
	
	public boolean isInfiniteChat() {
		return (Wrapper.getInstance().getModuleManager().getModule(Chat.class).infiniteChat);
	}
	
	public void handleCustomFont(String message, int i2, int j2, int l, int l1) {
		Wrapper.getInstance().getModuleManager().getModule(Chat.class).handleChatFont(message,i2,j2,l,l1);
	}
	
	public static boolean usesFalcun(UUID id) {
		return UserList.usesFalcun(id);
	}
	
	public static String consoleServerIP() {
		return Wrapper.getInstance().getModuleManager().getModule(ConsoleClient.class).serverIP;
	}

	public boolean isChunkLimiter() {
		return true;
	}
	
	public boolean isPatchcrumb() {
		return Wrapper.getInstance().getModuleManager().getModule(Patchcrumbs.class).enabled;
	}

	public boolean getPatchcrumbsMap() {
		return Wrapper.getInstance().getModuleManager().getModule(Patchcrumbs.class).minimap;
	}
	
	public int getPatchcrumbX() {
		return Wrapper.getInstance().getModuleManager().getModule(Patchcrumbs.class).currentCrumb.posX;
	}

	public int getPatchcrumbZ() {
		return Wrapper.getInstance().getModuleManager().getModule(Patchcrumbs.class).currentCrumb.posZ;
	}
	
	public float getPatchcrumbsExpandRadius() {
		return Wrapper.getInstance().getModuleManager().getModule(Patchcrumbs.class).distance;
	}

	public PatchCrumb getPatchcrumbsCrumb() {
		return Wrapper.getInstance().getModuleManager().getModule(Patchcrumbs.class).currentCrumb;
	}
	
	public String getPartnerContent() {
		return getFalcun().partnerContent;
	}
	
	public boolean getEnderPearlCooldown() {
		return Wrapper.getInstance().getModuleManager().getModule(Cooldowns.class).ep;
	}
	
	public int getEnderPearlCooldownTime() {
		return Wrapper.getInstance().getModuleManager().getModule(Cooldowns.class).enderpearlTime;
	}
	
	public boolean getCooldown() {
		return 	Wrapper.getFalcun().moduleManager.getModule(Cooldowns.class).enabled;
	}

	public Color getHighlightColor(UUID user) {
		
		if(Minecraft.getMinecraft().thePlayer.getUniqueID().equals(user)) {
			return null;
		}
		
			for(GroupData group : GroupList.getGroups()) {
					for(UserInfo usr : group.getUsers()) {
						if(usr.getUUID().equals(user)) {
							return new Color(group.getColor(), true);
						}
					}
			}
		
		return null;
	}
	

}