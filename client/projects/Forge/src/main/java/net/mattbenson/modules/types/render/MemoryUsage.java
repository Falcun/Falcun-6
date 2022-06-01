package net.mattbenson.modules.types.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.mattbenson.config.ConfigValue;
import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.hud.HUDElement;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.mattbenson.utils.DrawUtils;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.windows.nt.GlobalMemory;

public class MemoryUsage extends Module {
	@ConfigValue.Boolean(name = "Percentage", description = "Show memory usage in percentage.")
	private boolean percentage = false;
	
	@ConfigValue.Boolean(name = "Background")
	private boolean backGround = true;
	
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
	
	public MemoryUsage() {
		super("Memory usage", ModuleCategory.RENDER);
		
		hud = new HUDElement("memory usage", width, height) {
			@Override
			public void onRender() {
				render();
			}
		};
		
		hud.setX(1);
		hud.setY(175);
		
		addHUD(hud);
	}
	
	public void render() {
		if (this.mc.gameSettings.showDebugInfo) {
			return;
		}
		
		GL11.glPushMatrix();
		
		/*/
		   SystemInfo systemInfo = new SystemInfo();
	        HardwareAbstractionLayer hardware = systemInfo.getHardware();
	  

	        System.out.println(Long.valueOf(bytesToMb(hardware.getMemory().getTotal())));
		/*/
		
        long i = Runtime.getRuntime().maxMemory();
        long j = Runtime.getRuntime().totalMemory();
        long k = Runtime.getRuntime().freeMemory();
        long l = j - k;
		
        long usedMemory = Long.valueOf(bytesToMb(l));
        long unusedMemory = Long.valueOf(bytesToMb(i));
        long percentage = Long.valueOf(l * 100L / i);
        
		String string = String.format("Memory: %2d%% %03d/%03dMB", new Object[]{percentage, usedMemory, unusedMemory});
		
		if(this.percentage) {
			string = "Memory: " + percentage + "%";
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
				DrawUtils.drawChromaString(string, posX, posY + 3, true, true);
			} else if(isUsingWaveChroma) {
				DrawUtils.drawChromaString(string, posX, posY+ 3, false, true);
			} else {
				mc.fontRendererObj.drawStringWithShadow(string, (float) (posX), (float) posY+ 3, color.getRGB());
			}
		}
		GL11.glColor3f(1, 1, 1);
		GL11.glPopMatrix();
	}

	private static long bytesToMb(long bytes) {
		return bytes / 1024L / 1024L;
	}
}
