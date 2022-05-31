package falcun.net.api.gui;

import falcun.net.api.gui.animations.FalcunSaberSnake;
import falcun.net.api.gui.components.Component;
import falcun.net.api.gui.components.gradient.HorizontalGradient;
import falcun.net.api.gui.components.gradient.VerticalGradient;
import falcun.net.api.gui.components.rect.ColorSquare;
import falcun.net.api.gui.region.GuiRegion;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

public final class GuiUtils {

	public static List<Component> makeBoxShadows(GuiRegion gr, int len) {
		List<Component> gradients = makeBoxVerticalShadows(gr, len);
		gradients.addAll(makeBoxHorizontalShadows(gr, len));
		return gradients;
	}

	public static ColorSquare makeSquare(GuiRegion gr, int color, int curve) {
		return new ColorSquare(gr, () -> color, curve);
	}

	public static ColorSquare makeSquare(GuiRegion gr, int color) {
		return makeSquare(gr, color, 0);
	}

	public static void addSaberSnake(Component comp) {
		comp.effects.add(new FalcunSaberSnake(comp, 0xffff0000));
	}

	public static List<Component> makeBoxHorizontalShadows(GuiRegion gr, int len) {
		List<Component> gradients = new LinkedList<>();
		GuiRegion leftSide = new GuiRegion(gr.x - len, gr.y, len, gr.height);
		HorizontalGradient leftGrad = new HorizontalGradient(leftSide, () -> 0x00000000, () -> 0x80111111);
		GuiRegion rightSide = new GuiRegion(gr.getRight(), gr.y, len, gr.height);
		HorizontalGradient rightGrad = new HorizontalGradient(rightSide, () -> 0x80111111, () -> 0x00000000);
		gradients.add(leftGrad);
		gradients.add(rightGrad);
		return gradients;
	}

	public static List<Component> makeBoxVerticalShadows(GuiRegion gr, int len) {
		List<Component> gradients = new LinkedList<>();
		GuiRegion topSide = new GuiRegion(gr.x, gr.y - len, gr.width, len);
		VerticalGradient topGrad = new VerticalGradient(topSide, () -> 0x00000000, () -> 0x80111111);
		GuiRegion bottomSide = new GuiRegion(gr.x, gr.getBottom(), gr.width, len);
		VerticalGradient bottomGrad = new VerticalGradient(bottomSide, () -> 0x80111111, () -> 0x00000000);
		gradients.add(topGrad);
		gradients.add(bottomGrad);
		return gradients;
	}

	public static void drawNonTextured(BiConsumer<Tessellator, WorldRenderer> callback) {
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		Tessellator tess = Tessellator.getInstance();
		WorldRenderer wr = tess.getWorldRenderer();
		callback.accept(tess, wr);
		GlStateManager.disableBlend();
		GlStateManager.enableTexture2D();
	}

	public static void drawSemiShape(GuiRegion region, int color, int curve, boolean fill, float lineWidth, boolean a, boolean b, boolean c, boolean d) {
		if (lineWidth == 0 && !fill) return;
		drawNonTextured((tess, wr) -> {
//			setColor(color);
//			if (!fill) {
//				GL11.glLineWidth(lineWidth);
//				GL11.glEnable(GL11.GL_LINE_SMOOTH);
//				wr.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
//			} else {
//				wr.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION);
//			}
//			drawSemiRoundedSquare(wr, region, curve, a,b,c,d);
//			tess.draw();
//			if (!fill) {
//				GL11.glDisable(GL11.GL_LINE_SMOOTH);
//				GL11.glLineWidth(1f);
//			}
			setColor(color);
			GL11.glLineWidth(lineWidth);
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			wr.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
			wr.pos(region.x, region.y, 0.0).endVertex();
			wr.pos(region.x, region.getBottom() - curve, 0.0).endVertex();
			addArch(wr, region.x + curve, region.getBottom() - curve, curve, 270, 360);
			wr.pos(region.x + curve, region.getBottom(), 0.0).endVertex();
			wr.pos(region.getRight(), region.getBottom(), 0.0).endVertex();
			tess.draw();
			GL11.glDisable(GL11.GL_LINE_SMOOTH);
			GL11.glLineWidth(1f);
		});
	}

	public static void drawShape(GuiRegion region, int color, int curve, boolean fill, float lineWidth) {
		if (lineWidth == 0 && !fill) return;
		drawNonTextured((tess, wr) -> {
			setColor(color);
			if (!fill) {
				GL11.glLineWidth(lineWidth);
				GL11.glEnable(GL11.GL_LINE_SMOOTH);
				wr.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
			} else {
				wr.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION);
			}
			drawRoundedSquare(wr, region, curve);
			tess.draw();
			if (!fill) {
				GL11.glDisable(GL11.GL_LINE_SMOOTH);
				GL11.glLineWidth(1f);
			}
		});
	}

	public static void drawRightArrow(GuiRegion region, int color, float lineWidth) {
		drawNonTextured((tess, wr) -> {
			setColor(color);
			GL11.glLineWidth(lineWidth);
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			wr.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
			wr.pos(region.x, region.getMidY(), 0).endVertex();
			wr.pos(region.getRight(), region.getMidY(), 0).endVertex();
			wr.pos(region.getMidX(), region.y, 0).endVertex();
			wr.pos(region.getRight(), region.getMidY(), 0).endVertex();
			wr.pos(region.getMidX(), region.getBottom(), 0).endVertex();
			wr.pos(region.getRight(), region.getMidY(), 0).endVertex();
			tess.draw();
			GL11.glDisable(GL11.GL_LINE_SMOOTH);
			GL11.glLineWidth(1f);
		});
	}

	public static void setColor(int color) {
		float colorRed = ((color >> 16) & 0xFF) / 255f;
		float colorGreen = ((color >> 8) & 0xFF) / 255f;
		float colorBlue = (color & 0xFF) / 255f;
		float colorAlpha = ((color >> 24) & 0xFF) / 255f;
		GlStateManager.color(colorRed, colorGreen, colorBlue, colorAlpha);
	}

	public static void drawRoundedSquare(WorldRenderer wr, GuiRegion region, int curve) {
		wr.pos(region.x, region.y + curve, 0.0).endVertex();
		wr.pos(region.x, region.getBottom() - curve, 0.0).endVertex();
		if (curve != 0) addArch(wr, region.x + curve, region.getBottom() - curve, curve, 270, 360);
		wr.pos(region.x + curve, region.getBottom(), 0.0).endVertex();
		wr.pos(region.getRight() - curve, region.getBottom(), 0.0).endVertex();
		if (curve != 0) addArch(wr, region.getRight() - curve, region.getBottom() - curve, curve, 0, 90);
		wr.pos(region.getRight(), region.getBottom() - curve, 0.0).endVertex();
		wr.pos(region.getRight(), region.y + curve, 0.0).endVertex();
		if (curve != 0) addArch(wr, region.getRight() - curve, region.y + curve, curve, 90, 180);
		wr.pos(region.getRight() - curve, region.y, 0.0).endVertex();
		wr.pos(region.x + curve, region.y, 0.0).endVertex();
		if (curve != 0) addArch(wr, region.x + curve, region.y + curve, curve, 180, 270);
	}

	public static void addArch(WorldRenderer wr, double x, double y, int radius, int startAngle, int endAngle) {
		for (int i = startAngle; i < endAngle; i++) {
			wr.pos(x + Math.sin(i * Math.PI / 180) * radius, y + Math.cos(i * Math.PI / 180) * radius, 0.0).endVertex();
		}
	}

	public static void drawQuadGradient(WorldRenderer wr, int x, int y, int w, int h, int left, int right) {
		wr.pos(x, y, 0);
		color(wr, left);
		wr.endVertex();
		wr.pos(x, y + h, 0);
		color(wr, left);
		wr.endVertex();
		wr.pos(x + w, y + h, 0);
		color(wr, right);
		wr.endVertex();
		wr.pos(x + w, y, 0);
		color(wr, right);
		wr.endVertex();
	}

	public static void color(WorldRenderer wr, int color) {
		wr.color(((color >> 16) & 0xff), ((color >> 8) & 0xff), (color & 0xff), (color >> 24));
	}

}
