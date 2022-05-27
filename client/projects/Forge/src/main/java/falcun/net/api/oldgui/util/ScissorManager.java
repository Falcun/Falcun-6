package falcun.net.api.oldgui.util;

import falcun.net.Falcun;
import falcun.net.api.oldgui.inheritance.ScalingGui;
import falcun.net.api.oldgui.region.GuiRegion;
import falcun.net.api.oldgui.scaling.FalcunScaling;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class ScissorManager {
	static List<GuiRegion> scissorStack = new ArrayList<>();

	public static void startScissor(GuiRegion toUse) {
		if (scissorStack.size() == 0) {
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
		}
		scissorStack.add(toUse);
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		int relX = (int) (toUse.x / (double) sr.getScaledWidth() * Display.getWidth());
		int relW = (int) (toUse.width / (double) sr.getScaledWidth() * Display.getWidth());
		int relH = (int) (toUse.height / (double) sr.getScaledHeight() * Display.getHeight());
		int relY = (int) (toUse.y / (double) sr.getScaledHeight() * Display.getHeight());
		if (!(Falcun.minecraft.currentScreen instanceof ScalingGui)) {
			float scale = 1f / new FalcunScaling(Falcun.minecraft).getScaleFactor();
			relX = Math.round((float) relX * scale);
			relW = Math.round((float) relW * scale);
			relH = Math.round((float) relH * scale);
			relY = Math.round((float) relY * scale);
		}
		GL11.glPushAttrib(GL11.GL_SCISSOR_BIT);
		if (scissorStack.size() > 1) {
			GuiRegion parentScissor = scissorStack.get(scissorStack.size() - 2);
			if (toUse.x > parentScissor.x && toUse.y > parentScissor.y && toUse.getBottom() < parentScissor.getBottom() && toUse.getRight() < parentScissor.getRight()) {
				GL11.glScissor(relX, Display.getHeight() - (relY + relH) - 2, relW, relH + 2);
			}
		} else {
			GL11.glScissor(relX, Display.getHeight() - (relY + relH) - 2, relW, relH + 2);
		}
	}

	public static void finishScissor() {
		//System.out.println(Thread.currentThread().getStackTrace()[2]);
		scissorStack.remove(scissorStack.size() - 1);
		GL11.glPopAttrib();
		if (scissorStack.size() == 0) {
			GL11.glDisable(GL11.GL_SCISSOR_TEST);
		}
	}

}
