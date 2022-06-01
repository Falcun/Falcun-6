package net.mattbenson.utils;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import net.mattbenson.Falcun;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

public class RenderUtils {
	public RenderUtils() {}
	
	public static int getShadowStart(int color, int amount) {
		Color old = new Color(color);
		
		int newAlpha = clampColor(old.getBlue() - amount);

		return new Color(old.getRed(), old.getGreen(), old.getBlue(), newAlpha).getRGB();
	}
	
	public static int getShadowEnd(int color, int amount) {
		Color old = new Color(color);
		
		int newRed = clampColor(old.getRed() - amount);
		int newGreen = clampColor(old.getGreen() - amount);
		int newBlue = clampColor(old.getBlue() - amount);
		
		return new Color(newRed, newGreen, newBlue, old.getAlpha()).getRGB();
	}
	
	private static int clampColor(int color) {
		if(color > 255) {
			return 255;
		} else if(color < 0) {
			return 0;
		} else {
			return color;
		}
	}
}
