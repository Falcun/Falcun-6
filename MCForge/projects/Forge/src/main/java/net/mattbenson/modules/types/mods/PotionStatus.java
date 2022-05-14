package net.mattbenson.modules.types.mods;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import net.mattbenson.config.ConfigValue;
import net.mattbenson.gui.hud.HUDElement;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class PotionStatus extends Module {
	@ConfigValue.List(name = "Design", values = { "Boxes", "Icon", "Slim" })
	private static String potionstatusdesign = "Icon";

	@ConfigValue.Boolean(name = "Preset Potion Color")
	private boolean presetColor = true;

	@ConfigValue.Color(name = "Potion Color")
	private Color potionColor = Color.WHITE;

	private Map<PotionEffect, Integer> potionMaxDurationMap = new HashMap<PotionEffect, Integer>();
	protected float zLevel = -150.0F;
	private ScaledResolution scaledResolution;
	public int xOffset = 0;
	public int yOffset = 10;
	public int yBase;

	private HUDElement hud;
	private int width = 10;
	private int height = 10;

	public PotionStatus() {
		super("Potion Status", ModuleCategory.MODS);

		hud = new HUDElement("stopwatch", width, height) {
			@Override
			public void onRender() {
				render();
			}
		};

		hud.setX(0);
		hud.setY(175);

		addHUD(hud);
	}
	
	public void render() {
		if (this.mc.gameSettings.showDebugInfo) {
			return;
		}
		Collection<?> activeEffects = mc.thePlayer.getActivePotionEffects();

		if (!activeEffects.isEmpty()) {

			GL11.glPushMatrix();
			yBase = 0;
			for (Iterator<?> iteratorPotionEffect = activeEffects.iterator(); iteratorPotionEffect.hasNext();) {
				PotionEffect potionEffect = (PotionEffect) iteratorPotionEffect.next();
				if (!potionMaxDurationMap.containsKey(potionEffect)
						|| potionMaxDurationMap.get(potionEffect).intValue() < potionEffect.getDuration())
					potionMaxDurationMap.put(potionEffect, new Integer(potionEffect.getDuration()));

				Potion potion = Potion.potionTypes[potionEffect.getPotionID()];
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
				boolean enableBackground = true;
				int xBase = 0;
				String potionName = "";

				potionName = StatCollector.translateToLocal(potion.getName());

				if (potionEffect.getAmplifier() == 1) {
					potionName = potionName + " II";
				} else if (potionEffect.getAmplifier() == 2) {
					potionName = potionName + " III";
				} else if (potionEffect.getAmplifier() == 3) {
					potionName = potionName + " IV";
				} else if (potionEffect.getAmplifier() == 4) {
					potionName = potionName + " V";
				} else if (potionEffect.getAmplifier() == 5) {
					potionName = potionName + " VI";
				} else if (potionEffect.getAmplifier() == 6) {
					potionName = potionName + " VII";
				} else if (potionEffect.getAmplifier() == 7) {
					potionName = potionName + " VIII";
				} else if (potionEffect.getAmplifier() == 8) {
					potionName = potionName + " IX";
				} else if (potionEffect.getAmplifier() == 9) {
					potionName = potionName + " X";
				} else if (potionEffect.getAmplifier() > 9) {
					potionName = potionName + " " + (potionEffect.getAmplifier() + 1);
				}

				int posX = hud.getX();
				int posY = hud.getY();

				String effectDuration = Potion.getDurationString(potionEffect);
				if (potionstatusdesign.contains("Boxes")) {
					drawTexturedModalRect((int) posX + xBase, (int) posY + yBase, 0, 166, 140, 32, zLevel);

					if (potion.hasStatusIcon()) {
						int potionStatusIcon = potion.getStatusIconIndex();
						drawTexturedModalRect((int) posX + xBase + 5, (int) posY + yBase + (enableBackground ? 7 : 0),
								0 + potionStatusIcon % 8 * 18, 166 + 32 + potionStatusIcon / 8 * 18, 18, 18, zLevel);
					}

					mc.fontRendererObj.drawStringWithShadow(potionName, (float) posX + xBase + 26,
							(float) posY + yBase + 7, presetColor ? potion.getLiquidColor() : potionColor.getRGB());

					mc.fontRendererObj.drawStringWithShadow(effectDuration, (float) posX + xBase + 26,
							(float) posY + yBase + 17, 0xffffff);
					yBase += 32;

				} else if (potionstatusdesign.contains("Slim")) {
					mc.fontRendererObj.drawStringWithShadow(potionName + " - " + effectDuration, (float) posX,
							(float) posY + yBase + 7, presetColor ? potion.getLiquidColor() : potionColor.getRGB());

					yBase += 10;
				} else if (potionstatusdesign.contains("Icon")) {
					if (potion.hasStatusIcon()) {
						int potionStatusIcon = potion.getStatusIconIndex();
						drawTexturedModalRect((int) posX + xBase, (int) posY + yBase - 2, 0 + potionStatusIcon % 8 * 18,
								166 + 32 + potionStatusIcon / 8 * 18, 18, 18, zLevel);
					}
					mc.fontRendererObj.drawStringWithShadow(potionName, (float) posX + xBase + 21, (float) posY + yBase,
							presetColor ? potion.getLiquidColor() : potionColor.getRGB());
					mc.fontRendererObj.drawStringWithShadow(effectDuration, (float) posX + xBase + 21,
							(float) posY + yBase + 9, presetColor ? potion.getLiquidColor() : potionColor.getRGB());

					yBase += 20;
				}
			}

			GL11.glScaled(1, 1, 1);
			GL11.glColor3f(1, 1, 1);
			GL11.glPopMatrix();
		}

		width = 10;
		if (activeEffects.isEmpty()) {
			yBase = 10;
		}
		if (PotionStatus.potionstatusdesign.contains("Slim")) {
			width += 110;
			yBase += 5;
		} else {
			width += 120;
		}

		hud.setHeight(yBase);
		hud.setWidth(width);

		List<PotionEffect> toRemove = new LinkedList<PotionEffect>();

		for (PotionEffect pe : potionMaxDurationMap.keySet())
			if (!activeEffects.contains(pe))
				toRemove.add(pe);

		for (PotionEffect pe : toRemove)
			potionMaxDurationMap.remove(pe);
	}

	public void drawTexturedModalRect(int x, int y, int u, int v, int width, int height, float zLevel) {
		float var7 = 0.00390625F;
		float var8 = 0.00390625F;
		WorldRenderer tessellator = Tessellator.getInstance().getWorldRenderer();
		tessellator.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		tessellator.pos((x + 0), (y + height), zLevel).tex(((u + 0) * var7), ((v + height) * var8)).endVertex();
		tessellator.pos((x + width), (y + height), zLevel).tex(((u + width) * var7), ((v + height) * var8)).endVertex();
		tessellator.pos((x + width), (y + 0), zLevel).tex(((u + width) * var7), ((v + 0) * var8)).endVertex();
		tessellator.pos((x + 0), (y + 0), zLevel).tex(((u + 0) * var7), ((v + 0) * var8)).endVertex();
		Tessellator.getInstance().draw();
	}

	private int getX(int width) {
		return xOffset;
	}
}
