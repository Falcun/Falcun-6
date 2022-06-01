package net.mattbenson.cosmetics.crates;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import net.mattbenson.Falcun;
import net.mattbenson.chat.ChatUtils;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.render.RenderEvent;
import net.mattbenson.events.types.render.RenderType;
import net.mattbenson.modules.types.other.PerspectiveMod;
import net.mattbenson.network.common.Reward;
import net.mattbenson.utils.DrawUtils;
import net.mattbenson.w2s.W2SUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class CrateManager {
	public static ResourceLocation SOUND_RESOURCE = new ResourceLocation("emoticons", "ball");
	public static final double MAX_DISTANCE = 5000D;
	public static final long CRATE_SPIN_TIME = 7000;
	
	private List<Crate> crates;
	
	public CrateManager() {
		crates = new CopyOnWriteArrayList<>();
	}
	
	@SubscribeEvent
	public void draw(RenderEvent event) {
		if(event.getRenderType() != RenderType.INGAME_OVERLAY) {
			return;
		}
		
		if(!crates.isEmpty()) {
			List<Crate> remove = new ArrayList<>();
			long time = System.currentTimeMillis();
			
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
			
			for(Crate crate : crates) {
				if(crate.getRenderDelta() == -1) {
					long death = time - crate.getFinishTime();
					
					if(death > CRATE_SPIN_TIME) {
						onReset(crate);
						remove.add(crate);
						continue;
					}
				} else {
					long alive = time - crate.getStart();
					
					if(alive > CRATE_SPIN_TIME * 10) {
						onReset(crate);
						remove.add(crate);
						crate.onHit(crate.getReward());
						continue;
					}
				}
				
				if(crate.getEntity() != null && crate.getEntity().getDistanceSqToEntity(Minecraft.getMinecraft().thePlayer) < MAX_DISTANCE) {
					drawCrate(crate, modelViewMatrix, projectionMatrix, viewport);
				}
			}
			
			GlStateManager.popMatrix();
			
			crates.removeAll(remove);
		}
	}
	
	private void onReset(Crate crate) {
		if(crate.getEntity().getUniqueID().equals(Minecraft.getMinecraft().thePlayer.getUniqueID())) {
			//Falcun.getInstance().moduleManager.getModule(PerspectiveMod.class).perspectiveToggled = false;
			//Falcun.getInstance().moduleManager.getModule(PerspectiveMod.class).isCrating = false;
			Minecraft.getMinecraft().gameSettings.thirdPersonView = 0;   
		}
	}
	
	private void drawCrate(Crate crate, FloatBuffer modelViewMatrix, FloatBuffer projectionMatrix, IntBuffer viewport) {
		Minecraft mc = Minecraft.getMinecraft();
		float[] points = W2SUtils.getEntityScreenBounds(crate.getEntity(), modelViewMatrix, projectionMatrix, viewport);
		
		if(points == null) {
			return;
		}
		
		int backgroundColor = new Color(0, 0, 50).getRGB();
		int lineColor = new Color(255, 0, 255).getRGB();
		int textColor = new Color(0, 0, 0).getRGB();
		
		int x = (int)points[0] / 2;
		int y = (int)points[1] / 2;
		
		int heightOffset = 10;  // height from head
		int width = 300; 		// width of the spinning box
		int height = 50;		// height of the spinning box
		int lineWidth = 2;		// line width of the line that shows middle.
		int spacing = 15;		// this is a kind of extra lining around the box on the side of the box to make spinning look smooth.
		int barHeight = 15;		// the grade bar under the name height.
		int barYSpacing = 5;	// the spacing of the bar and the entry
		int barXSpacing = 5;	// the spacing of the entries
		int speed = 2200;		// the speed of rotating
		
		x += (int)((points[2] / 2) / 2);
		x -= width / 2;
		y -= height + heightOffset;
		
		
		drawRect(x - spacing, y, x + width, y + height, backgroundColor);
		
		long start = System.currentTimeMillis() - crate.getStart();
		float progress = (float)start / CRATE_SPIN_TIME;
		int lineX = x + width / 2 - (lineWidth / 2);
		
		if(progress < 0) {
			progress = 0;
		}
		
		int theX = x;
		
		if(crate.getRenderDelta() == -1) {
			theX = x + crate.getSavedX();
		} else {
			int startX = x;
			
			for(int i = crate.getEntries().size() - 1; i > 0; i--) {
				Reward reward = crate.getEntries().get(i);
				
				String ratingText = reward.getRating().toString().replace("_", " ").toUpperCase();
				int ratingWidth = mc.fontRendererObj.getStringWidth(ratingText);
				int rewardNameWidth = mc.fontRendererObj.getStringWidth(reward.getName());
				int rewardWidth = Math.max(ratingWidth, rewardNameWidth) + 8;
				
				startX -= rewardWidth;
			}
			
			theX = (int)(startX + speed * progress);
		}
		
		int orginalX = theX;
		
		y -= 15;
		
		for(int i = crate.getEntries().size() - 1; i > 0; i--) {
			Reward reward = crate.getEntries().get(i);
			
			String ratingText = reward.getRating().toString().replace("_", " ").toUpperCase();
			int ratingWidth = mc.fontRendererObj.getStringWidth(ratingText);
			int rewardNameWidth = mc.fontRendererObj.getStringWidth(reward.getName());
			int rewardWidth = Math.max(ratingWidth, rewardNameWidth) + 8;
			
			int barStart = theX;
			int barEnd = theX + rewardWidth;
			
			if(barEnd > x + width) {
				barEnd = x + width;
			}
			
			if(barStart < x) {
				barStart = x;
			}
			
			if(barStart < lineX && barEnd > lineX) {
				String identifier = reward.getName() + i;
				
				if(crate.getLastSelect() == null || !crate.getLastSelect().equalsIgnoreCase(identifier)) {
					if(crate.getLastSelect() != null && crate.getRenderDelta() != -1) {
						Minecraft.getMinecraft().getSoundHandler().playSound(new PositionedSoundRecord(SOUND_RESOURCE, 1, 1, (float) crate.getEntity().posX, (float) crate.getEntity().posY, (float) crate.getEntity().posZ));
					}
					
					crate.setLastSelect(identifier);
				}
				
				if(crate.getRenderDelta() != -1) {
					if(progress >= 0.5 && reward.getName().equalsIgnoreCase(crate.getReward())) {
						crate.setSavedX(orginalX - x);
						crate.setFinishTime(System.currentTimeMillis());
						crate.setRenderDelta(-1);
						crate.onHit(reward.getName());
					}
				}
			}
			
			if(barEnd >= x) {
				drawString(reward.getName(), theX + rewardWidth / 2 - rewardNameWidth / 2, y + height / 2, Color.white.getRGB(), x, x + width);
				drawRect(barStart, y + height / 2 + mc.fontRendererObj.FONT_HEIGHT + barYSpacing, barEnd, y + height / 2 + barHeight + mc.fontRendererObj.FONT_HEIGHT + barYSpacing, reward.getRating().getColor().getRGB());
				drawString(ratingText, theX + rewardWidth / 2 - ratingWidth / 2, y + height / 2 + mc.fontRendererObj.FONT_HEIGHT + barYSpacing + 4, textColor, x, x + width);
			}
			
			if(theX + rewardWidth >= x + width) {
				break;
			}
			
			theX += barXSpacing + rewardWidth;
		}
		
		y += 15;
		
		drawRect(x - spacing, y, x, y + height, backgroundColor);
		drawRect(x + width, y, x + width + spacing, y + height, backgroundColor);
		drawRect(lineX, y, x + width / 2 + (lineWidth / 2), y + height, lineColor);
	}
	
	private void drawRect(int x, int y, int width, int height, int color) {
		DrawUtils.drawRect(x, y, width - x, height - y, color);
	}
	
	private void drawString(String string, int x, int y, int color, int min, int maxWidth) {
		Minecraft mc = Minecraft.getMinecraft();
		int xTmp = x;
		for (char textChar : string.toCharArray()) {
			if(xTmp > maxWidth) {
				break;
			}
			
			int width = mc.fontRendererObj.getCharWidth(textChar);
			
			if(xTmp < min) {
				xTmp += width;
				continue;
			}
			
			mc.fontRendererObj.drawString(String.valueOf(textChar), xTmp, y, color, false);
			xTmp += width;
		}
	}
	
	public void addCrate(Entity entity, List<Reward> rewards, String reward) {
		boolean found = false;
		
		for(Reward r : rewards) {
			if(r.getName().equalsIgnoreCase(reward)) {
				found = true;
				break;
			}
		}
		
		if(found) {
			if(entity.getUniqueID().equals(Minecraft.getMinecraft().thePlayer.getUniqueID())) {
				Minecraft.getMinecraft().displayGuiScreen((GuiScreen) null);
				Minecraft.getMinecraft().setIngameFocus();
				
				if(!Falcun.getInstance().moduleManager.getModule(PerspectiveMod.class).perspectiveToggled) {
					Falcun.getInstance().moduleManager.getModule(PerspectiveMod.class).cameraYaw = Minecraft.getMinecraft().getRenderManager().playerViewY + 180;
					Falcun.getInstance().moduleManager.getModule(PerspectiveMod.class).cameraPitch = 0;
					Falcun.getInstance().moduleManager.getModule(PerspectiveMod.class).perspectiveToggled = true; 
					Falcun.getInstance().moduleManager.getModule(PerspectiveMod.class).isCrating = true; 
					Minecraft.getMinecraft().gameSettings.thirdPersonView = 1;
				}
			}
			
			crates.add(new Crate(entity, rewards, reward) {
				@Override
				public void onHit(String reward) {
					if(entity.getUniqueID().equals(Minecraft.getMinecraft().thePlayer.getUniqueID())) {
						ChatUtils.sendLocalMessage("You successfully won \"" + reward + "\". Congratulations!", true);
					}
				}
			});
		}
	}

	public boolean hasCrate(Entity entity) {
		for(Crate crate : crates) {
			if(crate.getEntity().getUniqueID().equals(entity.getUniqueID())) {
				return true;
			}
		}
		
		return false;
	}
}
