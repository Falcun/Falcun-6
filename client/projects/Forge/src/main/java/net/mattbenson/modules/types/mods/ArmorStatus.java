package net.mattbenson.modules.types.mods;

import java.awt.Color;
import java.lang.annotation.ElementType;

import org.lwjgl.opengl.GL11;

import net.mattbenson.Falcun;
import net.mattbenson.config.ConfigValue;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.render.RenderEvent;
import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.hud.HUDElement;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class ArmorStatus extends Module {
	@ConfigValue.Boolean(name = "Item Name")
	private boolean armorstatusitemname = false;
	
	@ConfigValue.Boolean(name = "Item Equipped")
	private boolean armorstatusitemequipped = true;
	
	@ConfigValue.Boolean(name = "Preset Durability Colors")
	private boolean duraColors = true;
	
	@ConfigValue.Color(name = "Durability Color")
	private Color valueColor = Color.WHITE;
	
	@ConfigValue.Boolean(name = "Custom Font")
	private boolean customFont = true;
	
	@ConfigValue.List(name = "Direction", values = { "Vertical", "Horizontal"})
	private String armourstatusdirection = "Vertical";
	
	@ConfigValue.List(name = "Value Display", values = {"Percent", "Value Damage/Max", "Value Damage"})
	private String armorstatusdisplay = "Value Damage";
	
	@ConfigValue.List(name = "Value Left/Right", values = {"Left", "Right"})
	private String leftRight = "Left";
	
	private HUDElement hud;
	private int armorstatuswidth = 10;
	private int armorstatusheight = 10;
	
	public ArmorStatus() {
		super("Armor Status", ModuleCategory.MODS);
		
		hud = new HUDElement("status", armorstatuswidth, armorstatusheight) {
			@Override
			public void onRender() {
				render();
			}
		};
		
		hud.setX(0);
		hud.setY(200);
		
		addHUD(hud);
	}
	
	private EnumChatFormatting getColorThreshold(int percent) {
		if (percent > 75 && percent <= 100)
			return EnumChatFormatting.GREEN;
		else if (percent > 50 && percent <= 75)
			return EnumChatFormatting.YELLOW;
		else if (percent > 25 && percent <= 50)
			return EnumChatFormatting.RED;
		else if (percent >= 0 && percent <= 25)
			return EnumChatFormatting.DARK_RED;

		return EnumChatFormatting.WHITE;
	}

	public void render() {
		if (armourstatusdirection.contains("Horizontal")) {
			armorstatuswidth = 170;
			if (armorstatusdisplay.contains("Value Damage/Max")) {
				armorstatuswidth += 80;
				if (armorstatusitemequipped) {
					armorstatuswidth += 35;
				}
			}
			if (armorstatusdisplay.equals("Percent")) {
				armorstatuswidth += 20;
			}
			if (armorstatusitemequipped) {
				armorstatuswidth += 35;
			}
			armorstatusheight = 15;
		} else {
			armorstatuswidth = 50;
			armorstatusheight = 65;
			if (armorstatusitemequipped) {
				armorstatusheight += 20;
			}
			if (armorstatusitemname) {
				armorstatuswidth += 100;
			}
			if (armorstatusdisplay.contains("Value Damage/Max")) {
				armorstatuswidth += 20;
			}
		}

		hud.setHeight(armorstatusheight);
		hud.setWidth(armorstatuswidth);

		if (this.mc.gameSettings.showDebugInfo) {
			return;
		}
		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableAlpha();
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.disableLighting();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		RenderHelper.enableGUIStandardItemLighting();
		/*
		 * VERTICAL MODE
		 */
		int posX = hud.getX();
		int posY = hud.getY();
		
		int xdraw = armorstatusheight - 17;
		if (armourstatusdirection.contains("Vertical")) {
			RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
			ItemStack[] reversed = new ItemStack[4];
			for (int i = 0; i < mc.thePlayer.inventory.armorInventory.length; i++) {
				ItemStack itemStack = mc.thePlayer.inventory.armorInventory[i];
				if (itemStack != null) {
					reversed[3 - i] = itemStack;
				}
			}

			if (armorstatusitemequipped) {
				if (Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem() != null) {
					GlStateManager.disableLighting();
					GlStateManager.enableBlend();
					ItemStack itemStack = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem();
					int maxDamage = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem().getMaxDamage();
					int damage = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem().getItemDamage();
					int percent = maxDamage > 0 ? 100 - (damage * 100) / maxDamage : 1000;
					String toDisplay = maxDamage > 0 ? (armorstatusdisplay.equalsIgnoreCase("Percent") ? percent + "% "
							: armorstatusdisplay.equalsIgnoreCase("Value Damage/Max")
									? (maxDamage - damage) + "/" + maxDamage
									: "" + (maxDamage - damage))
							: "";
					String color = EnumChatFormatting.WHITE.toString();
					if (this.armorstatusitemname) {
						toDisplay += " " + EnumChatFormatting.WHITE.toString() + itemStack.getDisplayName();
					}
					if (duraColors) {
						color = getColorThreshold(percent).toString();
					} else {
						color = "";
					}
					int width = mc.fontRendererObj.getStringWidth(toDisplay);
					if (!customFont) {
						mc.fontRendererObj.drawStringWithShadow(color + toDisplay,
								(float) (posX + (leftRight.equalsIgnoreCase("Left")
										? maxDamage > 0 ? armorstatuswidth - width - 21 : armorstatuswidth - width - 21
										: maxDamage > 0 ? 20 : 16)),
								(float) (xdraw + posY + 5), valueColor.getRGB());
					} else {
						width = Fonts.RobotoHUD.getStringWidth(EnumChatFormatting.getTextWithoutFormattingCodes(toDisplay));
						Fonts.RobotoHUD.drawString(EnumChatFormatting.getTextWithoutFormattingCodes(toDisplay), (int) (posX - 3
								+ (leftRight.equalsIgnoreCase("Left")
										? maxDamage > 0 ? armorstatuswidth - width - 21 : armorstatuswidth - width - 21
										: maxDamage > 0 ? 20 : 16)),
								(int) (xdraw + posY + 1), valueColor.getRGB());
					}

					itemRenderer.renderItemAndEffectIntoGUI(itemStack,
							(int) ((leftRight.equalsIgnoreCase("Left") ? posX + armorstatuswidth - 20 : posX)),
							(int) (xdraw + posY + 0));
					
					itemRenderer.renderItemOverlayIntoGUI(mc.fontRendererObj, itemStack,
							(int) ((leftRight.equalsIgnoreCase("Left") ? posX + armorstatuswidth - 20 : posX)),
							(int) (xdraw + posY + 2), null);
					xdraw -= 17;
				}
			}

			for (ItemStack itemStack : mc.thePlayer.inventory.armorInventory) {

				if (itemStack != null) {
					GlStateManager.disableLighting();
					GlStateManager.enableBlend();
					int maxDamage = itemStack.getMaxDamage();
					int damage = itemStack.getItemDamage();
					int percent = maxDamage > 0 ? 100 - (damage * 100) / maxDamage : 1000;
					String toDisplay = armorstatusdisplay.equalsIgnoreCase("Percent") ? percent + "% "
							: armorstatusdisplay.equalsIgnoreCase("Value Damage/Max")
									? (maxDamage - damage) + "/" + maxDamage
									: "" + (maxDamage - damage);
					String color = EnumChatFormatting.WHITE.toString();
					if (this.armorstatusitemname) {
						toDisplay += " " + EnumChatFormatting.WHITE.toString() + itemStack.getDisplayName();
					}
					if (duraColors) {
						color = getColorThreshold(percent).toString();
					} else {
						color = "";
					}
					int width = mc.fontRendererObj.getStringWidth(toDisplay);
					if (!customFont) {
						mc.fontRendererObj.drawStringWithShadow(color + toDisplay,
								(float) (posX
										+ (leftRight.equalsIgnoreCase("Left") ? (armorstatuswidth) - width - 21 : 20)),
								(float) (xdraw + posY + 5), valueColor.getRGB());
					} else {
						width = Fonts.RobotoHUD.getStringWidth(EnumChatFormatting.getTextWithoutFormattingCodes(toDisplay));
						Fonts.RobotoHUD.drawString(EnumChatFormatting.getTextWithoutFormattingCodes(toDisplay),
								(int) (posX - 3
										+ (leftRight.equalsIgnoreCase("Left") ? (armorstatuswidth) - width - 21 : 20)),
								(int) (xdraw + posY + 1), valueColor.getRGB());
					}
					itemRenderer.renderItemAndEffectIntoGUI(itemStack,
							(int) ((leftRight.equalsIgnoreCase("Left") ? posX + armorstatuswidth - 20 : posX)),
							(int) (xdraw + posY + 0));

					itemRenderer.renderItemOverlayIntoGUI(mc.fontRendererObj, itemStack,
							(int) ((leftRight.equalsIgnoreCase("Left") ? posX + armorstatuswidth - 20 : posX)),
							(int) (xdraw + posY + 2), null);
					xdraw -= 17;
				}
			}

		}

		int ydraw = 0;

		/*
		 * HORIZONTAL MODE
		 */

		if (armourstatusdirection.contains("Horizontal")) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
			ItemStack[] reversed = new ItemStack[4];
			for (int i = 0; i < mc.thePlayer.inventory.armorInventory.length; i++) {
				ItemStack itemStack = mc.thePlayer.inventory.armorInventory[i];
				if (itemStack != null) {
					reversed[3 - i] = itemStack;
				}
			}

			for (ItemStack itemStack : reversed) {
				if (itemStack != null) {
					GlStateManager.disableLighting();
					GlStateManager.enableBlend();
					int maxDamage = itemStack.getMaxDamage();
					int damage = itemStack.getItemDamage();
					int percent = maxDamage > 0 ? 100 - (damage * 100) / maxDamage : 1000;
					String toDraw = armorstatusdisplay.equalsIgnoreCase("Percent") ? percent + "%"
							: armorstatusdisplay.equalsIgnoreCase("Value Damage/Max")
									? "" + (maxDamage - damage) + "/" + maxDamage
									: "" + (maxDamage - damage);
					String color = EnumChatFormatting.WHITE.toString();
					int width = mc.fontRendererObj.getStringWidth(toDraw);
					if (duraColors) {
						color = getColorThreshold(percent).toString();
					} else {
						color = "";
					}
					if (this.customFont) {
						width = Fonts.RobotoHUD.getStringWidth(EnumChatFormatting.getTextWithoutFormattingCodes(toDraw));
						Fonts.RobotoHUD.drawString(EnumChatFormatting.getTextWithoutFormattingCodes(toDraw), (int) (posX + ydraw + 16), (int) (posY + 1),
								valueColor.getRGB());
					} else {
						mc.fontRendererObj.drawStringWithShadow(color + toDraw, (float) (posX + ydraw + 18),
								(float) (posY + 5), valueColor.getRGB());
					}
					itemRenderer.renderItemAndEffectIntoGUI(itemStack, (int) (posX + ydraw + 0), (int) (posY + 0));
					itemRenderer.renderItemOverlayIntoGUI(mc.fontRendererObj, itemStack, (int) (posX + ydraw + 0),
							(int) (posY + 0), "");
					ydraw += width + 20;
				}
			}

			if (armorstatusitemequipped) {
				if (Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem() != null) {
					GlStateManager.disableLighting();
					GlStateManager.enableBlend();
					ItemStack itemStack = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem();
					int maxDamage = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem().getMaxDamage();
					int damage = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem().getItemDamage();
					int percent = maxDamage > 0 ? 100 - (damage * 100) / maxDamage : 1000;
					String toDraw = maxDamage > 0 ? (armorstatusdisplay.equalsIgnoreCase("Percent") ? percent + "% "
							: armorstatusdisplay.equalsIgnoreCase("Value Damage/Max")
									? (maxDamage - damage) + "/" + maxDamage
									: "" + (maxDamage - damage))
							: "";
					String color = EnumChatFormatting.WHITE.toString();
					int width = mc.fontRendererObj.getStringWidth(toDraw);
					if (duraColors) {
						color = getColorThreshold(percent).toString();
					} else {
						color = "";
					}
					if (this.customFont) {
						width = Fonts.RobotoHUD.getStringWidth(EnumChatFormatting.getTextWithoutFormattingCodes(toDraw));
						Fonts.RobotoHUD.drawString(EnumChatFormatting.getTextWithoutFormattingCodes(toDraw), (int) (posX + ydraw + 15), (int) (posY + 1),
								valueColor.getRGB());
					} else {
						mc.fontRendererObj.drawStringWithShadow(color + toDraw, (float) (posX + ydraw + 18),
								(float) (posY + 5), valueColor.getRGB());
					}
					itemRenderer.renderItemAndEffectIntoGUI(itemStack, (int) (posX + ydraw), (int) posY);
					itemRenderer.renderItemOverlayIntoGUI(mc.fontRendererObj, itemStack, (int) (posX + ydraw + 0),
							(int) (posY + 0), "");

					ydraw += width + 20;
				}
			}
		}
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GL11.glPopMatrix();
	}
}
