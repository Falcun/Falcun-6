package net.mattbenson.modules.types.render;

import java.awt.Color;
import java.util.LinkedList;
import java.util.Queue;

import org.lwjgl.opengl.GL11;

import net.mattbenson.config.ConfigValue;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.input.MouseDownEvent;
import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.hud.HUDElement;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.mattbenson.utils.ClickCounter;
import net.mattbenson.utils.DrawUtils;

public class CPS extends Module {
	private final ClickCounter leftClickCounter = new ClickCounter();
	private final ClickCounter rightClickCounter = new ClickCounter();
	
	@ConfigValue.Boolean(name = "Right Click Counter")
	private boolean rightClick = false;
	
	@ConfigValue.Color(name = "Color")
	private Color color = Color.WHITE;
	
	@ConfigValue.Boolean(name = "Background")
	private boolean backGround = true;
	
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
	
	public CPS() {
		super("CPS", ModuleCategory.RENDER);
		
		hud = new HUDElement("cps", width, height) {
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
		
		String string = leftClickCounter.getCps() + (rightClick ? " | " + this.rightClickCounter.getCps() : "") + " CPS";
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
	
	@SubscribeEvent
	public void onClick(MouseDownEvent e) {
		if(e.getButton() == 0) {
			leftClickCounter.onClick();
		} else if(e.getButton() == 1) {
			rightClickCounter.onClick();
		}
	}
}