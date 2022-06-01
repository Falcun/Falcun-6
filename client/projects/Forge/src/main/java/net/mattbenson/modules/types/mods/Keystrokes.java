package net.mattbenson.modules.types.mods;

import java.awt.Color;

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

public class Keystrokes extends Module {
	@ConfigValue.Boolean(name = "Show CPS")
	private boolean keystrokescps = true;
	
	@ConfigValue.Boolean(name = "Show Mouse Buttons")
	private boolean keystrokesmouse = true;
	
	@ConfigValue.Boolean(name = "Show Spacebar")
	private boolean spacebar = true;
	
	@ConfigValue.Boolean(name = "Custom Font")
	private boolean isCustomFont = false;
	
	@ConfigValue.Color(name = "Background Color")
	private Color backgroundColor = new Color(0, 0, 0, 150);
	
	@ConfigValue.Color(name = "Key Unpressed Color")
	private Color color = Color.WHITE;
	
	@ConfigValue.Color(name = "Key Pressed Color")
	private Color color2 = Color.BLACK;
	
	@ConfigValue.Boolean(name = "Static Chroma")
	private boolean isUsingStaticChroma = false;
	
	@ConfigValue.Boolean(name = "Wave Chroma")
	private boolean isUsingWaveChroma = false;
	
	private HUDElement hud;
	private int width = 78;
	private int height = 52;
	
	private final ClickCounter leftClickCounter = new ClickCounter();
	private final ClickCounter rightClickCounter = new ClickCounter();
	
	public Keystrokes() {
		super("Key Strokes", ModuleCategory.MODS);
		
		hud = new HUDElement("keystrokes", width, height) {
			@Override
			public void onRender() {
				render();
			}
		};
		
		hud.setX(0);
		hud.setY(120);
		
		addHUD(hud);
	}

	public void render() {
		if (this.mc.gameSettings.showDebugInfo) {
			return;
		}

		GL11.glPushMatrix();
		
		int posX = hud.getX();
		int posY = hud.getY();
		
		int shiftY = 0;
		int shiftX = 26;
		int shiftXFont = 0;

		if (this.isCustomFont) {
			shiftXFont += 2;
		}
		if (mc.gameSettings.keyBindForward.isKeyDown()) {
			DrawUtils.drawGradientRect(posX + shiftX, posY + shiftY, posX + 25 + shiftX, posY + 25 + shiftY,
					getBackGroundColor(true), getBackGroundColor(true));
			GL11.glPushMatrix();
			GL11.glScalef(1.005F, 1.0F, 1.0F);
			drawString("W", (posX + shiftX + 7) / 1.005F, (posY + shiftY + 7) / 1.005F, color2);
			GL11.glScalef(1.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();
		} else {
			DrawUtils.drawGradientRect(posX + shiftX, posY + shiftY, posX + 25 + shiftX, posY + 25 + shiftY,
					getBackGroundColor(false), getBackGroundColor(false));
			GL11.glPushMatrix();
			GL11.glScalef(1.005F, 1.0F, 1.0F);
			drawString("W", (posX + shiftX + 7) / 1.005F, (posY + shiftY + 7) / 1.005F, color);
			GL11.glScalef(1.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();
		}

		shiftX = 0;
		shiftY += 26;
		if (mc.gameSettings.keyBindLeft.isKeyDown()) {
			DrawUtils.drawGradientRect(posX + shiftX, posY + shiftY, posX + 25 + shiftX, posY + 25 + shiftY,
					getBackGroundColor(true), getBackGroundColor(true));
			GL11.glPushMatrix();
			GL11.glScalef(1.005F, 1.0F, 1.0F);
			drawString("A", (posX + shiftX + shiftXFont + 7) / 1.005F, (posY + shiftY + 7) / 1.005F, color2);
			GL11.glScalef(1.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();
		} else {
			DrawUtils.drawGradientRect(posX + shiftX, posY + shiftY, posX + 25 + shiftX, posY + 25 + shiftY,
					getBackGroundColor(false), getBackGroundColor(false));
			GL11.glPushMatrix();
			GL11.glScalef(1.005F, 1.0F, 1.0F);
			drawString("A", (posX + shiftX + shiftXFont + 7) / 1.005F, (posY + shiftY + 7) / 1.005F, color);
			GL11.glScalef(1.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();
		}
		shiftX += 26;
		if (mc.gameSettings.keyBindBack.isKeyDown()) {
			DrawUtils.drawGradientRect(posX + shiftX, posY + shiftY, posX + 25 + shiftX, posY + 25 + shiftY,
					getBackGroundColor(true), getBackGroundColor(true));
			GL11.glPushMatrix();
			GL11.glScalef(1.005F, 1.0F, 1.0F);
			drawString("S", (posX + shiftX + shiftXFont + 7) / 1.005F, (posY + shiftY + 7) / 1.005F, color2);
			GL11.glScalef(1.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();
		} else {
			DrawUtils.drawGradientRect(posX + shiftX, posY + shiftY, posX + 25 + shiftX, posY + 25 + shiftY,
					getBackGroundColor(false), getBackGroundColor(false));
			GL11.glPushMatrix();
			GL11.glScalef(1.005F, 1.0F, 1.0F);
			drawString("S", (posX + shiftX + shiftXFont + 7) / 1.005F, (posY + shiftY + 7) / 1.005F, color);
			GL11.glScalef(1.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();
		}
		shiftX += 26;
		if (mc.gameSettings.keyBindRight.isKeyDown()) {
			DrawUtils.drawGradientRect(posX + shiftX, posY + shiftY, posX + 25 + shiftX, posY + 25 + shiftY,
					getBackGroundColor(true), getBackGroundColor(true));
			GL11.glPushMatrix();
			GL11.glScalef(1.005F, 1.0F, 1.0F);
			drawString("D", (posX + shiftX + shiftXFont + 7) / 1.005F, (posY + shiftY + 7) / 1.005F, color2);
			GL11.glScalef(1.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();
		} else {
			DrawUtils.drawGradientRect(posX + shiftX, posY + shiftY, posX + 25 + shiftX, posY + 25 + shiftY,
					getBackGroundColor(false), getBackGroundColor(false));
			GL11.glPushMatrix();
			GL11.glScalef(1.005F, 1.0F, 1.0F);
			drawString("D", (posX + shiftX + shiftXFont + 7) / 1.005F, (posY + shiftY + 7) / 1.005F, color);
			GL11.glScalef(1.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();
		}
		shiftY += 26;
		
		int mShift = 0;
		int shift = 3;
		
		if(isCustomFont) {
			shift = 6;
			mShift = 1;
		}
		
		if (this.keystrokesmouse) {
			shiftX = 0;
			if (this.keystrokescps) {
				if (mc.gameSettings.keyBindAttack.isKeyDown()) {
					DrawUtils.drawGradientRect(posX + shiftX, posY + shiftY, posX + 38 + shiftX, posY + 25 + shiftY,
							getBackGroundColor(true), getBackGroundColor(true));
					GL11.glPushMatrix();
					drawString("LMB", (posX + mShift + shiftX + 38 / 2 - getStringWidth("LMB") / 2), (posY + shiftY + 3), color2);

					GL11.glScalef(0.75F, 0.75F, 0.75F);
					drawString(leftClickCounter.getCps() + " CPS",
							(posX + shift + shiftX + 38 / 2 - (getStringWidth(leftClickCounter.getCps() + " CPS") / 2)) / 0.75F,
							(posY + shiftY + 15) / 0.75F, color2);
					GL11.glScalef(1.0F, 1.0F, 1.0F);
					GL11.glPopMatrix();
				} else {
					DrawUtils.drawGradientRect(posX + shiftX, posY + shiftY, posX + 38 + shiftX, posY + 25 + shiftY,
							getBackGroundColor(false), getBackGroundColor(false));
					GL11.glPushMatrix();
					drawString("LMB", (posX + mShift + shiftX + 38 / 2 - getStringWidth("LMB") / 2), (posY + shiftY + 3), color);

					GL11.glScalef(0.75F, 0.75F, 0.75F);
					drawString(leftClickCounter.getCps() + " CPS",
							((posX + shift + shiftX + 38 / 2 - getStringWidth(leftClickCounter.getCps() + " CPS") / 2))  / 0.75F,
							(posY + shiftY + 15) / 0.75F, color);
					GL11.glScalef(1.0F, 1.0F, 1.0F);
					GL11.glPopMatrix();
				}

				shiftX += 39;

				if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
					DrawUtils.drawGradientRect(posX + shiftX, posY + shiftY, posX + 38 + shiftX, posY + 25 + shiftY,
							getBackGroundColor(true), getBackGroundColor(true));
					GL11.glPushMatrix();
					drawString("RMB", (posX + mShift + shiftX + 38 / 2 - getStringWidth("RMB") / 2), (posY + shiftY + 3), color2);
					GL11.glScalef(0.75F, 0.75F, 0.75F);
					drawString(rightClickCounter.getCps() + " CPS",
							((posX + shift + shiftX + 38 / 2 - getStringWidth(rightClickCounter.getCps() + " CPS") / 2)) / 0.75F,
							(posY + shiftY + 15) / 0.75F, color2);
					GL11.glScalef(1.0F, 1.0F, 1.0F);

					GL11.glPopMatrix();
				} else {
					GL11.glPushMatrix();
					DrawUtils.drawGradientRect(posX + shiftX, posY + shiftY, posX + 38 + shiftX, posY + 25 + shiftY,
							getBackGroundColor(false), getBackGroundColor(false));
					drawString("RMB", (posX + mShift + shiftX + 38 / 2 - getStringWidth("RMB") / 2), (posY + shiftY + 3), color);

					GL11.glScalef(0.75F, 0.75F, 0.75F);
					drawString(rightClickCounter.getCps() + " CPS",
							((posX + shift + shiftX + 38 / 2 - getStringWidth(rightClickCounter.getCps() + " CPS") / 2)) / 0.75F,
							(posY + shiftY + 15) / 0.75F, color);
					GL11.glScalef(1.0F, 1.0F, 1.0F);

					GL11.glPopMatrix();
				}
				shiftY += 26;
			} else {
				if (mc.gameSettings.keyBindAttack.isKeyDown()) {
					DrawUtils.drawGradientRect(posX + shiftX, posY + shiftY, posX + 38 + shiftX, posY + 25 + shiftY,
							getBackGroundColor(true), getBackGroundColor(true));
					GL11.glPushMatrix();
					GL11.glScalef(1.005F, 1.0F, 1.0F);
					drawString("LMB", (posX + shiftX + 7), (posY + shiftY + 7), color2);
					GL11.glScalef(1.0F, 1.0F, 1.0F);
					GL11.glPopMatrix();
				} else {
					DrawUtils.drawGradientRect(posX + shiftX, posY + shiftY, posX + 38 + shiftX, posY + 25 + shiftY,
							getBackGroundColor(false), getBackGroundColor(false));
					GL11.glPushMatrix();
					GL11.glScalef(1.005F, 1.0F, 1.0F);
					drawString("LMB", (posX + shiftX + 7), (posY + shiftY + 7), color);
					GL11.glScalef(1.0F, 1.0F, 1.0F);
					GL11.glPopMatrix();
				}
				shiftX += 39;
				if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
					DrawUtils.drawGradientRect(posX + shiftX, posY + shiftY, posX + 38 + shiftX, posY + 25 + shiftY,
							getBackGroundColor(true), getBackGroundColor(true));
					GL11.glPushMatrix();
					GL11.glScalef(1.005F, 1.0F, 1.0F);
					drawString("RMB", (posX + shiftX + 7) / 1.005F, (posY + shiftY + 7) / 1.005F, color2);
					GL11.glScalef(1.0F, 1.0F, 1.0F);
					GL11.glPopMatrix();
				} else {
					DrawUtils.drawGradientRect(posX + shiftX, posY + shiftY, posX + 38 + shiftX, posY + 25 + shiftY,
							getBackGroundColor(false), getBackGroundColor(false));
					GL11.glPushMatrix();
					GL11.glScalef(1.005F, 1.0F, 1.0F);
					drawString("RMB", (posX + shiftX + 7) / 1.005F, (posY + shiftY + 7) / 1.005F, color);
					GL11.glScalef(1.0F, 1.0F, 1.0F);
					GL11.glPopMatrix();
				}
				shiftY += 26;
			}
		}

		if (this.spacebar) {
			shiftX = 0;
			if (mc.gameSettings.keyBindJump.isKeyDown()) {
				DrawUtils.drawGradientRect(posX + shiftX, posY + shiftY, posX + 77 + shiftX, posY + 13 + shiftY,
						getBackGroundColor(true), getBackGroundColor(true));
				DrawUtils.drawGradientRect((float) posX + shiftX + 23, (float) posY + shiftY + 4,
						(float) posX + shiftX + 55, (float) posY + shiftY + 5, color2.getRGB(), color2.getRGB());
			} else {
				DrawUtils.drawGradientRect(posX + shiftX, posY + shiftY, posX + 77 + shiftX, posY + 13 + shiftY,
						getBackGroundColor(false), getBackGroundColor(false));
				DrawUtils.drawGradientRect((float) posX + shiftX + 23, (float) posY + shiftY + 4,
						(float) posX + shiftX + 55, (float) posY + shiftY + 5, color.getRGB(), color.getRGB());
			}
			shiftY += 14;
		}

		hud.setHeight(shiftY);

		GL11.glColor3f(1, 1, 1);
		GL11.glScalef(1, 1, 1);
		GL11.glPopMatrix();
	}

	public int getStringWidth(String text) {
		if (this.isCustomFont) {
			return (int) Fonts.RobotoHUD.getStringWidth(text);
		} else {
			return (int) mc.fontRendererObj.getStringWidth(text);
		}
	}

	public int getStringHeight(String text) {
		if (this.isCustomFont) {
			return (int) Fonts.RobotoHUD.getStringWidth(text);
		} else {
			return mc.fontRendererObj.getStringWidth(text);
		}
	}

	public void drawString(String text, float posX, float posY, Color color) {
		if (this.isCustomFont) {
			if (isUsingStaticChroma) {
				DrawUtils.drawCustomFontChromaString(Fonts.RobotoHUD, text, (int)posX, (int)posY - 2, true, true);
			} else if (isUsingWaveChroma) {
				DrawUtils.drawCustomFontChromaString(Fonts.RobotoHUD, text, (int)posX, (int)posY - 2, false, true);
			} else {
				Fonts.RobotoHUD.drawString(text, (int)posX, (int)posY - 2, color.getRGB());
			}
		} else {
			if (isUsingStaticChroma) {
				DrawUtils.drawChromaString(text, (int)posX + 2, (int)posY + 2, true, true);
			} else if (isUsingWaveChroma) {
				DrawUtils.drawChromaString(text, (int)posX + 2, (int)posY + 2, false, true);
			} else {
				mc.fontRendererObj.drawString(text, (float) posX + 2, (float) posY + 2, color.getRGB(), false);
			}
		}
	}

	public int getBackGroundColor(boolean pressed) {
		return backgroundColor.getRGB();
	}

	@SubscribeEvent
	public void onClick(MouseDownEvent e) {
		if (e.getButton() == 0) {
			leftClickCounter.onClick();
		}
		if (e.getButton() == 1) {
			rightClickCounter.onClick();
		}
	}
}
