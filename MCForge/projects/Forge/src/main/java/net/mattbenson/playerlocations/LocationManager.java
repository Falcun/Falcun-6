package net.mattbenson.playerlocations;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import net.mattbenson.Falcun;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.network.ServerSwitchEvent;
import net.mattbenson.events.types.render.RenderEvent;
import net.mattbenson.events.types.render.RenderType;
import net.mattbenson.events.types.world.OnTickEvent;
import net.mattbenson.fonts.Fonts;
import net.mattbenson.modules.types.mods.GroupLocation;
import net.mattbenson.network.common.GroupData;
import net.mattbenson.network.network.packets.groups.GroupList;
import net.mattbenson.network.network.packets.play.PlayerUpdate;
import net.mattbenson.w2s.W2SUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class LocationManager {
	private final float maxPulse = 0.7F;
	private final float minPulse = 0.3F;
	private final float pulseSpeed = 0.03F;
	
	private Color orginalColor = new Color(255, 0, 0, 255);
	
	private final int circleRadius = 50;
	private final int triangleSize = 15;
	
	private float pulsating;
	private boolean increasePulsate;

	
	private static final int TEXT_COLOR = new Color(255, 255, 255, 255).getRGB();
	public static final long MAX_UPDATE_AGE_MILLIS = 10000L;
	
	public static Map<UUID, PlayerLocation> locations;
	
	public LocationManager() {
		locations = new ConcurrentHashMap<>();
	}
	
	@SubscribeEvent
	public void onServerJoin(ServerSwitchEvent event) {
		locations.clear();
	}
	
	@SubscribeEvent
	public void onRender(RenderEvent event) {
		if(Minecraft.getMinecraft().theWorld == null) return;
		if(Minecraft.getMinecraft().thePlayer == null) return;
		if(event.getRenderType() != RenderType.INGAME_OVERLAY) return;
		
		GlStateManager.pushMatrix();
		float value = 2f / new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
		GlStateManager.scale(value, value, value);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
		
		GL11.glPushAttrib(GL11.GL_TRANSFORM_BIT);
		GlStateManager.matrixMode(GL11.GL_PROJECTION);
		GlStateManager.pushMatrix();
		GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		GlStateManager.pushMatrix();
		Minecraft.getMinecraft().entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
		
		FloatBuffer modelViewMatrix = BufferUtils.createFloatBuffer(16);
		FloatBuffer projectionMatrix = BufferUtils.createFloatBuffer(16);
		IntBuffer viewport = BufferUtils.createIntBuffer(16);
		
		GlStateManager.getFloat(GL11.GL_MODELVIEW_MATRIX, modelViewMatrix);
		GlStateManager.getFloat(GL11.GL_PROJECTION_MATRIX, projectionMatrix);
		GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);
		
		GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		GlStateManager.popMatrix();
		GlStateManager.matrixMode(GL11.GL_PROJECTION);
		GlStateManager.popMatrix();
		GlStateManager.popAttrib();	
		
		long time = System.currentTimeMillis();
		
		for(Entry<UUID, PlayerLocation> entry : locations.entrySet()) {
			GroupData group = entry.getValue().getGroup();
	
			double xPos = entry.getValue().getX();
			double yPos = entry.getValue().getY();
			double zPos = entry.getValue().getZ();
			
			EntityPlayer entity = Minecraft.getMinecraft().theWorld.getPlayerEntityByUUID(entry.getKey());
			boolean visible = false;
			
			if(entity != null) {
				continue;
			} else {
				if(time - entry.getValue().getTime() > MAX_UPDATE_AGE_MILLIS) {
					continue;
				}
			}
			
			if(!isInServer(entry.getKey())) {
				continue;
			}
			
			if(Minecraft.getMinecraft().thePlayer.dimension != entry.getValue().getDimension()) {
				continue;
			}
			
			float[] pos = W2SUtils.getScreenPos(xPos, yPos, zPos, modelViewMatrix, projectionMatrix, viewport);
			
			//drawTriangeForPos(xPos, yPos, zPos, modelViewMatrix, projectionMatrix, viewport);
			
			if(pos == null) {
				continue;
			}
			
			int x = (int)pos[0] / 2;
			int y = (int)pos[1] / 2;
			
			boolean head = false;
			
			int y2 = ((int)pos[1] / 2) - 15;
			
			int width = (int)Fonts.RalewayExtraBold.getStringWidth(entry.getValue().getName()) / 2;

			
			//Name
			if (Falcun.getInstance().moduleManager.getModule(GroupLocation.class).isNametag) {
				Fonts.RalewayExtraBold.drawString(entry.getValue().getName(), x - width / 2, y2, TEXT_COLOR);
				y2 += 4;
			}
	
			
			if (Falcun.getInstance().moduleManager.getModule(GroupLocation.class).isHealth) {

			} else {
				y2 += 7;
			}
			
			

			
			y2 -= 15;
			
			int distance = Math.round((float)Minecraft.getMinecraft().thePlayer.getDistance(xPos, yPos, zPos));
			String distanceText = "(" + distance + "M)";
			int distanceWidth = (int)Fonts.ubuntuMedium.getStringWidth(distanceText) / 2;
			
			
			
			if (Falcun.getInstance().moduleManager.getModule(GroupLocation.class).isPotions) {
			int res = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
			
			ItemStack item = new ItemStack(Items.potionitem);
			item.setItemDamage(16421);
			
			GlStateManager.pushMatrix();
			GlStateManager.enableLighting();
			GlStateManager.enableRescaleNormal();
			
			if(res == 1) { // Small
				value = 0.5f / new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
			} else if(res == 2) { // Medum
				value = 1f / new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
			} else if(res == 3) { // Large
				value = 1.5f / new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
			}
			
			GlStateManager.scale(value, value, value);
			//Potion
			y2 -= 3;
			
			if (Falcun.getInstance().moduleManager.getModule(GroupLocation.class).isSkin) {
				Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(item, x * 2 - 20, y2 * 2 + 35);
	            GlStateManager.disableLighting();
				GlStateManager.popMatrix();
				
				//Potions
				Fonts.RalewayExtraBold.drawString("" + entry.getValue().getPotions(), x + 18, y2 + 17, TEXT_COLOR);
				y2 += 3;
			} else {
				Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(item, x * 2 - 5, y2 * 2 + 35);
	            GlStateManager.disableLighting();
				GlStateManager.popMatrix();
				
				//Potions
				Fonts.RalewayExtraBold.drawString("" + entry.getValue().getPotions(), x + 5, y2 + 17, TEXT_COLOR);
				y2 -= 3;
			}
			} 
			
			
			if (!Falcun.getInstance().moduleManager.getModule(GroupLocation.class).isSkin && !Falcun.getInstance().moduleManager.getModule(GroupLocation.class).isPotions) {
				y2 -= 15;
			}
			
			
			//Distance
			if (Falcun.getInstance().moduleManager.getModule(GroupLocation.class).isDistance) {
			Fonts.ubuntuMedium.drawString(distanceText, x - distanceWidth / 2 + 2, y2 + 30, TEXT_COLOR);
			}
			
		}
		
		GlStateManager.popMatrix();
	}
	
	private boolean isInServer(UUID user) {
		NetHandlerPlayClient handler = Minecraft.getMinecraft().getNetHandler();
		
		if(handler == null) {
			return false;
		}
		
		if(handler.getPlayerInfo(user) == null) {
			return false;
		}
		
		return true;
	}

	public void drawTriangeForPos(double posX, double posY, double posZ, FloatBuffer modelViewMatrix, FloatBuffer projectionMatrix, IntBuffer viewport) {
		Color color = new Color(orginalColor.getRGB(), true);		
		color = new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.round(pulsating * orginalColor.getAlpha()));
		
		FloatBuffer screen = BufferUtils.createFloatBuffer(16);
		FloatBuffer screenMax = BufferUtils.createFloatBuffer(16);
		
		float[] xAngles = new float[2];
		float[] yAngles = new float[2];
		float minX = Integer.MAX_VALUE;
		float minY = Integer.MAX_VALUE;
		float maxX = -Integer.MAX_VALUE;
		float maxY = -Integer.MAX_VALUE;
		
		float change = 0;
		
		if(W2SUtils.gluProject((float)(posX), (float)(posY), (float)(posZ), modelViewMatrix, projectionMatrix, viewport, screen)) {
			if(screen.get(2) >= 1) {
				change = 180;
			}
			
			if(W2SUtils.gluProject((float)(posX), (float)(posY), (float)(posZ), modelViewMatrix, projectionMatrix, viewport, screenMax)) {
				xAngles[0] = screen.get(0);
				xAngles[1] = screenMax.get(0);
				yAngles[0] = Minecraft.getMinecraft().displayHeight - screen.get(1);
				yAngles[1] = Minecraft.getMinecraft().displayHeight - screenMax.get(1);
				
				for(float x : xAngles) {
					if(x > maxX) {
						maxX = x;
					}
					
					if(x < minX) {
						minX = x;
					}
				}
				
				for(float y : yAngles) {
					if(y > maxY) {
						maxY = y;
					}
					
					if(y < minY) {
						minY = y;
					}
				}
				
				if(minX > Minecraft.getMinecraft().displayWidth || minY > Minecraft.getMinecraft().displayHeight || maxX < 0 || maxY < 0 || change > 0) {
					ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
					int middleX = res.getScaledWidth() / 2;
					int middleY = res.getScaledHeight() / 2;
					
					double centerX = minX + (maxX - minY);
					double centerY = minY + (maxY - minY);
					
					float angle = (float)(Math.atan2(centerY - middleY, centerX - middleX) * (180.0F / Math.PI)) + 90;
					
					drawTriangle(Math.round(angle + change) % 360, color);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onTick(OnTickEvent event) {
		if(Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null) {
			return;
		}
		
		if(increasePulsate) {
			pulsating += pulseSpeed;
		} else {
			pulsating -= pulseSpeed;
		}
		
		if(pulsating >= maxPulse) {
			pulsating = maxPulse;
			increasePulsate = false;
		} else if(pulsating <= minPulse) {
			pulsating = minPulse;
			increasePulsate = true;
		}
		
		PlayerUpdate.writePlayerUpdate();
	}
	
	public void addLocation(UUID uuid, String name, GroupData group, long time, double x, double y, double z, int dimension, float health, int potions) {
		locations.put(uuid, new PlayerLocation(name, group, time, x, y, z, dimension, health, potions));
	}
	
	private void drawSkinHead(ResourceLocation skin, int x, int y, int dimension, float opacity) {
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		Minecraft.getMinecraft().getTextureManager().bindTexture(skin);
		GlStateManager.color(1, 1, 1, opacity);
		float multiplier = dimension / 18F;
		GuiIngame.drawModalRectWithCustomSizedTexture(x, y, 19 * multiplier, 19 * multiplier, Math.round(18 * multiplier), Math.round(18 * multiplier), 150 * multiplier, 150 * multiplier);
		GuiIngame.drawModalRectWithCustomSizedTexture(x, y, (19 * 5 * multiplier) - multiplier, 19 * multiplier, Math.round(18 * multiplier), Math.round(18 * multiplier), 150 * multiplier, 150 * multiplier);
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
	}
	
	private void drawTriangle(int angle, Color color) {
		ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
		int width = res.getScaledWidth() / 2;
		int height = res.getScaledHeight() / 2;
		
		double radius = (angle - 90) * (Math.PI / 180);
		int x = (int)(width + ((circleRadius * 4) * Math.cos(radius)));
		int y = (int)(height + ((circleRadius  * 4) * Math.sin(radius)));
		
		drawTriangle(x, y, triangleSize, triangleSize, angle, color.getRGB());
	}
	
	private void drawTriangle(int x, int y, int width, int height, int pointing, int color) {
		float f = (float)(color >> 24 & 255) / 255.0F;
		float f1 = (float)(color >> 16 & 255) / 255.0F;
		float f2 = (float)(color >> 8 & 255) / 255.0F;
		float f3 = (float)(color & 255) / 255.0F;
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, 0);
		GlStateManager.rotate(pointing, 0, 0, 1);
		GlStateManager.translate(-x, -y, 0);
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer bufferbuilder = tessellator.getWorldRenderer();
		GlStateManager.color(f1, f2, f3, f);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
		bufferbuilder.pos((double)x + (width / 3D), (double)y, (double)0).endVertex();
		bufferbuilder.pos((double)x + width, (double)y + height, (double)0).endVertex();
		bufferbuilder.pos((double)x - (width / 3D), (double)y + height, (double)0).endVertex();
		bufferbuilder.pos((double)x + width, (double)y + height, (double)0).endVertex();
		tessellator.draw();
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
		GlStateManager.popMatrix();
	}
}
