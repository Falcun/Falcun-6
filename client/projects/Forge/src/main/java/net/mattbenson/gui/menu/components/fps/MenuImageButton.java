package net.mattbenson.gui.menu.components.fps;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import net.mattbenson.Falcun;
import net.mattbenson.gui.framework.components.MenuButton;
import net.mattbenson.gui.framework.draw.ButtonState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

public class MenuImageButton extends MenuButton {
	private static Color DISABLED_COLOR = new Color(255, 255, 255, 200);
	private static Color OVER_COLOR = new Color(255, 255, 255, 160);
	private static Color ACTIVE_COLOR = new Color(255, 255, 255, 180);
	private static Color ACTIVE_HOVER_COLOR = new Color(255, 255, 255, 150);
	
	private String imageId;
	
	private ResourceLocation disabledImage;
	private ResourceLocation normalImage;
	private ResourceLocation hoverImage;
	private ResourceLocation hoverActiveImage;
	private ResourceLocation activeImage;
	
	public MenuImageButton(int x, int y, int width, int height, BufferedImage image) {
		super("", x, y, width, height);
		this.imageId = image.toString() + ":image-button";
		
		loadImage(image);
	}
	
	@Override
	public void onPreSort() {
		int x = this.getRenderX();
		int y = this.getRenderY();
		int width = (this.width == -1 && this.height == -1) ? (getStringWidth(text) + minOffset * 2) : this.width;
		int height = (this.width == -1 && this.height == -1) ? (getStringHeight(text) + minOffset * 2) : this.height;
		int mouseX = parent.getMouseX();
		int mouseY = parent.getMouseY();
		
		ButtonState state = active ? ButtonState.ACTIVE : ButtonState.NORMAL;
		
		if(!disabled) {
			if(mouseX >= x && mouseX <= x + width - 1) {
				if(mouseY >= y && mouseY <= y + height - 1) {
					state = ButtonState.HOVER;
					
					if(mouseDown) {
						active = !active;
						onAction();
					}
				}
			}
		} else {
			state = ButtonState.DISABLED;
		}
		
		lastState = state;
	}
	
	public String getImageId() {
		return imageId;
	}
	
	@Override
	public void onRender() {
		if(isActive()) {
			if(lastState == ButtonState.HOVER) {
				lastState = ButtonState.HOVERACTIVE;
			}
		}
		
		int x = this.getRenderX();
		int y = this.getRenderY();
		
		drawImage(getImage(), x, y, getWidth(), getHeight());
		mouseDown = false;
	}
	
	private void loadImage(BufferedImage image) {
		loadDisabled(image);
		loadNormal(image);
		loadHover(image);
		loadActive(image);
		loadActiveHover(image);
	}
	
	private ResourceLocation getImage() {
		switch(lastState) {
			case ACTIVE:
				return activeImage;
				
			case DISABLED:
				return disabledImage;
				
			case HOVER:
				return hoverImage;
				
			case HOVERACTIVE:
				return hoverActiveImage;
				
			case NORMAL:
				return normalImage;
				
			case POPUP:
				return normalImage;
				
			default:
				return normalImage;
		}
	}
	
	private void loadDisabled(BufferedImage image) {
		disabledImage = colorImage(image, DISABLED_COLOR, ButtonState.DISABLED);
	}
	
	private void loadNormal(BufferedImage image) {
		normalImage = getResourceLocationFromImage(image, ButtonState.NORMAL);
	}
	
	private void loadHover(BufferedImage image) {
		hoverImage = colorImage(image, OVER_COLOR, ButtonState.HOVER);
	}
	
	private void loadActive(BufferedImage image) {
		activeImage = colorImage(image, ACTIVE_COLOR, ButtonState.ACTIVE);
	}
	
	private void loadActiveHover(BufferedImage image) {
		hoverActiveImage = colorImage(image, ACTIVE_HOVER_COLOR, ButtonState.HOVERACTIVE);
	}
	
	private ResourceLocation colorImage(BufferedImage src, Color color, ButtonState state) {
		int width = src.getWidth();
		int height = src.getHeight();
		
		BufferedImage tinted = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = tinted.createGraphics();
		RescaleOp rop = new RescaleOp(new float[] {color.getRed() / 255F, color.getGreen() / 255F, color.getRed() / 255F, color.getAlpha() / 255F}, new float[] {0, 0, 0, 0}, null);
		graphics.drawImage(src, rop, 0, 0);
		
		return getResourceLocationFromImage(tinted, state);
	}
	
	private ResourceLocation getResourceLocationFromImage(BufferedImage image, ButtonState state) {
        DynamicTexture dynamicTexture = new DynamicTexture(image);
		return Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(imageId + ":" + state.toString(), dynamicTexture);
	}
}
