package net.mattbenson.modules.types.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.mattbenson.config.ConfigValue;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.render.RenderEvent;
import net.mattbenson.events.types.render.RenderType;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class ChunkBorders extends Module {
	@ConfigValue.Color(name = "Color")
	private Color vColor = Color.WHITE;

	@ConfigValue.Float(name = "Line Width", min = 0.1F, max = 7.5F)
	private float lineWidth = 1;

	@ConfigValue.Integer(name = "Radius", min = 0, max = 6)
	private int radius = 3;

	@ConfigValue.Integer(name = "Height", min = -10, max = 10)
	private int height = 0;

	@ConfigValue.Integer(name = "Position Y", min = -10, max = 300)
	private int pos = 100;

	@ConfigValue.List(name = "Chunkborder Mode", values = { "Static", "Dynamic", "Vertical" })
	private String mode = "Static";

	@ConfigValue.Boolean(name = "Chroma")
	private boolean isUsingChroma = false;

	public ChunkBorders() {
		super("Chunk Borders", ModuleCategory.RENDER);
	}

	@SubscribeEvent
	public void onWorldRenderLast(RenderEvent event) {
		if (event.getRenderType() != RenderType.WORLD) {
			return;
		}
		
		Color color = vColor;

		if (isUsingChroma) {
			float hue = System.currentTimeMillis() % 20000L / 20000.0f;
			int chroma = Color.HSBtoRGB(hue, 1.0f, 1.0f);
			float alpha = (chroma >> 24 & 0xFF) / 255.0f;
			float red = (chroma >> 16 & 0xFF) / 255.0f;
			float green = (chroma >> 8 & 0xFF) / 255.0f;
			float blue = (chroma & 0xFF) / 255.0f;
			color = new Color(red, green, blue, alpha);
		}

		EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().thePlayer;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();

		GL11.glPushMatrix();
		float frame = event.getPartialTicks();
		double inChunkPosX = entityPlayerSP.lastTickPosX + (entityPlayerSP.posX - entityPlayerSP.lastTickPosX) * frame;
		double inChunkPosY = entityPlayerSP.lastTickPosY + (entityPlayerSP.posY - entityPlayerSP.lastTickPosY) * frame;
		double inChunkPosZ = entityPlayerSP.lastTickPosZ + (entityPlayerSP.posZ - entityPlayerSP.lastTickPosZ) * frame;
		inChunkPosY += height;
		GL11.glTranslated(-inChunkPosX, -inChunkPosY, -inChunkPosZ);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(770, 771);
		GL11.glDepthMask(false);
		GL11.glLineWidth(lineWidth);

		GL11.glTranslatef((entityPlayerSP.chunkCoordX * 16), height + 0.01F, (entityPlayerSP.chunkCoordZ * 16));
		double x = 0.0D;
		double z = 0.0D;
		double y = 0.0D;
		worldrenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
		if (mode.equalsIgnoreCase("static") || mode.equalsIgnoreCase("dynamic")) {
			for (int chunkZ = -radius; chunkZ <= radius; chunkZ++) {

				for (int chunkX = -radius; chunkX <= radius; chunkX++) {

					if (Math.abs(chunkX) != 2 || Math.abs(chunkZ) != 2) {
						x = (chunkX * 16);
						z = (chunkZ * 16);
						if (mode.equalsIgnoreCase("static")) {
							y = this.pos;
						}
						if (mode.equalsIgnoreCase("dynamic")) {
							y = inChunkPosY;
						}
						if (vColor.getBlue() == 6 && vColor.getRed() == 6 && vColor.getGreen() == 6) {
							long l = (long) (System.currentTimeMillis() - (x * 10 - z * 10));
							int i = Color.HSBtoRGB(l % (int) 2000.0F / 2000.0F, 0.8F, 0.8F);
							int red = i >> 16;
							int green = i >> 8 & 255;
							int blue = i & 255;
							worldrenderer.pos(x, y, z).color(red, green, blue, 255).endVertex();
							worldrenderer.pos(x + 16, y, z).color(red, green, blue, 255).endVertex();
							worldrenderer.pos(x, y, z).color(red, green, blue, 255).endVertex();
							worldrenderer.pos(x, y, z + 16).color(red, green, blue, 255).endVertex();
							worldrenderer.pos(x + 16, y, z).color(red, green, blue, 255).endVertex();
							worldrenderer.pos(x + 16, y, z + 16).color(red, green, blue, 255).endVertex();
							worldrenderer.pos(x, y, z + 16).color(red, green, blue, 255).endVertex();
							worldrenderer.pos(x + 16, y, z + 16).color(red, green, blue, 255).endVertex();
						} else {
							worldrenderer.pos(x, y, z)
									.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
									.endVertex();
							worldrenderer.pos(x + 16, y, z)
									.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
									.endVertex();
							worldrenderer.pos(x, y, z)
									.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
									.endVertex();
							worldrenderer.pos(x, y, z + 16)
									.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
									.endVertex();
							worldrenderer.pos(x + 16, y, z)
									.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
									.endVertex();
							worldrenderer.pos(x + 16, y, z + 16)
									.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
									.endVertex();
							worldrenderer.pos(x, y, z + 16)
									.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
									.endVertex();
							worldrenderer.pos(x + 16, y, z + 16)
									.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
									.endVertex();
						}
					}
				}
			}
		}
		z = (x = 0.0);
		if (mode.equalsIgnoreCase("vertical")) {

			float greenColourR = vColor.getRed() / 255.0F;
			float greenColourG = vColor.getGreen() / 255.0F;
			float greenColourB = vColor.getBlue() / 255.0F;
			float greenColourA = vColor.getAlpha() / 255.0F;

			final float eyeHeight = (float) (mc.thePlayer.getEyeHeight() + mc.thePlayer.posY);
			final int eyeHeightBlock = (int) Math.floor(eyeHeight);
			final int minY = Math.max(0, eyeHeightBlock - 16);
			final int maxY = Math.min(256, eyeHeightBlock + 16);

			boolean renderedSome = false;
			for (int y2 = 0; y2 < 257; ++y2) {

				greenColourA = 0.4f;
				if (y2 < minY) {
					greenColourA -= (float) (Math.pow(minY - y2, 2.0) / 500.0);
				}
				if (y2 > maxY) {
					greenColourA -= (float) (Math.pow(y2 - maxY, 2.0) / 500.0);
				}
				if (greenColourA < 0.01f) {
					if (renderedSome) {
						break;
					}
				} else {
					if (y2 < 256) {
						for (int n = 1; n < 16; ++n) {
							worldrenderer.pos((double) n, (double) y2, z)
									.color(greenColourR, greenColourG, greenColourB, greenColourA).endVertex();
							worldrenderer.pos((double) n, (double) (y2 + 1), z)
									.color(greenColourR, greenColourG, greenColourB, greenColourA).endVertex();
							worldrenderer.pos(x, (double) y2, (double) n)
									.color(greenColourR, greenColourG, greenColourB, greenColourA).endVertex();
							worldrenderer.pos(x, (double) (y2 + 1), (double) n)
									.color(greenColourR, greenColourG, greenColourB, greenColourA).endVertex();
							worldrenderer.pos((double) n, (double) y2, z + 16.0)
									.color(greenColourR, greenColourG, greenColourB, greenColourA).endVertex();
							worldrenderer.pos((double) n, (double) (y2 + 1), z + 16.0)
									.color(greenColourR, greenColourG, greenColourB, greenColourA).endVertex();
							worldrenderer.pos(x + 16.0, (double) y2, (double) n)
									.color(greenColourR, greenColourG, greenColourB, greenColourA).endVertex();
							worldrenderer.pos(x + 16.0, (double) (y2 + 1), (double) n)
									.color(greenColourR, greenColourG, greenColourB, greenColourA).endVertex();
						}
					}
					worldrenderer.pos(0.0, (double) y2, 0.0)
							.color(greenColourR, greenColourG, greenColourB, greenColourA).endVertex();
					worldrenderer.pos(16.0, (double) y2, 0.0)
							.color(greenColourR, greenColourG, greenColourB, greenColourA).endVertex();
					worldrenderer.pos(0.0, (double) y2, 0.0)
							.color(greenColourR, greenColourG, greenColourB, greenColourA).endVertex();
					worldrenderer.pos(0.0, (double) y2, 16.0)
							.color(greenColourR, greenColourG, greenColourB, greenColourA).endVertex();
					worldrenderer.pos(16.0, (double) y2, 0.0)
							.color(greenColourR, greenColourG, greenColourB, greenColourA).endVertex();
					worldrenderer.pos(16.0, (double) y2, 16.0)
							.color(greenColourR, greenColourG, greenColourB, greenColourA).endVertex();
					worldrenderer.pos(0.0, (double) y2, 16.0)
							.color(greenColourR, greenColourG, greenColourB, greenColourA).endVertex();
					worldrenderer.pos(16.0, (double) y2, 16.0)
							.color(greenColourR, greenColourG, greenColourB, greenColourA).endVertex();
					renderedSome = true;
				}
			}
		}

		tessellator.draw();

		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
}
