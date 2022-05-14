package net.mattbenson.modules.types.other;

import net.mattbenson.config.ConfigValue;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.render.RenderEvent;
import net.mattbenson.events.types.render.RenderType;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class HealthVignette extends Module {
	@ConfigValue.Integer(name = "Health Threshold", min = 1, max = 10)
	private int threshold = 5;
	
	public final ResourceLocation vignetteTexPath = new ResourceLocation("textures/misc/vignette.png");
	public float prevVignetteBrightness = 1.0F;
	ScaledResolution p_180480_2_ = new ScaledResolution(Minecraft.getMinecraft());
	
	public HealthVignette() {
		super("Low HP Tint", ModuleCategory.OTHER);
	}
	
	@SubscribeEvent
	public void onRender(RenderEvent event) {
		if (event.getRenderType() != RenderType.INGAME_OVERLAY) {
			return;
		}
		
		if(mc.playerController.isInCreativeMode()) {
			return;
		}
		
		int currentHealth = (int) mc.thePlayer.getHealth() / 2;
		ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());

		if (currentHealth <= threshold) {

			final float f = 1;
			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			GlStateManager.disableDepth();
			GlStateManager.depthMask(false);
			GlStateManager.tryBlendFuncSeparate(0, 769, 1, 0);
			GlStateManager.color(0.0f, f, f, 1.0f);
			mc.getTextureManager().bindTexture(vignetteTexPath);
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldRenderer = tessellator.getWorldRenderer();
			worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
			worldRenderer.pos(0.0, (double) resolution.getScaledHeight(), -90.0).tex(0.0, 1.0).endVertex();
			worldRenderer.pos((double) resolution.getScaledWidth(), (double) resolution.getScaledHeight(), -90.0)
					.tex(1.0, 1.0).endVertex();
			worldRenderer.pos((double) resolution.getScaledWidth(), 0.0, -90.0).tex(1.0, 0.0).endVertex();
			worldRenderer.pos(0.0, 0.0, -90.0).tex(0.0, 0.0).endVertex();
			tessellator.draw();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			GlStateManager.depthMask(true);
			GlStateManager.enableDepth();
			GlStateManager.disableBlend();
			GlStateManager.popMatrix();
		}
	}
}
