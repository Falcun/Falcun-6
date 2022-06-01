package net.mattbenson.modules.types.mods;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.mattbenson.config.ConfigValue;
import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.hud.HUDElement;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.mattbenson.utils.AssetUtils;
import net.mattbenson.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class FancyCompass extends Module {
	@ConfigValue.Color(name = "Marker Color")
	private Color colorM = Color.WHITE;
	
	@ConfigValue.Color(name = "Direction Color")
	private Color colorD = Color.WHITE;
	
	@ConfigValue.Boolean(name = "Background")
	private boolean isBackground = false;
	
	@ConfigValue.Color(name = "Background Color")
	private Color background = new Color(0, 0, 0, 100);
	
	private ResourceLocation COMPASS = AssetUtils.getResource("modules/fancycompass-compass.png");
	private ResourceLocation POINTER = AssetUtils.getResource("modules/compass_triangle.png");
	
	private HUDElement hud;
	
	public FancyCompass() {
		super("Fancy Compass", ModuleCategory.MODS);
		
		hud = new HUDElement("compass", 300, 30) {
			@Override
			public void onRender() {
				render();
			}
		};
		
		hud.setX(1);
		hud.setY(175);
		
		addHUD(hud);
	}
	
	private void render() {
		GL11.glPushMatrix();
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
        int k = 1458;
        int kk = hud.getWidth();
        int k2 = 40;
        int i2 = (int)hud.getX();
        int j2 = (int)hud.getY();
        
        float f = mc.thePlayer.rotationYaw + 30;

		
		if(isBackground) {
			DrawUtils.drawGradientRect(hud.getX(), hud.getY(), hud.getX() + 300, hud.getY() + 20, background.getRGB(), background.getRGB());
		}
        
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(COMPASS);
        GL11.glColor4f(colorD.getRed(), colorD.getGreen(), colorD.getBlue(), colorD.getAlpha());
        net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture(i2, j2, f - (float)(k / 2), (float)(k2 / 2), kk, k2 / 2, 360.0F, (float)(k2 / 2));
        GL11.glColor4f(colorM.getRed(),colorM.getBlue(),colorM.getGreen(), colorM.getAlpha());
        Minecraft.getMinecraft().getTextureManager().bindTexture(POINTER);
        net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture(i2 + (hud.getWidth() / 2), j2, 13, 12.0F, 13, 12, 13.0F, 12.0F);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
}
