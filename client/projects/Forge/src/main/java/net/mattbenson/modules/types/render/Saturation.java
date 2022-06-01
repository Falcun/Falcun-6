package net.mattbenson.modules.types.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.mattbenson.config.ConfigValue;
import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.hud.HUDElement;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.mattbenson.utils.DrawUtils;
import net.minecraft.util.FoodStats;

public class Saturation extends Module {
	@ConfigValue.Boolean(name = "Background")
	private boolean backGround = true;
	
	@ConfigValue.Boolean(name = "Just Output")
	private boolean basic = true;
	
	@ConfigValue.Color(name = "Color")
	private Color color = Color.WHITE;
	
	@ConfigValue.Color(name = "Background Color")
	private Color background = new Color(0, 0, 0, 150);
	
	@ConfigValue.Boolean(name = "Custom Font")
	private boolean customFont = false;
	
	@ConfigValue.Boolean(name = "Static Chroma")
	private boolean isUsingStaticChroma = false;
	
	@ConfigValue.Boolean(name = "Wave Chroma")
	private boolean isUsingWaveChroma = false;
	
	private HUDElement hud;
	private int width = 56;
	private int height = 18;

	public Saturation() {
		super("Saturation", ModuleCategory.RENDER);
		
		hud = new HUDElement("saturation", width, height) {
			@Override
			public void onRender() {
				render();
			}
		};
		
		hud.setX(1);
		hud.setY(80);
		
		addHUD(hud);
	}
	
	public void render() {
		if (this.mc.gameSettings.showDebugInfo) {
			return;
		}
		
		GL11.glPushMatrix();
		
        FoodStats stats = mc.thePlayer.getFoodStats();
        String saturation = String.valueOf((Math.round(stats.getSaturationLevel() * 10) / 10F));
        
        String string;
        
        if (basic) { 
            string = saturation;
        } else {
            string = "Saturation: " + saturation;
        } 

        int width = hud.getWidth();
		int height = hud.getHeight();
		
		if(backGround) {
			DrawUtils.drawGradientRect(hud.getX(), hud.getY(), hud.getX() + width, hud.getY() + height, background.getRGB(), background.getRGB());
		}
		
		float posY = hud.getY() + 2;
		float posX = hud.getX() + 9;
		
		if(customFont) {
			hud.setWidth((int)Fonts.RobotoHUD.getStringWidth(string) + 16);
			hud.setHeight((int)Fonts.RobotoHUD.getStringHeight(string) + 7);
			
			if(isUsingStaticChroma) {
				DrawUtils.drawCustomFontChromaString(Fonts.RobotoHUD, string, (int) (posX), (int) posY, true, true);
			} else if(isUsingWaveChroma) {
				DrawUtils.drawCustomFontChromaString(Fonts.RobotoHUD, string, (int) (posX), (int) posY, false, true);
			} else {
				Fonts.RobotoHUD.drawString(string,(int) (posX), (int)posY, color.getRGB());
			}
		} else {
			hud.setWidth(mc.fontRendererObj.getStringWidth(string) + 16);
			hud.setHeight(mc.fontRendererObj.FONT_HEIGHT + 9);
			
			if(isUsingStaticChroma) {
				DrawUtils.drawChromaString(string, posX, posY + 3, true ,true);
			} else if(isUsingWaveChroma) {
				DrawUtils.drawChromaString(string, posX, posY + 3, false ,true);
			} else {
				mc.fontRendererObj.drawStringWithShadow(string, (float) (posX), (float) posY+ 3, color.getRGB());
			}
		}
		GL11.glColor3f(1, 1, 1);
		GL11.glPopMatrix();
	}
}
