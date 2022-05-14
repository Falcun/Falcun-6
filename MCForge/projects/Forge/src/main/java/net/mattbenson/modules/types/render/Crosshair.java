package net.mattbenson.modules.types.render;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.lwjgl.input.Mouse;

import net.mattbenson.config.ConfigValue;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.render.RenderEvent;
import net.mattbenson.events.types.render.RenderType;
import net.mattbenson.events.types.world.OnTickEvent;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

public class Crosshair extends Module {
	@ConfigValue.Color(name = "Draw color")
	private Color color = Color.RED;
	
	@ConfigValue.Integer(name = "Brush size", min = 2, max = 10)
	private int brushSize = 2;
	
	@ConfigValue.Boolean(name = "Rainbow color")
	private boolean rainbow;
	
	@ConfigValue.Boolean(name = "Delete mode")
	private boolean deleteMode;
	
	public boolean stoppedMouse1;
	
	public static int rainbowSpeedNerf = 0;
	public static int rainbowSpeedNerfProg = 0;
	
	public static Color rainbowColor = new Color(0, 0, 0, 0);
	public static int rainbowR = 255;
	public static int rainbowG = 0;
	public static int rainbowB = 0;

	public static int[][] crosshair;
	public ResourceLocation crosshairResource;
	public boolean shouldUpdateCrosshair = false;
	
	int containerColor = new Color(50, 50, 50, 255).getRGB();
	int parentContainerColor = new Color(0, 0, 0, 255).getRGB();
	int innerContainerColor = new Color(25, 25, 25, 255).getRGB();
	int groupHeaderColor = new Color(20, 20, 20, 255).getRGB();
	int textColor = new Color(255, 255, 255, 255).getRGB();
	int groupContainerColor = new Color(35, 35, 35, 255).getRGB();
	
	public Crosshair() {
		super("Crosshair", ModuleCategory.RENDER);
		crosshair = new int[84][74];
	}
	
	@SubscribeEvent
	public void onRender(RenderEvent event) {
		if(event.getRenderType() != RenderType.CROSSHAIR) {
			return;
		}
		event.setCancelled(true);
		drawCrosshair();
	}
	
    public void drawPicker(int drawX, int drawY, int drawWidth, int drawHeight, int mouseX, int mouseY) {
		GuiScreen.drawRectangle(drawX, drawY, drawX + drawWidth, drawY + drawHeight, parentContainerColor);
		GuiScreen.drawRectangle(drawX + drawWidth / 2 - 1, drawY + 1, drawX + drawWidth / 2 + 1, drawY + drawHeight - 1, containerColor);
		GuiScreen.drawRectangle(drawX + 1, drawY + drawHeight / 2 - 1, drawX + drawWidth - 1, drawY + drawHeight / 2 + 1, containerColor);
		
		boolean hovering = true;
		boolean mouseDown = Mouse.isButtonDown(0) && hovering;
		
		if(!mouseDown && hovering) {
			stoppedMouse1 = true;
		} else if(!hovering) {
			stoppedMouse1 = false;
		}
		
		if(mouseDown && !stoppedMouse1) {
			mouseDown = false;
		}
		
		int mX = Math.round(mouseX);
		int mY = Math.round(mouseY);
		int theColor = deleteMode ? Color.white.getRGB() : color.getRGB();
		
		drawX = (drawX + 1);
		drawY = (drawY + 1);
		boolean edited = false;
		
		for(int x = 0; x < crosshair.length; x++) {
			for(int y = 0; y < crosshair[x].length; y++) {
				int color = crosshair[x][y];
				int theX = drawX + x;
				int theY = drawY + y;
							
				if(mX == theX && mY == theY && mouseDown && hovering) {
					for(int xx = -(brushSize / 2) + x; xx < x + brushSize / 2; xx++) {
						for(int yy = -(brushSize / 2) + y; yy < y + brushSize / 2; yy++) {
							crosshair[xx < crosshair.length && xx >= 0 ? xx : x][yy < crosshair[x].length && yy >= 0 ? yy : y] = deleteMode ? 0 : theColor;
							edited = true;
						}
					}
				}
				
				if(color == 0) {
					continue;
				}
				
				drawPixel(theX, theY, rainbow ? rainbowColor.getRGB() : color);
			}
		}
		
		if(edited) {
			shouldUpdateCrosshair = true;
		}
		
		if(hovering) {
			for(int xx = -(brushSize / 2) + mX; xx < mX + brushSize / 2; xx++) {
				for(int yy = -(brushSize / 2) + mY; yy < mY + brushSize / 2; yy++) {
					if(xx < crosshair.length + drawX && xx >= drawX && yy < crosshair[0].length + drawY && yy >= drawY)
						drawPixel(xx, yy, theColor);
				}
			}	
		}
    }
    
    
	public void drawBackground(int x, int y, int width, int height) {
		GuiScreen.drawRectangle(x, y, x + width, y + height, parentContainerColor);
		GuiScreen.drawRectangle(x + 1, y + 1, x + width - 1, y + height - 1, containerColor);
		GuiScreen.drawRectangle(x + 2, y + 2, x + width - 2, y + height - 2, innerContainerColor);
	}
    
	public void drawPixel(int x, int y, int color) {
		GuiScreen.drawRectangle(x, y, x + 1, y + 1, color);
	}
    
	 public void drawCrosshair() {
	    	if(!enabled) {
	    		return;
	    	}
	    	
	    	int width = crosshair.length;
			int height = crosshair[0].length;
			
			if(crosshairResource == null || shouldUpdateCrosshair) {
				if(crosshairResource != null) {
					mc.renderEngine.deleteTexture(crosshairResource);
					crosshairResource = null;
				}
				
				BufferedImage bufferedCrosshair = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
				
				for(int x = 0; x < crosshair.length; x++) {
					for(int y = 0; y < crosshair[x].length; y++) {
						int color = crosshair[x][y];
						
						if(color != 0 && rainbow) {
							color = rainbowColor.getRGB();
						}
						
						bufferedCrosshair.setRGB(x, y, color);
					}
				}
				DynamicTexture texture = new DynamicTexture(bufferedCrosshair);
				bufferedCrosshair.getRGB(0, 0, bufferedCrosshair.getWidth(), bufferedCrosshair.getHeight(), texture.getTextureData(), 0, bufferedCrosshair.getWidth());
				final ResourceLocation resource = mc.getTextureManager().getDynamicTextureLocation("custom-crosshair", texture);
				crosshairResource = resource;
				
				shouldUpdateCrosshair = false;
			}
			
			if(crosshairResource != null) {
				ScaledResolution sr = new ScaledResolution(mc);
				int drawX = sr.getScaledWidth() / 2 - width / 2;
				int drawY = sr.getScaledHeight() / 2 - height / 2;
				
				drawImage(crosshairResource, drawX, drawY, width, height);
				
				if(crosshairResource != null) {
					if(rainbow) {
						shouldUpdateCrosshair = true;
					}
				}
			}
		}
	    
		public void drawImage(ResourceLocation image, int x, int y, int width, int height) {
		    GlStateManager.enableAlpha();
		    GlStateManager.enableBlend();
		    GlStateManager.blendFunc(770, 771);
		    Minecraft.getMinecraft().getTextureManager().bindTexture(image);
		    GlStateManager.color(1, 1, 1);
			GuiScreen.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, width, height, width, height);
		    GlStateManager.disableBlend();
		    GlStateManager.disableAlpha();
		}

	@SubscribeEvent
	public void onTick(OnTickEvent event) {
		if(rainbowSpeedNerfProg >= rainbowSpeedNerf) {
			if(rainbowR == 255 && rainbowG != 255 && rainbowB == 0)
				rainbowG++;
			else if(rainbowR != 0 && rainbowG == 255 && rainbowB == 0)
				rainbowR--;
			else if(rainbowR == 0 && rainbowG == 255 && rainbowB != 255)
				rainbowB++;
			else if(rainbowR == 0 && rainbowG != 0 && rainbowB == 255)
				rainbowG--;
			else if(rainbowR != 255 && rainbowG == 0 && rainbowB == 255)
				rainbowR++;
			else if(rainbowR == 255 && rainbowG == 0 && rainbowB != 0)
				rainbowB--;
			rainbowColor = new Color(rainbowR, rainbowG, rainbowB);
			rainbowSpeedNerfProg = 0;
		} else {
			rainbowSpeedNerfProg++;
		}
	}
}
