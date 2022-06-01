package net.mattbenson.modules.types.mods;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.mattbenson.config.ConfigValue;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.input.KeyDownEvent;
import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.hud.HUDElement;
import net.mattbenson.input.KeybindManager;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.mattbenson.utils.DrawUtils;
import net.mattbenson.utils.Timer;

public class StopWatch extends Module {
	@ConfigValue.Boolean(name = "Background")
	private boolean background = true;
	
	@ConfigValue.Color(name = "Background Color")
	private Color backgroundColor = new Color(0, 0, 0, 150);
	
	@ConfigValue.Boolean(name = "Custom Font")
	private boolean customFont = false;
	
	@ConfigValue.Color(name = "Color")
	private Color color = Color.WHITE;
	
	@ConfigValue.Keybind(name = "Start/Pause Key")
	private int startkeyBind = 0;
	
	@ConfigValue.Keybind(name = "Reset Key")
	private int resetkeyBind = 0;
		
	@ConfigValue.Boolean(name = "Static Chroma")
	private boolean isUsingStaticChroma = false;
		
	@ConfigValue.Boolean(name = "Wave Chroma")
	private boolean isUsingWaveChroma = false;
	
	Timer timer = new Timer();

	public static int state = 0;
	public int seconds = 0;
	
	private HUDElement hud;
	private int width = 56;
	private int height = 18;
	
	public StopWatch() {
		super("Stop Watch", ModuleCategory.MODS);
		
		hud = new HUDElement("stopwatch", width, height) {
			@Override
			public void onRender() {
				renderPlayerCoords();
			}
		};
		
		hud.setX(70);
		hud.setY(175);
		
		addHUD(hud);
	}
	
	@SubscribeEvent
	public void onKeyPress(KeyDownEvent event) {
		
		if(KeybindManager.isInvalidScreen(mc.currentScreen)) {
			return;
		}
		
		if (startkeyBind == 0)
			return;
		if (event.getKey() == startkeyBind) {
			if (state == 0) {
				state = 1;
			} else if (state == 1) {
				state = 2;
			} else if (state == 3) {
				state = 0;
			} else {
				state = 0;
			}
		}
		if (event.getKey() == resetkeyBind) {
			if (Keyboard.getEventKeyState()) {
				seconds = 0;
			}
		}
	}

	/**
	 * Writes the player's Coordinates and Compass Bearing onto the game screen
	 * 
	 */
	private void renderPlayerCoords() {
		if (this.mc.gameSettings.showDebugInfo) {
			return;
		}
		
		int posY = hud.getY() + 2;
		int posX = hud.getX() + 9;
		
		String string = null;

		if (state == 0) {
			string = Keyboard.getKeyName(startkeyBind) + " to Start";
			seconds = 0;
		} else if (state == 1) {
			if (timer.hasReached(1000)) {
				seconds++;
				timer.reset();
			}
			string = calculateTime(seconds) + "";
		} else {
			string = calculateTime(seconds) + "";
		}

		GL11.glPushMatrix();
		int width = hud.getWidth();
		int height = hud.getHeight();
		
		if(background) {
			DrawUtils.drawGradientRect(hud.getX(), hud.getY(), hud.getX() + width, hud.getY() + height, backgroundColor.getRGB(), backgroundColor.getRGB());
		}
		
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
		GL11.glScaled(1, 1, 1);
		GL11.glPopMatrix();
	}

	public int getStringWidth(String text) {
		if (this.customFont) {
			return (int) Fonts.RobotoHUD.getStringWidth(text);
		} else {
			return mc.fontRendererObj.getStringWidth(text);
		}
	}

	public int getStringHeight(String text) {
		if (this.customFont) {
			return (int) Fonts.RobotoHUD.getStringWidth(text);
		} else {
			return mc.fontRendererObj.getStringWidth(text);
		}
	}

	public void drawString(String text, int posX, int posY, Color color) {
		if (this.customFont) {
			if (color.getBlue() == 5 && color.getRed() == 5 && color.getGreen() == 5) {
				DrawUtils.drawCustomFontChromaString(Fonts.RobotoHUD, text, posX, posY - 2, true, true);
			} else if (color.getBlue() == 6 && color.getRed() == 6 && color.getGreen() == 6) {
				DrawUtils.drawCustomFontChromaString(Fonts.RobotoHUD, text, posX, posY - 2, false, true);
			} else {
				Fonts.RobotoHUD.drawString(text, posX, posY - 2, color.getRGB());
			}
		} else {
			if (color.getBlue() == 5 && color.getRed() == 5 && color.getGreen() == 5) {
				DrawUtils.drawChromaString(text, posX + 2, posY + 2, true, true);
			} else if (color.getBlue() == 6 && color.getRed() == 6 && color.getGreen() == 6) {
				DrawUtils.drawChromaString(text, posX + 2, posY + 2, false, true);
			} else {
				mc.fontRendererObj.drawStringWithShadow(text, posX + 2, posY + 2, color.getRGB());
			}
		}
	}

	public static String calculateTime(long seconds) {
		if (seconds <= 60) {
			return "00:" + seconds + "";
		}

		long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
		long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60);

		if (second < 10) {
			return minute + ":0" + second;
		}

		return minute + ":" + second;
	}
}
