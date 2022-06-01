package net.mattbenson.modules.types.factions;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.mattbenson.chat.ChatUtils;
import net.mattbenson.config.ConfigValue;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.entity.PlayerInteractEvent;
import net.mattbenson.events.types.entity.PlayerInteractEvent.Action;
import net.mattbenson.events.types.input.MouseDownEvent;
import net.mattbenson.gui.hud.HUDElement;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.mattbenson.utils.DrawUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.block.state.IBlockState;

public class TickCounter extends Module {
	@ConfigValue.Boolean(name = "Fill")
	private boolean turbosettingsfill = true;
	
	@ConfigValue.Boolean(name = "Redstone Ticks")
	private boolean redstoneTicks = false;
	
	@ConfigValue.Boolean(name = "Game Ticks")
	private boolean gameTicks = false;
	
	@ConfigValue.Boolean(name = "Seconds")
	private boolean seconds = false;
	
	@ConfigValue.Color(name = "Type Color")
	private Color tColor = Color.WHITE;
	
	@ConfigValue.Color(name = "Value Color")
	private Color vColor = Color.WHITE;
	
	@ConfigValue.Boolean(name = "Static Chroma")
	private boolean isUsingStaticChroma = false;
	
	@ConfigValue.Boolean(name = "Wave Chroma")
	private boolean isUsingWaveChroma = false;
	
	private double ticks;
	
	private HUDElement hud;
	private int width = 30;
	private int height = 30;
	
	public TickCounter() {
		super("Tick Counter", ModuleCategory.FACTIONS);
		
		hud = new HUDElement("counter", width, height) {
			@Override
			public void onRender() {
				render();
			}
		};
		
		hud.setX(1);
		hud.setY(60);
		
		addHUD(hud);
	}
	
	@Override
	public void onEnable() {
		ticks = 0;
	}
	
	@Override
	public void onDisable() {
		ticks = 0;
	}
	
	@SubscribeEvent
	public void onClick(PlayerInteractEvent event) {
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			IBlockState state = event.getWorld().getBlockState(event.getPos());
			Block block = state.getBlock();
			int meta = block.getMetaFromState(state);
			if(block instanceof BlockRedstoneRepeater) {
				event.setCancelled(true);
				if(meta == 0 || meta == 1 || meta == 2 || meta == 3) {
					ticks += 1;
				}
				if(meta == 4 || meta == 5 || meta == 6 || meta == 7) {
					ticks += 2;
				}
				if(meta == 8 || meta == 9 || meta == 10 || meta == 11) {
					ticks += 3;
				}
				if(meta == 12 || meta == 13 || meta == 14 || meta == 15) {
					ticks += 4;
				}
			}
			if(block instanceof BlockRedstoneTorch) {
				ticks += 1;
			}
		}
	}

	@SubscribeEvent
	public void onClick(MouseDownEvent event) {
		if(event.getButton() == 0 && ticks > 0 && mc.thePlayer != null && mc.thePlayer.isSneaking()) {
			ticks = 0;
			ChatUtils.sendLocalMessage("Ticks Cleared!", true);
		}

	}
	
	public void render() {
		if (this.mc.gameSettings.showDebugInfo) {
			return;
		}
		
		GL11.glPushMatrix();
		int offsetX = 0;
		int offsetY = 0;
		int posX = hud.getX();
		int posY = hud.getY();
		
		if(redstoneTicks) {
			String type = "Redstone Ticks: ";
			String value = ticks + "";
			if(isUsingStaticChroma) {
				DrawUtils.drawChromaString(type, posX , posY + offsetY, true, true);
					
			} else if(isUsingWaveChroma) {
				DrawUtils.drawChromaString(type, posX , posY + offsetY, false, true);
				DrawUtils.drawChromaString(value, posX + mc.fontRendererObj.getStringWidth(type), posY + offsetY, false, true);		
			} else {
				mc.fontRendererObj.drawStringWithShadow(type,(float) posX,(float) posY + offsetY, tColor.getRGB());
			}
			if(isUsingStaticChroma) {
				DrawUtils.drawChromaString(value, posX + mc.fontRendererObj.getStringWidth(type), posY + offsetY, true, true);
			} else if(isUsingWaveChroma) {
				DrawUtils.drawChromaString(value, posX + mc.fontRendererObj.getStringWidth(type), posY + offsetY, false, true);		
			} else {
				mc.fontRendererObj.drawStringWithShadow(value,(float) posX + mc.fontRendererObj.getStringWidth(type) ,(float) posY + offsetY, vColor.getRGB());
			}
			offsetY += 10;
		}
		if(gameTicks) {
			String type = "Game Ticks: ";
			String value = (ticks * 2) + "";
			if(isUsingStaticChroma) {
				DrawUtils.drawChromaString(type, posX , posY + offsetY, true, true);
					
			} else if(isUsingWaveChroma) {
				DrawUtils.drawChromaString(type, posX , posY + offsetY, false, true);
				DrawUtils.drawChromaString(value, posX + mc.fontRendererObj.getStringWidth(type), posY + offsetY, false, true);		
			} else {
				mc.fontRendererObj.drawStringWithShadow(type,(float) posX,(float) posY + offsetY, tColor.getRGB());
			}
			if(isUsingStaticChroma) {
				DrawUtils.drawChromaString(value, posX + mc.fontRendererObj.getStringWidth(type), posY + offsetY, true, true);
			} else if(isUsingWaveChroma) {
				DrawUtils.drawChromaString(value, posX + mc.fontRendererObj.getStringWidth(type), posY + offsetY, false, true);		
			} else {
				mc.fontRendererObj.drawStringWithShadow(value,(float) posX + mc.fontRendererObj.getStringWidth(type) ,(float) posY + offsetY, vColor.getRGB());
			}
			offsetY += 10;
		}
		if(seconds) {
			String type = "Seconds: ";
			String value = (ticks/10) + "";
			if(isUsingStaticChroma) {
				DrawUtils.drawChromaString(type, posX , posY + offsetY, true, true);
					
			} else if(isUsingWaveChroma) {
				DrawUtils.drawChromaString(type, posX , posY + offsetY, false, true);
				DrawUtils.drawChromaString(value, posX + mc.fontRendererObj.getStringWidth(type), posY + offsetY, false, true);		
			} else {
				mc.fontRendererObj.drawStringWithShadow(type,(float) posX,(float) posY + offsetY, tColor.getRGB());
			}
			if(isUsingStaticChroma) {
				DrawUtils.drawChromaString(value, posX + mc.fontRendererObj.getStringWidth(type), posY + offsetY, true, true);
			} else if(isUsingWaveChroma) {
				DrawUtils.drawChromaString(value, posX + mc.fontRendererObj.getStringWidth(type), posY + offsetY, false, true);		
			} else {
				mc.fontRendererObj.drawStringWithShadow(value,(float) posX + mc.fontRendererObj.getStringWidth(type) ,(float) posY + offsetY, vColor.getRGB());
			}
			offsetY += 10;
		}
		hud.setHeight(offsetY);
		hud.setWidth(mc.fontRendererObj.getStringWidth("Redstone Ticks: " + ticks));
		GL11.glColor3f(1, 1, 1);
		GL11.glPopMatrix();
	}

}
