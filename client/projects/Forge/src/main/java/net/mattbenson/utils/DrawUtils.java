package net.mattbenson.utils;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glVertex3d;

import java.awt.Color;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import com.github.lunatrius.core.util.vector.Vector3d;

import net.mattbenson.fonts.FalcunFont;
import net.mattbenson.fonts.NahrFont;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

public class DrawUtils {
	public DrawUtils() {}
	
    private static Tessellator tessellator;
    private static WorldRenderer worldRenderer;
    private static RenderManager renderManager;
	
	public static void drawImage(ResourceLocation image, int x, int y, int width, int height) {
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		Minecraft.getMinecraft().getTextureManager().bindTexture(image);
		GlStateManager.color(1, 1, 1);
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, width, height, width, height);
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
	}
	
	public static void drawSkinHead(AbstractClientPlayer player, int x, int y, int dimension, float opacity) {
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		Minecraft.getMinecraft().getTextureManager().bindTexture(player.getLocationSkin());
		GlStateManager.color(1, 1, 1, opacity);
		float multiplier = dimension / 18F;
		Gui.drawModalRectWithCustomSizedTexture(x, y, 19 * multiplier, 19 * multiplier, Math.round(18 * multiplier), Math.round(18 * multiplier), 150 * multiplier, 150 * multiplier);
		Gui.drawModalRectWithCustomSizedTexture(x, y, (19 * 5 * multiplier) - multiplier, 19 * multiplier, Math.round(18 * multiplier), Math.round(18 * multiplier), 150 * multiplier, 150 * multiplier);
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
	}
	
	public static void drawTexturedModalRect(float xCoord, float yCoord, int minU, int minV, int maxU, int maxV) {
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        int zLevel = 0;
        Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer bufferbuilder = tessellator.getWorldRenderer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double)(xCoord + 0.0F), (double)(yCoord + (float)maxV), (double)zLevel).tex((double)((float)(minU + 0) * 0.00390625F), (double)((float)(minV + maxV) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double)(xCoord + (float)maxU), (double)(yCoord + (float)maxV), (double)zLevel).tex((double)((float)(minU + maxU) * 0.00390625F), (double)((float)(minV + maxV) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double)(xCoord + (float)maxU), (double)(yCoord + 0.0F), (double)zLevel).tex((double)((float)(minU + maxU) * 0.00390625F), (double)((float)(minV + 0) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double)(xCoord + 0.0F), (double)(yCoord + 0.0F), (double)zLevel).tex((double)((float)(minU + 0) * 0.00390625F), (double)((float)(minV + 0) * 0.00390625F)).endVertex();
        tessellator.draw();
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
	}
	
	public static int getStringHeight(FontRenderer font, String string) {
		return font.FONT_HEIGHT;
	}
	
	public static int getStringWidth(NahrFont font, String string) {
		return (int)font.getStringWidth(string);
	}

	public static int getStringHeight(FalcunFont font, String string) {
		return (int)font.getStringHeight(string);
	}
	
	public static int getStringWidth(FalcunFont font, String string) {
		return (int)font.getStringWidth(string);
	}

	public static void drawRect(int x, int y, int width, int height, int color) {
		Gui.drawRectangle(x, y, x + width, y + height, color);
	}
	
	public static void drawText(NahrFont font, String text, int x, int y, int color) {
		 font.drawString(text, x, y + 1, color);
	}
	
	public static void drawTextWithShadow(FontRenderer font, String text, int x, int y, int color) {
		font.drawStringWithShadow(text, x, y + 1, color);
	}
	
	public static void drawText(FalcunFont font, String text, int x, int y, int color) {
		 font.drawString(text, x, y + 1, color);
	}
	
	public static void drawVerticalLine(int x, int y, int height, int thickness, int color) {
		drawRect(x, y, thickness, height, color);
	}
	
	public static void drawHorizontalLine(int x, int y, int width, int thickness, int color) {
		drawRect(x, y, width, thickness, color);
	}
	
	public static void drawPixel(int x, int y, int color) {
		drawRect(x, y, 1, 1, color);
	}
	
	public static void drawRainbowBar(int rainbowX, int rainbowY, int rainbowWidth, int rainbowHeight) {
		drawGradientRectSideways(rainbowX, rainbowY, rainbowX + rainbowWidth / 4, rainbowY + rainbowHeight, new Color(45, 135, 166).getRGB(), new Color(103, 93, 161).getRGB());
		drawGradientRectSideways(rainbowX + rainbowWidth / 4, rainbowY, rainbowX + (rainbowWidth / 4) * 2, rainbowY + rainbowHeight, new Color(103, 93, 161).getRGB(), new Color(156, 58, 154).getRGB());
		drawGradientRectSideways(rainbowX + (rainbowWidth / 4) * 2, rainbowY, rainbowX + (rainbowWidth / 4) * 3, rainbowY + rainbowHeight, new Color(156, 58, 154).getRGB(), new Color(156 ,120, 94).getRGB());
		drawGradientRectSideways(rainbowX + (rainbowWidth / 4) * 3, rainbowY, rainbowX + (rainbowWidth / 4) * 4, rainbowY + rainbowHeight, new Color(156 ,120, 94).getRGB(), new Color(156, 173, 45).getRGB());
	}
	
	public static void drawBoundingBox(float x, float y, float z, float width, float height, float depth, Color color) {
		x -= Minecraft.getMinecraft().getRenderManager().renderPosX;
		y -= Minecraft.getMinecraft().getRenderManager().renderPosY;
		z -= Minecraft.getMinecraft().getRenderManager().renderPosZ;
		
		GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
		
		GL11.glBegin(2);
		GL11.glVertex3f(x, y, z);
		GL11.glVertex3f(x + width, y, z);
		GL11.glVertex3f(x + width, y, z + depth);
		GL11.glVertex3f(x, y, z + depth);
		GL11.glVertex3f(x, y + height, z + depth);
		GL11.glVertex3f(x + width, y + height, z + depth);
		GL11.glVertex3f(x + width, y + height, z);
		GL11.glVertex3f(x, y + height, z);
		GL11.glVertex3f(x, y, z);
		GL11.glVertex3f(x, y, z + depth);
		GL11.glVertex3f(x, y + height, z + depth);
		GL11.glVertex3f(x, y + height, z);
		GL11.glVertex3f(x + width, y + height, z);
		GL11.glVertex3f(x + width, y, z);
		GL11.glVertex3f(x + width, y, z + depth);
		GL11.glVertex3f(x + width, y + height, z + depth);
		GL11.glVertex3f(x + width, y + height, z);
		GL11.glVertex3f(x + width, y, z);
		GL11.glEnd();
	}
	
	public static void drawGradientRectSideways(int x, int y, int width, int height, int startColor, int endColor) {
		float f = (float)(startColor >> 24 & 255) / 255.0F;
		float f1 = (float)(startColor >> 16 & 255) / 255.0F;
		float f2 = (float)(startColor >> 8 & 255) / 255.0F;
		float f3 = (float)(startColor & 255) / 255.0F;
		float f4 = (float)(endColor >> 24 & 255) / 255.0F;
		float f5 = (float)(endColor >> 16 & 255) / 255.0F;
		float f6 = (float)(endColor >> 8 & 255) / 255.0F;
		float f7 = (float)(endColor & 255) / 255.0F;
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer bufferbuilder = tessellator.getWorldRenderer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
		bufferbuilder.pos((double)x + width, (double)y + height, (double)0).color(f5, f6, f7, f4).endVertex();
		bufferbuilder.pos((double)x + width, (double)y, (double)0).color(f5, f6, f7, f4).endVertex();
		bufferbuilder.pos((double)x, (double)y, (double)0).color(f1, f2, f3, f).endVertex();
		bufferbuilder.pos((double)x, (double)y + height, (double)0).color(f1, f2, f3, f).endVertex();
		tessellator.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
	}
	
	public static void drawGradientRectUpwards(int x, int y, int width, int height, int startColor, int endColor) {
		float f = (float)(startColor >> 24 & 255) / 255.0F;
		float f1 = (float)(startColor >> 16 & 255) / 255.0F;
		float f2 = (float)(startColor >> 8 & 255) / 255.0F;
		float f3 = (float)(startColor & 255) / 255.0F;
		float f4 = (float)(endColor >> 24 & 255) / 255.0F;
		float f5 = (float)(endColor >> 16 & 255) / 255.0F;
		float f6 = (float)(endColor >> 8 & 255) / 255.0F;
		float f7 = (float)(endColor & 255) / 255.0F;
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer bufferbuilder = tessellator.getWorldRenderer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
		bufferbuilder.pos((double)x + width, (double)y, (double)0).color(f5, f6, f7, f4).endVertex();
		bufferbuilder.pos((double)x, (double)y, (double)0).color(f5, f6, f7, f4).endVertex();
		bufferbuilder.pos((double)x, (double)y + height, (double)0).color(f1, f2, f3, f).endVertex();
		bufferbuilder.pos((double)x + width, (double)y + height, (double)0).color(f1, f2, f3, f).endVertex();
		tessellator.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
	}

	public static void drawTriangle(int x, int y, int width, int height, int pointing, int color) {
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
	
	// Functions under this line is directly ported, with barely or none edits.

	public static int getCenterX() {
		Minecraft minecraft = Minecraft.getMinecraft();
		ScaledResolution scaledresolution = new ScaledResolution(minecraft);
		return minecraft.displayWidth / (2 * scaledresolution.getScaleFactor());
	}
	
	public static int getCenterY() {
		Minecraft minecraft = Minecraft.getMinecraft();
		ScaledResolution scaledresolution = new ScaledResolution(minecraft);
		return minecraft.displayHeight / (2 * scaledresolution.getScaleFactor());
	}
	
	public static void drawChromaString(String string, double x, double y, boolean static_chroma, boolean shadow) {
		Minecraft mc = Minecraft.getMinecraft();
		
		if (static_chroma) {
			int i = Color.HSBtoRGB((float) (System.currentTimeMillis() % 5000L) / 5000.0F, 0.8F, 0.8F);
			mc.fontRendererObj.drawString(string, (float) x, (float) y, i, shadow);
		} else {
			double xTmp = x;
			for (char textChar : string.toCharArray()) {
				long l = (long) (System.currentTimeMillis() - (xTmp * 10 - y * 10));
				int i = Color.HSBtoRGB(l % (int) 2000.0F / 2000.0F, .8F, .8F);
				String tmp = String.valueOf(textChar);
				mc.fontRendererObj.drawString(tmp, (float) xTmp, (float) y, i, shadow);
				xTmp += mc.fontRendererObj.getCharWidth(textChar);
			}
		}
	}
	
	public static void drawCustomFontChromaString(FalcunFont font, String string, int x, int y, boolean static_chroma, boolean shadow) {
		Minecraft mc = Minecraft.getMinecraft();
		if (static_chroma) {
			
			int i = Color.HSBtoRGB((float) (System.currentTimeMillis() % 5000L) / 5000.0F, 0.8F, 0.8F);
			font.drawString(string,  x,  y, i);
		} else {
			int xTmp = x;
			for (char textChar : string.toCharArray()) {
				long l = (long) (System.currentTimeMillis() - (xTmp * 10 - y * 10));
				int i = Color.HSBtoRGB(l % (int) 2000.0F / 2000.0F, 0.8F, 0.8F);
				String tmp = String.valueOf(textChar);
				font.drawString(tmp, xTmp,  y, i);
				xTmp += font.getStringWidth(tmp);
			}
		}
	}
	
	public static void drawGradientRect(double minX, double minY, double maxX, double maxY, int par5, int par6) {
		int zLevel = 0;

		GL11.glPushMatrix();
		final float f = (par5 >> 24 & 255) / 255.0F;
		final float f1 = (par5 >> 16 & 255) / 255.0F;
		final float f2 = (par5 >> 8 & 255) / 255.0F;
		final float f3 = (par5 & 255) / 255.0F;
		final float f4 = (par6 >> 24 & 255) / 255.0F;
		final float f5 = (par6 >> 16 & 255) / 255.0F;
		final float f6 = (par6 >> 8 & 255) / 255.0F;
		final float f7 = (par6 & 255) / 255.0F;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		final WorldRenderer tessellator = Tessellator.getInstance().getWorldRenderer();
		tessellator.begin(7, DefaultVertexFormats.POSITION_COLOR);
		tessellator.pos(maxX, minY, zLevel).color(f1, f2, f3, f).endVertex();
		tessellator.pos(minX, minY, zLevel).color(f1, f2, f3, f).endVertex();
		tessellator.pos(minX, maxY, zLevel).color(f5, f6, f7, f4).endVertex();
		tessellator.pos(maxX, maxY, zLevel).color(f5, f6, f7, f4).endVertex();
		Tessellator.getInstance().draw();
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
	}
	
	public static void drawCircle(float x, float y, double r, int c, float width, boolean filled)
	{
		float red = (float)(c >> 16 & 0xff) / 255F;
		float green = (float)(c >> 8 & 0xff) / 255F;
		float blue = (float)(c & 0xff) / 255F;
		float alpha = (float)(c >> 24 & 0xff) / 255F;
		GL11.glPushMatrix();
		//GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBlendFunc(770, 771);
		GL11.glLineWidth(width);
		GL11.glColor4f(red, green, blue, alpha);
		GL11.glBegin(filled?GL11.GL_TRIANGLE_FAN:GL11.GL_LINE_LOOP);
		for(int i = 0; i <= 360; i++)
		{
			double x2 = Math.sin((i * 3.141526D / 180)) * r;
			double y2 = Math.cos((i * 3.141526D / 180)) * r;
			GL11.glVertex2d(x + x2, y + y2);
		}
		GL11.glEnd();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		//GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	public static void drawRoundedRect(int x0, int y0, int x1, int y1, float radius, int color) {
		int i = 18;
		float f = 90.0F / (float) i;
		float f1 = (float) (color >> 24 & 255) / 255.0F;
		float f2 = (float) (color >> 16 & 255) / 255.0F;
		float f3 = (float) (color >> 8 & 255) / 255.0F;
		float f4 = (float) (color & 255) / 255.0F;
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColor4f(f2, f3, f4, f1);
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glVertex2f((float) x0 + radius, (float) y0);
		GL11.glVertex2f((float) x0 + radius, (float) y1);
		GL11.glVertex2f((float) x1 - radius, (float) y0);
		GL11.glVertex2f((float) x1 - radius, (float) y1);
		GL11.glEnd();
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glVertex2f((float) x0, (float) y0 + radius);
		GL11.glVertex2f((float) x0 + radius, (float) y0 + radius);
		GL11.glVertex2f((float) x0, (float) y1 - radius);
		GL11.glVertex2f((float) x0 + radius, (float) y1 - radius);
		GL11.glEnd();
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glVertex2f((float) x1, (float) y0 + radius);
		GL11.glVertex2f((float) x1 - radius, (float) y0 + radius);
		GL11.glVertex2f((float) x1, (float) y1 - radius);
		GL11.glVertex2f((float) x1 - radius, (float) y1 - radius);
		GL11.glEnd();
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		float f5 = (float) x1 - radius;
		float f6 = (float) y0 + radius;
		GL11.glVertex2f(f5, f6);

		for (int j = 0; j <= i; ++j) {
			float f7 = (float) j * f;
			GL11.glVertex2f((float) ((double) f5 + (double) radius * Math.cos(Math.toRadians((double) f7))),
					(float) ((double) f6 - (double) radius * Math.sin(Math.toRadians((double) f7))));
		}

		GL11.glEnd();
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		f5 = (float) x0 + radius;
		f6 = (float) y0 + radius;
		GL11.glVertex2f(f5, f6);

		for (int k = 0; k <= i; ++k) {
			float f8 = (float) k * f;
			GL11.glVertex2f((float) ((double) f5 - (double) radius * Math.cos(Math.toRadians((double) f8))),
					(float) ((double) f6 - (double) radius * Math.sin(Math.toRadians((double) f8))));
		}

		GL11.glEnd();
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		f5 = (float) x0 + radius;
		f6 = (float) y1 - radius;
		GL11.glVertex2f(f5, f6);

		for (int l = 0; l <= i; ++l) {
			float f9 = (float) l * f;
			GL11.glVertex2f((float) ((double) f5 - (double) radius * Math.cos(Math.toRadians((double) f9))),
					(float) ((double) f6 + (double) radius * Math.sin(Math.toRadians((double) f9))));
		}

		GL11.glEnd();
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		f5 = (float) x1 - radius;
		f6 = (float) y1 - radius;
		GL11.glVertex2f(f5, f6);

		for (int i1 = 0; i1 <= i; ++i1) {
			float f10 = (float) i1 * f;
			GL11.glVertex2f((float) ((double) f5 + (double) radius * Math.cos(Math.toRadians((double) f10))),
					(float) ((double) f6 + (double) radius * Math.sin(Math.toRadians((double) f10))));
		}

		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public static void drawStrip(int x, int y, float width, double angle, float points, float radius, int color) {
		GL11.glPushMatrix();
		float f1 = (float) (color >> 24 & 255) / 255.0F;
		float f2 = (float) (color >> 16 & 255) / 255.0F;
		float f3 = (float) (color >> 8 & 255) / 255.0F;
		float f4 = (float) (color & 255) / 255.0F;
		GL11.glTranslatef(x, y, 0);
		GL11.glColor4f(f2, f3, f4, f1);
		GL11.glLineWidth(width);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
		GL11.glEnable(GL13.GL_MULTISAMPLE);

		if (angle > 0) {
			GL11.glBegin(GL11.GL_LINE_STRIP);

			for (int i = 0; i < angle; i++) {
				float a = (float) (i * (angle * Math.PI / points));
				float xc = (float) (Math.cos(a) * radius);
				float yc = (float) (Math.sin(a) * radius);
				GL11.glVertex2f(xc, yc);
			}

			GL11.glEnd();
		}

		if (angle < 0) {
			GL11.glBegin(GL11.GL_LINE_STRIP);

			for (int i = 0; i > angle; i--) {
				float a = (float) (i * (angle * Math.PI / points));
				float xc = (float) (Math.cos(a) * -radius);
				float yc = (float) (Math.sin(a) * -radius);
				GL11.glVertex2f(xc, yc);
			}

			GL11.glEnd();
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL13.GL_MULTISAMPLE);
		GL11.glDisable(GL11.GL_MAP1_VERTEX_3);
		GL11.glPopMatrix();
	}

	public static void drawSquareTexture(ResourceLocation resourceLocation, float size, float x, float y) {
		GL11.glPushMatrix();
		float width = size * 2;
		float height = size * 2;
		float u = 0, v = 0;
		float uWidth = size, vHeight = size;
		float textureWidth = size, textureHeight = size;
		GL11.glEnable(3042);
		Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
		GL11.glBegin(7);
		GL11.glTexCoord2d(u / textureWidth, v / textureHeight);
		GL11.glVertex2d(x, y);
		GL11.glTexCoord2d(u / textureWidth, (v + vHeight) / textureHeight);
		GL11.glVertex2d(x, y + height);
		GL11.glTexCoord2d((u + uWidth) / textureWidth, (v + vHeight) / textureHeight);
		GL11.glVertex2d(x + width, y + height);
		GL11.glTexCoord2d((u + uWidth) / textureWidth, v / textureHeight);
		GL11.glVertex2d(x + width, y);
		GL11.glEnd();
		GL11.glDisable(3042);
		GL11.glPopMatrix();
	}
	
	public static void displayFilledRectangle(int x1, int y1, int x2, int y2, Color color) {
		GL11.glPushMatrix();
		
		if (x1 < x2) {
			int i = x1;
			x1 = x2;
			x2 = i;
		}
		
		if (y1 < y2) {
			int j = y1;
			y1 = y2;
			y2 = j;
		}
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColor4f((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F,
				(float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f((float) x1, (float) y2);
		GL11.glVertex2f((float) x2, (float) y2);
		GL11.glVertex2f((float) x2, (float) y1);
		GL11.glVertex2f((float) x1, (float) y1);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
	
	public static void tracerLine(float red, float green, float blue, float alpha, float width, float x, float y, float z) {
		Vec3 eyes = new Vec3(0, 0, 1).rotatePitch(-(float) Math.toRadians(Minecraft.getMinecraft().thePlayer.rotationPitch)).rotateYaw(-(float) Math.toRadians(Minecraft.getMinecraft().thePlayer.rotationYaw));
		x += 0.5 - Minecraft.getMinecraft().getRenderManager().viewerPosX;
		y += 0.01 - Minecraft.getMinecraft().getRenderManager().viewerPosY;
		z += 0.5 - Minecraft.getMinecraft().getRenderManager().viewerPosZ;
		glBlendFunc(770, 771);
		glDepthMask(true);
		glEnable(GL_BLEND);
		glLineWidth(width);
		glDisable(GL11.GL_TEXTURE_2D);
		glDisable(GL_DEPTH_TEST);
		GL11.glColor4f(red, green, blue,alpha);
		glBegin(GL_LINES);
		{
			glVertex3d(eyes.xCoord, Minecraft.getMinecraft().thePlayer.getEyeHeight() + eyes.yCoord, eyes.zCoord);
			glVertex3d(x, y, z);
		}
		glEnd();
		glEnable(GL11.GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glDepthMask(true);
		glDisable(GL_BLEND);
	}

	public static void render2DEsp(double pX, double pY, double pZ, float lineWidth, Color color) {

		int red = (int) (color.getRed());
		int green = (int) (color.getGreen());
		int blue = (int) (color.getBlue());
		int alpha = (int) (color.getAlpha());

		double renderPosX = Minecraft.getMinecraft().getRenderManager().viewerPosX;
		double renderPosY = Minecraft.getMinecraft().getRenderManager().viewerPosY;
		double renderPosZ = Minecraft.getMinecraft().getRenderManager().viewerPosZ;

		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		GlStateManager.disableTexture2D();
		GlStateManager.disableDepth();
		GlStateManager.disableAlpha();
		GL11.glLineWidth(lineWidth);
		GL11.glColor4f(red, green, blue, alpha);
		BlockPos pos = new BlockPos(pX,pY,pZ);
		Block bloc = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
		AxisAlignedBB bb = bloc.getSelectedBoundingBox(Minecraft.getMinecraft().theWorld, pos).expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D).offset(-renderPosX, -renderPosY, -renderPosZ);
		drawSelectionBoundingBox(bb);
		GL11.glLineWidth(1F);
		GlStateManager.enableTexture2D();
		GlStateManager.enableDepth();
		GlStateManager.enableAlpha();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}
	

    public static void drawSelectionBoundingBox(AxisAlignedBB p_181561_0_)
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(1, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        tessellator.draw();
    }

	public static void tracerLine(double x, double y, double z, Color color) {
		x += 0.5 - Minecraft.getMinecraft().getRenderManager().renderPosX;
		y += 0.5 - Minecraft.getMinecraft().getRenderManager().renderPosY;
		z += 0.5 - Minecraft.getMinecraft().getRenderManager().renderPosZ;
		glBlendFunc(770, 771);
		glEnable(GL_BLEND);
		glLineWidth(2.0F);
		glDisable(GL11.GL_TEXTURE_2D);
		glDisable(GL_DEPTH_TEST);
		glDepthMask(false);
		setColor(color);
		glBegin(GL_LINES);
		{
			glVertex3d(0, Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0);
			glVertex3d(x, y, z);
		}
		glEnd();
		glEnable(GL11.GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glDepthMask(true);
		glDisable(GL_BLEND);
	}

	public static void setColor(Color c) {
		GL11.glColor4f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
	}

    
    public static AxisAlignedBB normalize(final AxisAlignedBB boundingBoxIn) {
        return new AxisAlignedBB(boundingBoxIn.minX - renderManager.viewerPosX, boundingBoxIn.minY - renderManager.viewerPosY, boundingBoxIn.minZ - renderManager.viewerPosZ, boundingBoxIn.maxX - renderManager.viewerPosX, boundingBoxIn.maxY - renderManager.viewerPosY, boundingBoxIn.maxZ - renderManager.viewerPosZ);
    }
    
    public static Vector3d normalize(final Vector3d vecIn) {
        return new Vector3d(vecIn.x - renderManager.viewerPosX, vecIn.y - renderManager.viewerPosY, vecIn.z - renderManager.viewerPosZ);
    }
    
    public static void setGlColour(final Color colour, final int alpha) {
        GL11.glColor4d(colour.getRed() / 255.0, colour.getGreen() / 255.0, colour.getBlue() / 255.0, alpha / 255.0);
    }

    public static void drawFilledBoundingBox(final AxisAlignedBB box) {
        drawFilledTopFace(box);
        drawFilledBottomFace(box);
        drawFilledNorthFace(box);
        drawFilledSouthFace(box);
        drawFilledEastFace(box);
        drawFilledWestFace(box);
    }
    
    public static void drawFilledTopFace(final AxisAlignedBB box) {
        DrawUtils.worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        DrawUtils.worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        DrawUtils.worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        DrawUtils.worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        DrawUtils.worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        DrawUtils.tessellator.draw();
    }
    
    public static void drawFilledBottomFace(final AxisAlignedBB box) {
        DrawUtils.worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        DrawUtils.worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        DrawUtils.worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        DrawUtils.worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        DrawUtils.worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        DrawUtils.tessellator.draw();
    }
    
    public static void drawFilledNorthFace(final AxisAlignedBB box) {
        DrawUtils.worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        DrawUtils.worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        DrawUtils.worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        DrawUtils.worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        DrawUtils.worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        DrawUtils.tessellator.draw();
    }
    
    public static void drawFilledSouthFace(final AxisAlignedBB box) {
        DrawUtils.worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        DrawUtils.worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        DrawUtils.worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        DrawUtils.worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        DrawUtils.worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        DrawUtils.tessellator.draw();
    }
    
    public static void drawFilledEastFace(final AxisAlignedBB box) {
        DrawUtils.worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        DrawUtils.worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        DrawUtils.worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        DrawUtils.worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        DrawUtils.worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        DrawUtils.tessellator.draw();
    }
    
    public static void drawFilledWestFace(final AxisAlignedBB box) {
        DrawUtils.worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        DrawUtils.worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        DrawUtils.worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        DrawUtils.worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        DrawUtils.worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        DrawUtils.tessellator.draw();
    }

	public static void drawBorderedRect(double x, double y, double x2, double y2, float l1, int col1, int col2) {
		drawRect((float)x, (float)y, (float)x2, (float)y2, col2);

		float f = (float)(col1 >> 24 & 0xFF) / 255F;
		float f1 = (float)(col1 >> 16 & 0xFF) / 255F;
		float f2 = (float)(col1 >> 8 & 0xFF) / 255F;
		float f3 = (float)(col1 & 0xFF) / 255F;
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);

		GL11.glColor4f(f1, f2, f3, f);
		GL11.glLineWidth(l1);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glEnd();

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glPopMatrix();
	}
	
	public static void drawRect(float paramXStart, float paramYStart, float paramXEnd, float paramYEnd, int paramColor)
	{
		float alpha = (float)(paramColor >> 24 & 0xFF) / 255F;
		float red = (float)(paramColor >> 16 & 0xFF) / 255F;
		float green = (float)(paramColor >> 8 & 0xFF) / 255F;
		float blue = (float)(paramColor & 0xFF) / 255F;
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);

		GL11.glColor4f(red, green, blue, alpha);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(paramXEnd, paramYStart);
		GL11.glVertex2d(paramXStart, paramYStart);
		GL11.glVertex2d(paramXStart, paramYEnd);
		GL11.glVertex2d(paramXEnd, paramYEnd);
		GL11.glEnd();

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}
}
