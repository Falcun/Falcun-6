package net.mattbenson.schemshare;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.input.KeyDownEvent;
import net.mattbenson.events.types.render.RenderEvent;
import net.mattbenson.events.types.render.RenderType;
import net.mattbenson.fonts.Fonts;
import net.mattbenson.network.common.GroupData;
import net.mattbenson.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;

public class SchemShareManager {
	private final int key = Keyboard.KEY_Y;
	private final long displayTime = 10 * 1000L;
	
	private List<SchemShareRequest> requests;
	
	public SchemShareManager() {
		requests = new CopyOnWriteArrayList<>();
	}
	
	@SubscribeEvent
	public void onRender(RenderEvent event) {
		if(event.getRenderType() != RenderType.INGAME_OVERLAY) {
			return;
		}
		
		if(requests.isEmpty()) {
			return;
		}
		
		SchemShareRequest req = requests.get(0);
		
		if(req == null) {
			return;
		}
		
		long time = System.currentTimeMillis();
		
		if(!req.isActive()) {
			req.setActive();
		} else {
			if(req.isAccepted() || time - req.getStart() > displayTime) {
				requests.remove(0);
				return;
			}
		}
		
		ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
		
		int barBackground = new Color(0, 175, 0, 150).getRGB();
		int barColor = new Color(0, 255, 0, 150).getRGB();
		int backgroundColor = new Color(50, 50, 50, 150).getRGB();
		int textColor = new Color(255, 255, 255, 150).getRGB();
		int width = 150;
		int height = 50;
		
		int barHeight = 2;
		int y = 25;
		
		String line1 = "A group member requested to load \"" + req.getName() + "\".";
		int line1Width = (int)Fonts.RalewayExtraBold.getStringWidth(line1);
		
		if(line1Width > width) {
			width = line1Width + 20;
		}
		
		String title = "Schematica request - " + req.getGroup().getName();
		int titleWidth = (int)Fonts.RalewayExtraBoldLarge.getStringWidth(title);
		
		if(titleWidth > width) {
			width = titleWidth + 20;
		}
		
		float deleteDelta = (float)(time - req.getStart()) / displayTime;
		int baseX = res.getScaledWidth() / 2 - width / 2;
		DrawUtils.drawRect(baseX, y - barHeight, baseX + width - 1, y, barBackground);
		DrawUtils.drawRect((int)(baseX + (width * deleteDelta)), y - barHeight, baseX + width - 1, y, barColor);
		
		GlStateManager.pushMatrix();
		DrawUtils.drawRect(res.getScaledWidth() / 2 - width / 2, y, res.getScaledWidth() / 2 + width / 2, y + height, backgroundColor);
		
		Fonts.RalewayExtraBoldLarge.drawString(title, res.getScaledWidth() / 2 - titleWidth / 2, y + 3, textColor);
		
		int xPos = Math.round((float)req.getX());
		int yPos = Math.round((float)req.getY());
		int zPos = Math.round((float)req.getZ());
		
		Fonts.RalewayExtraBold.drawString(line1, res.getScaledWidth() / 2 - line1Width / 2, y + 18, textColor);
		
		String line2 = "X: " + xPos + ", Y: " + yPos + ", Z: " + zPos + ".";;
		Fonts.RalewayExtraBold.drawString(line2, res.getScaledWidth() / 2 - Fonts.RalewayExtraBold.getStringWidth(line2) / 2, y + 27, textColor);
		
		String accept = "Press " + Keyboard.getKeyName(key) + " to load.";
		Fonts.RalewayExtraBold.drawString(accept, res.getScaledWidth() / 2 - Fonts.RalewayExtraBold.getStringWidth(accept) / 2, y + 40, textColor);
		GlStateManager.popMatrix();
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	@SubscribeEvent
	public void onEvent(KeyDownEvent event) {
		if(Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {
			if(Minecraft.getMinecraft().currentScreen != null) {
				return;
			}
			
			if(requests.isEmpty()) {
				return;
			}
			
			int key = event.getKey();
			
			if(this.key == key) {
				SchemShareRequest request = requests.get(0);
				
				if(request != null && !request.isAccepted() && request.isActive()) {
					request.accept();
				}
			}
		}
	}
	
	public void addNewRequest(GroupData group, String name, int x, int y, int z, String schematica, List<EnumFacing> flip, List<EnumFacing> rotation) {
		for(SchemShareRequest request : requests) {
			if(schematica.equalsIgnoreCase(request.getSchematica()) && x == request.getX() && y == request.getY() && z == request.getZ()) {
				return;
			}
		}
		
		requests.add(new SchemShareRequest(group, name, x, y, z, schematica, flip, rotation));
	}
}
