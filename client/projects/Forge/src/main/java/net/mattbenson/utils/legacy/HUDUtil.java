package net.mattbenson.utils.legacy;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.mattbenson.fonts.NahrFont;
import net.mattbenson.fonts.NahrFont.FontType;
import net.mattbenson.utils.DrawUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class HUDUtil {

	private static Minecraft mc = Minecraft.getMinecraft();

	public static void drawHUD(String message, int x, int y, int width, int height, Color bColor, boolean bG, Color sColor, boolean shadow) {
		if (mc.gameSettings.showDebugInfo) {
			return;
		}

		if(bG) {
			DrawUtils.displayFilledRectangle(x, y, x+width, y+height, bColor);
		}

		int stringWidth = getStringWidth(message);
		if(shadow) {
			drawString(message,  x + (width/2) - (stringWidth/2) , y + (height/2) - (getStringHeight()/2), sColor, true);
		} else {
			drawString(message,  x + (width/2) - (stringWidth/2) , y + (height/2) - (getStringHeight()/2), sColor, false);
		}

	}

	public static void drawString(String message, int x, int y, Color color, boolean shadow) {
		int c = color.getRGB();
		if(color.getRed() == 5 && color.getGreen() == 5 && color.getBlue() == 5) {
			c = Color.HSBtoRGB((float) (System.currentTimeMillis() %  50000L) / 5000.0F, 1F, 1F);
		}
		else if(color.getRed() == 6 && color.getGreen() == 6 && color.getBlue() == 6) {
			double xTmp = x;
			for(char letter : message.toCharArray()) {
				long l = (long) (System.currentTimeMillis() - (xTmp * 10 - y * 10));
				int i = Color.HSBtoRGB(l % (int) 10000.0F / 10000.0F, 1F, 1F);
				String tmp = String.valueOf(letter);
				mc.fontRendererObj.drawString(tmp, (float) xTmp, (float)y, i, shadow);
				xTmp += mc.fontRendererObj.getCharWidth(letter);
			}
			return;
		}

		if(shadow) {
			mc.fontRendererObj.drawString(message, x, y, c, true);
		} else {
			mc.fontRendererObj.drawString(message, x, y, c, false);
		}
	}

	public static void drawString(String message, float x, float y, Color color, boolean shadow) {
		drawString(message, (int) x, (int) y, color, shadow);
	}

	public static void drawString(String message, double x, double y, Color color, boolean shadow) {
		drawString(message, (int) x, (int) y, color, shadow);
	}

	public static void drawHUD(String message, int x, int y, int width, int height, Color bColor, Color sColor) {
		drawHUD(message, x, y, width, height, bColor, true, sColor, true);
	}

	public static void drawHUD(String message, float x, float y, int width, int height, Color bColor, boolean bG, Color sColor, boolean shadow) {
		drawHUD(message, (int)x,(int) y, width, height, bColor, bG, sColor, shadow);
	}
	
	public static void drawHUD(String message, float x, float y, int width, int height, Color bColor, boolean bG, Color sColor, boolean shadow, boolean simple) {
		if(simple) {
			drawString(message, (int) x, (int) y, sColor, shadow);
		} else {
			drawHUD(message, (int)x,(int) y, width, height, bColor, bG, sColor, shadow);
		}
	}
	
	public static void highlight(Block block, BlockPos pos, Color hColor)
	{

		float red = hColor.getRed()/ 255.0f;
		float green = hColor.getGreen()/ 255.0f;
		float blue = hColor.getBlue()/ 255.0f;
		float alpha = hColor.getAlpha()/ 255.0f;


		float bx, by, bz;

		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer vertexBuffer = tessellator.getWorldRenderer();



		double renderPosX = mc.getRenderManager().viewerPosX;
		double renderPosY = mc.getRenderManager().viewerPosY;
		double renderPosZ = mc.getRenderManager().viewerPosZ;

		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		GlStateManager.disableTexture2D();
		GlStateManager.depthMask(false);

		GL11.glLineWidth(1);
		GL11.glColor4f(red, green, blue, 0.5F);


		AxisAlignedBB bb = block.getSelectedBoundingBox(mc.theWorld, pos).expand(0.0020000000949949026D, -0.000000000949949026D, 0.0020000000949949026D).offset(-renderPosX, -renderPosY, -renderPosZ);

		drawFilledBoundingBox(bb);
		GL11.glLineWidth(1F);
		GlStateManager.depthMask(true);
		GlStateManager.enableTexture2D();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();

	}

		
		public static void drawFilledBoundingBox(AxisAlignedBB box) {
			
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldRenderer = tessellator.getWorldRenderer();

			worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
			worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
			worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
			worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
			worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
			worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
			worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
			worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
			worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
			tessellator.draw();

			worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
			worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
			worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
			worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
			worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
			worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
			worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
			worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
			worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
			tessellator.draw();

			worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
			worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
			worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
			worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
			worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
			worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
			worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
			worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
			worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
			tessellator.draw();

			worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
			worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
			worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
			worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
			worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
			worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
			worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
			worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
			worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
			tessellator.draw();

			worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
			worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
			worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
			worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
			worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
			worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
			worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
			worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
			worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
			tessellator.draw();

			worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
			worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
			worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
			worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
			worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
			worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
			worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
			worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
			worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
			tessellator.draw();
		}
		
		

	public static void drawCustomFontString(NahrFont font, String text, float x, float y, Color color, boolean shadow) {
		int c = color.getRGB();
		if(color.getRed() == 5 && color.getGreen() == 5 && color.getBlue() == 5) {
			c = Color.HSBtoRGB((float) (System.currentTimeMillis() %  50000L) / 5000.0F, 1F, 1F);
		}
		else if(color.getRed() == 6 && color.getGreen() == 6 && color.getBlue() == 6) {
			double xTmp = x;
			for(char letter : text.toCharArray()) {
				long l = (long) (System.currentTimeMillis() - (xTmp * 10 - y * 10));
				int i = Color.HSBtoRGB(l % (int) 10000.0F / 10000.0F, 1F, 1F);
				String tmp = String.valueOf(letter);
				if(!shadow) {
					font.drawString(tmp, (float) xTmp, (float)y, i);
				} else {
					font.drawString(tmp, (float) xTmp, (float)y, FontType.SHADOW_THICK,  i, Color.BLACK.getRGB());
				}
				xTmp += font.getStringWidth(letter +"");
			}
			return;
		}

		if(!shadow) {
			font.drawString(text, x, y, c);
		} else {
			font.drawString(text, x, y, FontType.SHADOW_THICK, c, Color.BLACK.getRGB());
		}
	}

	public static void drawCustonFontStringCentered(NahrFont font, String text, float x, float y, Color color, boolean shadow) {
		final int width = (int) font.getStringWidth(text);
		final int height = (int) font.getStringHeight(text);
		final float dX = x - width / 2;
		final float dY = y - height / 2;
		drawCustomFontString(font, text, dX, dY, color, shadow);
	}

	public static void drawTexturedModalRect(int x, int y, int u, int v, int width, int height, float zLevel)
	{
		float var7 = 0.00390625F;
		float var8 = 0.00390625F;
		WorldRenderer tessellator = Tessellator.getInstance().getWorldRenderer();
		tessellator.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		tessellator.pos((x + 0),(y + height),zLevel).tex(((u + 0) * var7), ((v + height) * var8)).endVertex();
		tessellator.pos((x + width), (y + height), zLevel).tex(((u + width) * var7), ((v + height) * var8)).endVertex();
		tessellator.pos((x + width), (y + 0), zLevel).tex(((u + width) * var7), ((v + 0) * var8)).endVertex();
		tessellator.pos((x + 0), (y + 0), zLevel).tex(((u + 0) * var7), ((v + 0) * var8)).endVertex();
		Tessellator.getInstance().draw();
	}

	public static int getStringWidth(String string) {
		return mc.fontRendererObj.getStringWidth(string);
	}

	public static int getStringHeight() {
		return mc.fontRendererObj.FONT_HEIGHT;
	}

}