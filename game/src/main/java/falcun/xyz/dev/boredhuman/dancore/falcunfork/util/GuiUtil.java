package falcun.xyz.dev.boredhuman.dancore.falcunfork.util;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.Credits;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.shader.Shader;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.shader.Shaders;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

@Credits("https://github.com/boredhuman/Dancore/blob/master/src/main/java/dev/boredhuman/util/GuiUtil.java")
public final class GuiUtil {

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
