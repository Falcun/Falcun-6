package net.mattbenson.notifications;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.mattbenson.fonts.Fonts;
import net.mattbenson.fonts.NahrFont;
import net.mattbenson.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

public class Notification {
	private static Color BACKGROUND_COLOR = new Color(47, 53, 79, 255);
	private static Color TEXT_COLOR = new Color(255, 255, 255, 255);
	private static Color LINE_COLOR = new Color(255, 255, 255, 255);
	
	private static int FADEOUT_TIME = 750;
	private static long MAX_TIME = 10 * 1000L;
	
	private static NahrFont FONT;
	
	private String text;
	private Color color;
	private long start;
	
	public Notification(String text, Color color) {
		this.text = text;
		this.color = color;
		start = System.currentTimeMillis();
		
		if(FONT == null) {
			FONT = Fonts.RalewayExtraBoldSmall;
		}
	}
	
	public String getText() {
		return text;
	}
	
	public Color getColor() {
		return color;
	}
	
	public long getStart() {
		return start;
	}
	
	public boolean isDead() {
		return System.currentTimeMillis() - start > MAX_TIME;
	}
	
	public int draw(int y) {
		if(isDead()) {
			return y;
		}
		
		int x = new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth();
		int stringWidth = (int)FONT.getStringWidth(text) + 10;

		int width = 30;
		int height = 20;
		
		float textReady = getDeltaByTime(1900, 600);
		
		float fadeOut = getDeltaByTime((int)MAX_TIME - FADEOUT_TIME, FADEOUT_TIME);
		
		if(fadeOut > 0) {
			x = x + Math.round((stringWidth + width) * fadeOut);
		}
		
		float delta = getDeltaByTime(0, 500);		
		drawRect(Math.round(x - (width * delta)), y, x, y - height, BACKGROUND_COLOR);
		
		if(delta == 1) {
			delta = getDeltaByTime(500, 1000);
			float oldDelta = delta;
			
			if(delta == 1) {
				delta = getDeltaByTime(1500, 400);
				drawRect(x - width + 2, y - 2, x - width, y - 2 - Math.round((height - 4) * delta), LINE_COLOR);
				
				if(delta == 1) {
					delta = textReady;
					drawRect(x - width - Math.round(stringWidth * delta), y - height, x - width, y, BACKGROUND_COLOR);
				}
			}
			
			float textOutDelta = delta = getDeltaByTime(2500, 1500);
			if(textReady == 1) {
				FONT.drawString(text, x - width - Math.round(stringWidth * delta) + 5, y - 14, TEXT_COLOR.getRGB());
				GL11.glColor4f(BACKGROUND_COLOR.getRed() / 255F, BACKGROUND_COLOR.getGreen() / 255F, BACKGROUND_COLOR.getBlue() / 255F, BACKGROUND_COLOR.getAlpha() / 255F);
				drawRect(x - width, y, x, y - height, BACKGROUND_COLOR);
				drawRect(x - width + 2, y - 2, x - width, y - 2 - height + 4, LINE_COLOR);
			}
			
			DrawUtils.drawImage(NotificationManager.LOGO, x - 24, y - 20, 20, 20);
			
			drawRect(x - width, y - height, x, y - Math.round(20 * oldDelta), BACKGROUND_COLOR);
			
			if(textOutDelta == 1) {
				float timeLeft = getDeltaByTime(4000, (int)MAX_TIME - 4000 - FADEOUT_TIME);
				drawRect(x - width - stringWidth, y, x - Math.round((width + stringWidth) * (1 - timeLeft)), y - 1, color);
			}
		}
				
		return y - height;
	}
	
	//Legacy support
	private void drawRect(int x, int y, int x2, int y2, Color color) {
		Gui.drawRectangle(x, y, x2, y2, color.getRGB());
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
	
	private float getDeltaByTime(int start, int end) {
		float delta = (float)((System.currentTimeMillis() - this.start) - start) / end;
		
		if(delta > 1) {
			delta = 1;
		} else if(delta < 0) {
			delta = 0;
		}

		return delta;
	}
}
