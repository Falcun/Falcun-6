package net.mattbenson.utils;

import java.awt.Point;
import java.awt.Rectangle;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class InputUtils {
	public InputUtils() {}
	
	public static Point getMousePos() {
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution sr = new ScaledResolution(mc);
		int[] sizes = getWindowsSize();
		
		int x = Mouse.getX() * sizes[0] / mc.displayWidth;
		int y = sizes[1] - Mouse.getY() * sizes[1] / mc.displayHeight - 1;
		
		return new Point(x, y);
	}
	
	public static int[] getWindowsSize() {
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		return new int[] { sr.getScaledWidth(), sr.getScaledHeight() };
	}
}
