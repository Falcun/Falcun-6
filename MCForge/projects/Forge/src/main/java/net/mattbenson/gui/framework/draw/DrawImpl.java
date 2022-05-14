package net.mattbenson.gui.framework.draw;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.mattbenson.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public interface DrawImpl {
	static final int SHADOW_SIZE = 5;
	static final int SHADOW_AMOUNT = 3;

	default void drawImage(ResourceLocation image, int x, int y, int width, int height) {
		GlStateManager.color(1, 1, 1);
		GL11.glColor4f(1, 1, 1, 1F);
		drawImageCustomColor(image, x, y, width, height);
	}
	
	default void drawImageCustomColor(ResourceLocation image, int x, int y, int width, int height) {
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		Minecraft.getMinecraft().getTextureManager().bindTexture(image);
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, width, height, width, height);
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
	}
	
	default void drawSkinHead(AbstractClientPlayer player, int x, int y, int dimension, float opacity) {
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
	
	default void drawTexturedModalRect(ResourceLocation resource, int x, int y, int textureX, int textureY, int width, int height, int textureWidth, int textureHeight) {
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		Minecraft.getMinecraft().getTextureManager().bindTexture(resource);
		Gui.drawModalRectWithCustomSizedTexture(x, y, textureX, textureY, width, height, textureWidth, textureHeight);
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
	}
	
	default int getStringHeight(String string){
		return Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
	}
	
	default int getStringWidth(String string){
		return Minecraft.getMinecraft().fontRendererObj.getStringWidth(string);
	}
	
	default void drawRectFalcun(int x, int y, int width, int height, int color){
		GuiScreen.drawRectangle(x, y, x + width, y + height, color);
	}
	
	default void drawText(String text, int x, int y, int color){
		Minecraft.getMinecraft().fontRendererObj.drawString(text, x, y + 1, color);
	}
	
	default void drawTextWithShadow(String text, int x, int y, int color){
		Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, x, y + 1, color);
	}
	
	default void drawVerticalLine(int x, int y, int height, int thickness, int color){
		drawRectFalcun(x, y, thickness, height, color);
	}
	
	default void drawHorizontalLine(int x, int y, int width, int thickness, int color){
		drawRectFalcun(x, y, width, thickness, color);
	}
	
	default void drawPixel(int x, int y, int color) {
		drawRectFalcun(x, y, 1, 1, color);
	}
	
	default void drawRainbowBar(int rainbowX, int rainbowY, int rainbowWidth, int rainbowHeight){
		drawGradientRectSideways(rainbowX, rainbowY, rainbowX + rainbowWidth / 4, rainbowY + rainbowHeight, new Color(45, 135, 166).getRGB(), new Color(103, 93, 161).getRGB());
		drawGradientRectSideways(rainbowX + rainbowWidth / 4, rainbowY, rainbowX + (rainbowWidth / 4) * 2, rainbowY + rainbowHeight, new Color(103, 93, 161).getRGB(), new Color(156, 58, 154).getRGB());
		drawGradientRectSideways(rainbowX + (rainbowWidth / 4) * 2, rainbowY, rainbowX + (rainbowWidth / 4) * 3, rainbowY + rainbowHeight, new Color(156, 58, 154).getRGB(), new Color(156 ,120, 94).getRGB());
		drawGradientRectSideways(rainbowX + (rainbowWidth / 4) * 3, rainbowY, rainbowX + (rainbowWidth / 4) * 4, rainbowY + rainbowHeight, new Color(156 ,120, 94).getRGB(), new Color(156, 173, 45).getRGB());
	}
	
	default void drawGradientRectSideways(int x, int y, int width, int height, int startColor, int endColor) {
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
		GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
		GlStateManager.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		worldRenderer.pos((double)x + width, (double)y + height, (double)0).color(f5, f6, f7, f4).endVertex();
		worldRenderer.pos((double)x + width, (double)y, (double)0).color(f5, f6, f7, f4).endVertex();
		worldRenderer.pos((double)x, (double)y, (double)0).color(f1, f2, f3, f).endVertex();
		worldRenderer.pos((double)x, (double)y + height, (double)0).color(f1, f2, f3, f).endVertex();
		tessellator.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
	}
	
	default void drawGradientRectUpwards(int x, int y, int width, int height, int startColor, int endColor) {
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
		GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
		GlStateManager.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		worldRenderer.pos((double)x + width, (double)y, (double)0).color(f5, f6, f7, f4).endVertex();
		worldRenderer.pos((double)x, (double)y, (double)0).color(f5, f6, f7, f4).endVertex();
		worldRenderer.pos((double)x, (double)y + height, (double)0).color(f1, f2, f3, f).endVertex();
		worldRenderer.pos((double)x + width, (double)y + height, (double)0).color(f1, f2, f3, f).endVertex();
		tessellator.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
	}
	
	default void drawShadowUp(int x, int y, int width) {
		int startColor = 0;
		int endColor = 1342177280;
		
		drawGradientRectUpwards(x, y - DrawImpl.SHADOW_SIZE, width, SHADOW_SIZE, endColor, startColor);
	}
	
	default void drawShadowDown(int x, int y, int width) {
		int startColor = 0;
		int endColor = 1342177280;
		
		drawGradientRectUpwards(x, y, width, SHADOW_SIZE, startColor, endColor);
	}
	
	default void drawShadowLeft(int x, int y, int height) {
		int startColor = 0;
		int endColor = 1342177280;
		
		drawGradientRectSideways(x - DrawImpl.SHADOW_SIZE, y, SHADOW_SIZE, height, startColor, endColor);
	}

	default void drawShadowRight(int x, int y, int height) {
		int startColor = 0;
		int endColor = 1342177280;
		
		drawGradientRectSideways(x, y, SHADOW_SIZE, height, endColor, startColor);
	}

	default void drawTriangle(int x, int y, int width, int height, int pointing, int color) {
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
		GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		GlStateManager.color(f1, f2, f3, f);
		worldRenderer.begin(7, DefaultVertexFormats.POSITION);
		worldRenderer.pos((double)x + (width / 3D), (double)y, (double)0).endVertex();
		worldRenderer.pos((double)x + width, (double)y + height, (double)0).endVertex();
		worldRenderer.pos((double)x - (width / 3D), (double)y + height, (double)0).endVertex();
		worldRenderer.pos((double)x + width, (double)y + height, (double)0).endVertex();
		tessellator.draw();
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
		GlStateManager.popMatrix();
	}
	
	default void drawBottomRect(int x, int y, int width, int height, int color) {
		float f = (float)(color >> 24 & 255) / 255.0F;
		float f1 = (float)(color >> 16 & 255) / 255.0F;
		float f2 = (float)(color >> 8 & 255) / 255.0F;
		float f3 = (float)(color & 255) / 255.0F;
		
		GlStateManager.pushMatrix();
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		GlStateManager.color(f1, f2, f3, f);
		worldRenderer.begin(7, DefaultVertexFormats.POSITION);
		worldRenderer.pos((double)x, (double)y + height, 0.0D).endVertex();
		worldRenderer.pos((double)x + width, (double)y + height, 0.0D).endVertex();
		worldRenderer.pos((double)x + width, (double)y, 0.0D).endVertex();
		worldRenderer.pos((double)x + width, (double)y, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
		GlStateManager.popMatrix();
	}

}
