package falcun.xyz.dev.boredhuman.dancore.falcunfork.util;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.Credits;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.shader.Shader;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.shader.Shaders;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

@Credits("https://github.com/boredhuman/Dancore/blob/master/src/main/java/dev/boredhuman/util/GuiUtil.java")
public final class GuiUtil {

	public static void drawColorCircle(int x, int y, int radius, int color) {
		Shader circleShader = Shaders.getSmoothCircleShader();
		GL20.glUseProgram(circleShader.getProgramID());
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		GL20.glUniform2f(circleShader.getUniformID("center"), x,
			Display.getHeight() - y);
		GL20.glUniform1f(circleShader.getUniformID("radius"), radius);
		GuiUtil.setFeather(circleShader, 0.8f);
		GuiUtil.setShaderColor(circleShader, color);

		GuiUtil.start();

		Tessellator tess = Tessellator.getInstance();
		WorldRenderer wr = tess.getWorldRenderer();
		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		wr.pos(x - radius, y - radius, 0).endVertex();
		wr.pos(x - radius, y + radius, 0).endVertex();
		wr.pos(x + radius, y + radius, 0).endVertex();
		wr.pos(x + radius, y - radius, 0).endVertex();
		tess.draw();

		GuiUtil.finish();
	}

	public static void drawColorCircleOutline(int x, int y, int radius, int color, float lineWidth) {
		Shader circleOutlineShader = Shaders.getCircleOutlineShader();
		GL20.glUseProgram(circleOutlineShader.getProgramID());
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		GL20.glUniform2f(circleOutlineShader.getUniformID("center"), x,
			Display.getHeight() - y);
		GL20.glUniform1f(circleOutlineShader.getUniformID("radius"), radius);
		GL20.glUniform1f(circleOutlineShader.getUniformID("lineWidth"), lineWidth);
		GuiUtil.setFeather(circleOutlineShader, 0.8f);
		GuiUtil.setShaderColor(circleOutlineShader, color);

		GuiUtil.start();

		Tessellator tess = Tessellator.getInstance();
		WorldRenderer wr = tess.getWorldRenderer();
		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		wr.pos(x - radius, y - radius, 0).endVertex();
		wr.pos(x - radius, y + radius, 0).endVertex();
		wr.pos(x + radius, y + radius, 0).endVertex();
		wr.pos(x + radius, y - radius, 0).endVertex();
		tess.draw();

		GuiUtil.finish();
	}

//	public static void drawNeomStream(int left, int top, int width, int height) {
//		Shader quadShader = Shaders.getRoundedQuadShader();
//		GL20.glUseProgram(quadShader.getProgramID());
//		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
//		GL20.glUniform1f(quadShader.getUniformID("curve"), curve);
//		GL20.glUniform4f(quadShader.getUniformID("area"), left,
//			(left + width),
//			Display.getHeight() - ((top + height)),
//			Display.getHeight() - top);
//		GuiUtil.setFeather(quadShader, 0.8f);
//		GuiUtil.setShaderColor(quadShader, color);
//
//		GuiUtil.start();
//
//		Tessellator tess = Tessellator.getInstance();
//		WorldRenderer wr = tess.getWorldRenderer();
//		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
//		wr.pos(left, top, 0).endVertex();
//		wr.pos(left, top + height, 0).endVertex();
//		wr.pos(left + width, top + height, 0).endVertex();
//		wr.pos(left + width, top, 0).endVertex();
//		tess.draw();
//
//		GuiUtil.finish();
//	}

	public static void drawRoundedQuad(int left, int top, int width, int height, int curve, int color) {
		Shader quadShader = Shaders.getRoundedQuadShader();
		GL20.glUseProgram(quadShader.getProgramID());
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		GL20.glUniform1f(quadShader.getUniformID("curve"), curve);
		GL20.glUniform4f(quadShader.getUniformID("area"), left,
			(left + width),
			Display.getHeight() - ((top + height)),
			Display.getHeight() - top);
		GuiUtil.setFeather(quadShader, 0.8f);
		GuiUtil.setShaderColor(quadShader, color);

		GuiUtil.start();

		Tessellator tess = Tessellator.getInstance();
		WorldRenderer wr = tess.getWorldRenderer();
		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		wr.pos(left, top, 0).endVertex();
		wr.pos(left, top + height, 0).endVertex();
		wr.pos(left + width, top + height, 0).endVertex();
		wr.pos(left + width, top, 0).endVertex();
		tess.draw();

		GuiUtil.finish();
	}

	public static void drawRoundedQuadOutline(int left, int top, int width, int height, int curve, int color, float lineWidth) {
		Shader roundedQuadOutlineShader = Shaders.getRoundedQuadOutlineShader();
		GL20.glUseProgram(roundedQuadOutlineShader.getProgramID());
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		GL20.glUniform1f(roundedQuadOutlineShader.getUniformID("curve"), curve);
		GL20.glUniform4f(roundedQuadOutlineShader.getUniformID("area"), left,
			(left + width),
			Display.getHeight() - ((top + height)),
			Display.getHeight() - top);
		GL20.glUniform1f(roundedQuadOutlineShader.getUniformID("lineWidth"), 2f);
		GuiUtil.setFeather(roundedQuadOutlineShader, 0.8f);
		GuiUtil.setShaderColor(roundedQuadOutlineShader, color);

		GuiUtil.start();

		Tessellator tess = Tessellator.getInstance();
		WorldRenderer wr = tess.getWorldRenderer();
		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		wr.pos(left, top, 0).endVertex();
		wr.pos(left, top + height, 0).endVertex();
		wr.pos(left + width, top + height, 0).endVertex();
		wr.pos(left + width, top, 0).endVertex();
		tess.draw();

		GuiUtil.finish();
	}

	public static void start() {
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
	}

	public static void finish() {
		GlStateManager.enableTexture2D();
		GL20.glUseProgram(0);
	}

	public static void setFeather(Shader shader, float feather) {
		GL20.glUniform1f(shader.getUniformID("feather"), feather);
	}

	public static void setShaderColor(Shader shader, int color) {
		GL20.glUniform4f(shader.getUniformID("color"), ((color >> 16) & 0xff) / 255f,
			((color >> 8) & 0xff) / 255f, (color & 0xff) / 255f, ((color >> 24) & 0xff) / 255f);
	}

	public static void drawSquare(int x, int y, int width, int height) {
		Tessellator tess = Tessellator.getInstance();
		WorldRenderer wr = tess.getWorldRenderer();
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		wr.pos(x, y, 0).endVertex();
		wr.pos(x, y + height, 0).endVertex();
		wr.pos(x + width, y + height, 0).endVertex();
		wr.pos(x + width, y, 0).endVertex();
		tess.draw();
		GlStateManager.enableTexture2D();
	}

	public static void drawSquareTextured(int x, int y, int width, int height) {
		Tessellator tess = Tessellator.getInstance();
		WorldRenderer wr = tess.getWorldRenderer();
		GlStateManager.enableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		wr.pos(x, y, 0).endVertex();
		wr.pos(x, y + height, 0).endVertex();
		wr.pos(x + width, y + height, 0).endVertex();
		wr.pos(x + width, y, 0).endVertex();
		tess.draw();
		GlStateManager.disableTexture2D();
		GlStateManager.disableBlend();
	}

	public static void drawTextureSquare(int x, int y, int width, int height) {
		Tessellator tess = Tessellator.getInstance();
		WorldRenderer wr = tess.getWorldRenderer();
		GlStateManager.enableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		wr.pos(x, y, 0).tex(0, 0).endVertex();
		wr.pos(x, y + height, 0).tex(0, 1).endVertex();
		wr.pos(x + width, y + height, 0).tex(1, 1).endVertex();
		wr.pos(x + width, y, 0).tex(1, 0).endVertex();
		tess.draw();
		GlStateManager.disableTexture2D();
	}

	public static void drawTextureSquare(int x, int y, int width, int height, float minU, float maxU, float minV, float maxV) {
		Tessellator tess = Tessellator.getInstance();
		WorldRenderer wr = tess.getWorldRenderer();
		GlStateManager.enableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		wr.pos(x, y, 0).tex(minU, minV).endVertex();
		wr.pos(x, y + height, 0).tex(minU, maxV).endVertex();
		wr.pos(x + width, y + height, 0).tex(maxU, maxV).endVertex();
		wr.pos(x + width, y, 0).tex(maxU, minV).endVertex();
		tess.draw();
		GlStateManager.disableTexture2D();
	}

	public static void doSelectiveBlur(int x, int y, int width, int height) {
		Minecraft mc = Minecraft.getMinecraft();
		if (!OpenGlHelper.isFramebufferEnabled()) {
			return;
		}
		Shader blurShader = Shaders.getBlurShader();
		GL20.glUseProgram(blurShader.getProgramID());
		GlStateManager.setActiveTexture(GL13.GL_TEXTURE0);
		GlStateManager.bindTexture(mc.getFramebuffer().framebufferTexture);
		GL20.glUniform1i(blurShader.getUniformID("tex"), 0);
		GL20.glUniform2f(blurShader.getUniformID("resolution"), mc.displayWidth, mc.displayHeight);
		GL20.glUniform1i(blurShader.getUniformID("radius"), 3);

		GlStateManager.enableTexture2D();
		GlStateManager.color(1f, 1f, 1f, 1f);

		Tessellator tess = Tessellator.getInstance();
		WorldRenderer wr = tess.getWorldRenderer();
		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		wr.pos(x, y, 0).endVertex();
		wr.pos(x, y + height, 0).endVertex();
		wr.pos(x + width, y + height, 0).endVertex();
		wr.pos(x + width, y, 0).endVertex();
		tess.draw();
		GlStateManager.disableBlend();
		GL20.glUseProgram(0);
	}

	public static void doBlur(int pass) {
		Minecraft mc = Minecraft.getMinecraft();
		if (!OpenGlHelper.isFramebufferEnabled()) {
			return;
		}
		Shader blurShader = Shaders.getBlurShader();
		GL20.glUseProgram(blurShader.getProgramID());
		GlStateManager.setActiveTexture(GL13.GL_TEXTURE0);
		GlStateManager.bindTexture(mc.getFramebuffer().framebufferTexture);
		GL20.glUniform1i(blurShader.getUniformID("tex"), 0);
		GL20.glUniform2f(blurShader.getUniformID("resolution"), mc.displayWidth, mc.displayHeight);
		GL20.glUniform2f(blurShader.getUniformID("direction"), pass, 0);

		GlStateManager.enableTexture2D();
		GlStateManager.color(1f, 1f, 1f, 1f);

		Tessellator tess = Tessellator.getInstance();
		WorldRenderer wr = tess.getWorldRenderer();
		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		wr.pos(0, 0, 0).endVertex();
		wr.pos(0, mc.displayHeight, 0).endVertex();
		wr.pos(mc.displayWidth, mc.displayHeight, 0).endVertex();
		wr.pos(mc.displayWidth, 0, 0).endVertex();
		tess.draw();
		GlStateManager.disableBlend();

		GL20.glUseProgram(0);
		GL11.glFinish();
	}
}
