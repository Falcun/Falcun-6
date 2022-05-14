package net.mattbenson.gui;

import java.awt.Point;

import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.render.RenderEvent;
import net.mattbenson.events.types.render.RenderType;
import net.mattbenson.utils.AssetUtils;
import net.mattbenson.utils.DrawUtils;
import net.mattbenson.utils.InputUtils;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class CustomSplashScreen {
	@SubscribeEvent
	public void onSplashScreenDraw(RenderEvent event) {
		if(event.getRenderType() != RenderType.SPLASH_SCREEN) {
			return;
		}

		Point mouse = InputUtils.getMousePos();
		int[] sizes = InputUtils.getWindowsSize();
		
		int backR = 0;
		int backG = 0;
		int backB = 0;
		int backA = 0;
		
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		worldrenderer.pos(0.0D, (double)sizes[1], 0.0D).tex(0.0D, 0.0D).color(backR, backG, backB, backA).endVertex();
		worldrenderer.pos((double)sizes[0], (double)sizes[1], 0.0D).tex(0.0D, 0.0D).color(backR, backG, backB, backA).endVertex();
		worldrenderer.pos((double)sizes[0], 0.0D, 0.0D).tex(0.0D, 0.0D).color(backR, backG, backB, backA).endVertex();
		worldrenderer.pos(0.0D, 0.0D, 0.0D).tex(0.0D, 0.0D).color(backR, backG, backB, backA).endVertex();
		tessellator.draw();
		
		ResourceLocation logo = AssetUtils.getResource("/falcun/falcunwings.png");
		
		int width = 250;
		int height = 250;
		
		int x = (sizes[0] / 2) - (width / 2);
		int y = (sizes[1] / 2) - (height / 2);
		
		DrawUtils.drawImage(logo, x, y, width, height);
		
		event.setCancelled(true);
	}
}
