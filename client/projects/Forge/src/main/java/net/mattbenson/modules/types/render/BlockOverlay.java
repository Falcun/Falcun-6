package net.mattbenson.modules.types.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.mattbenson.config.ConfigValue;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.render.RenderEvent;
import net.mattbenson.events.types.render.RenderType;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

public class BlockOverlay extends Module {
	@ConfigValue.List(name = "Mode", values = { "Outline", "Highlight", "Both" })
	private String mode = "Outline";

	@ConfigValue.Color(name = "Outline Color")
	private Color vColor = Color.BLUE;

	@ConfigValue.Color(name = "Highlight Color")
	private Color hColor = Color.BLUE;

	@ConfigValue.Float(name = "Line Width", min = 0.1F, max = 7.5F)
	private float lineWidth = 1;

	@ConfigValue.Boolean(name = "Chroma")
	private boolean isUsingChroma = false;

	public BlockOverlay() {
		super("Block Overlay", ModuleCategory.RENDER);
	}

	@SubscribeEvent
	public void onWorldRenderLast(RenderEvent event) {
		if (event.getRenderType() != RenderType.WORLD) {
			return;
		}

		float f = event.getPartialTicks();
		float px = (float) mc.thePlayer.posX;
		float py = (float) mc.thePlayer.posY;
		float pz = (float) mc.thePlayer.posZ;
		float mx = (float) mc.thePlayer.prevPosX;
		float my = (float) mc.thePlayer.prevPosY;
		float mz = (float) mc.thePlayer.prevPosZ;
		float dx = mx + (px - mx) * f;
		float dy = my + (py - my) * f;
		float dz = mz + (pz - mz) * f;

		MovingObjectPosition mop = mc.objectMouseOver;

		if (mop == null)
			return;

		if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectType.BLOCK) {
			Block block = mc.theWorld.getBlockState(mop.getBlockPos()).getBlock();
			BlockPos blockPos = mop.getBlockPos();

			if (mode.equalsIgnoreCase("Outline") || mode.equalsIgnoreCase("Both")) {
				drawLines(block, blockPos, dx, dy, dz);
			}
			if (mode.equalsIgnoreCase("Highlight") || mode.equalsIgnoreCase("Both")) {
				highlight(block, blockPos);
			}
		}
	}

	private void highlight(Block block, BlockPos pos) {
		float red = hColor.getRed() / 255.0f;
		float green = hColor.getGreen() / 255.0f;
		float blue = hColor.getBlue() / 255.0f;
		float alpha = hColor.getAlpha() / 255.0f;

		if (isUsingChroma) {
			float hue = System.currentTimeMillis() % 20000L / 20000.0f;
			int chroma = Color.HSBtoRGB(hue, 1.0f, 1.0f);
			alpha = (chroma >> 24 & 0xFF) / 255.0f;
			red = (chroma >> 16 & 0xFF) / 255.0f;
			green = (chroma >> 8 & 0xFF) / 255.0f;
			blue = (chroma & 0xFF) / 255.0f;
		}
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

		GL11.glLineWidth(lineWidth);
		GL11.glColor4f(red, green, blue, 0.5F);

		AxisAlignedBB bb = block.getSelectedBoundingBox(mc.theWorld, pos)
				.expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D)
				.offset(-renderPosX, -renderPosY, -renderPosZ);

		drawFilledBoundingBox(bb);
		GL11.glLineWidth(1F);
		GlStateManager.depthMask(true);
		GlStateManager.enableTexture2D();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();

	}

	private void drawLines(Block block, BlockPos b, float px, float py, float pz) {
		float red = vColor.getRed() / 255.0f;
		float green = vColor.getGreen() / 255.0f;
		float blue = vColor.getBlue() / 255.0f;
		float alpha = vColor.getAlpha() / 255.0f;

		if (isUsingChroma) {
			float hue = System.currentTimeMillis() % 20000L / 20000.0f;
			int chroma = Color.HSBtoRGB(hue, 1.0f, 1.0f);
			alpha = (chroma >> 24 & 0xFF) / 255.0f;
			red = (chroma >> 16 & 0xFF) / 255.0f;
			green = (chroma >> 8 & 0xFF) / 255.0f;
			blue = (chroma & 0xFF) / 255.0f;
		}

		double renderPosX = mc.getRenderManager().viewerPosX;
		double renderPosY = mc.getRenderManager().viewerPosY;
		double renderPosZ = mc.getRenderManager().viewerPosZ;

		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		GlStateManager.disableTexture2D();
		GlStateManager.depthMask(false);
		GL11.glLineWidth(lineWidth);
		GL11.glColor4f(red, green, blue, 1F);

		AxisAlignedBB box = block.getSelectedBoundingBox(mc.theWorld, b)
				.expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D)
				.offset(-renderPosX, -renderPosY, -renderPosZ);
		;

		drawSelectionBoundingBox(box);
		GL11.glLineWidth(1F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.depthMask(true);
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

	private void drawSelectionBoundingBox(AxisAlignedBB p_181561_0_) {
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

	private void drawFilledBoundingBox(AxisAlignedBB box) {
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
}
