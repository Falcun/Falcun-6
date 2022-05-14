package net.mattbenson.gui.menu.components.cosmetics;

import java.awt.Color;

import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;
import net.mattbenson.utils.DrawUtils;

public class CosmeticRainbowButton extends CosmeticGenericButton {
	public CosmeticRainbowButton(String text, int x, int y, int width, int height) {
		super(text, x, y, width, height, true);
	}
	
	@Override
	public void onInitColors() {
		setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(18, 17, 22, 255));
		setColor(DrawType.BACKGROUND, ButtonState.ACTIVE, new Color(44, 44, 48, 255));
		setColor(DrawType.BACKGROUND, ButtonState.HOVER, new Color(28, 28, 31, 255));
		setColor(DrawType.BACKGROUND, ButtonState.HOVERACTIVE, new Color(54, 54, 59, 255));
		setColor(DrawType.BACKGROUND, ButtonState.DISABLED, new Color(100, 100, 100, 255));
		
		setColor(DrawType.LINE, ButtonState.NORMAL, new Color(46, 46, 48, 255));
		setColor(DrawType.LINE, ButtonState.ACTIVE, new Color(63, 63, 66, 255));
		setColor(DrawType.LINE, ButtonState.HOVER, new Color(58, 58, 61, 255));
		setColor(DrawType.LINE, ButtonState.HOVERACTIVE, new Color(76, 76, 79, 255));
		setColor(DrawType.LINE, ButtonState.DISABLED, new Color(100, 100, 100, 255));
		
		setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(255, 255, 255, 255));
		setColor(DrawType.TEXT, ButtonState.ACTIVE, new Color(255, 255, 255, 255));
		setColor(DrawType.TEXT, ButtonState.HOVER, new Color(255, 255, 255, 255));
		setColor(DrawType.TEXT, ButtonState.HOVERACTIVE, new Color(255, 255, 255, 255));
		setColor(DrawType.TEXT, ButtonState.DISABLED, new Color(255, 255, 255, 255));
	}
	
	@Override
	public void onRender() {
		int x = this.getRenderX();
		int y = this.getRenderY();
		int width = (this.width == -1 && this.height == -1) ? (getStringWidth(text) + minOffset * 2) : this.width;
		int height = (this.width == -1 && this.height == -1) ? (getStringHeight(text) + minOffset * 2) : this.height;
		
		int backgroundColor = getColor(DrawType.BACKGROUND, lastState);
		int lineColor = getColor(DrawType.LINE, lastState);
		int textColor = getColor(DrawType.TEXT, lastState);
		
		if(filledBackground) {
			drawRectFalcun(x, y, width, height, backgroundColor);
		}
		
		drawHorizontalLine(x, y, width + 1, 1, lineColor);
		drawVerticalLine(x, y + 1, height - 1, 1, lineColor);
		drawHorizontalLine(x, y + height, width + 1, 1, lineColor);
		drawVerticalLine(x + width, y + 1, height - 1, 1, lineColor);

		drawShadowUp(x, y, width + 1);
		drawShadowLeft(x, y, height + 1);
		drawShadowDown(x, y + height + 1, width + 1);
		drawShadowRight(x + width + 1, y, height + 1);
		
		DrawUtils.drawCustomFontChromaString(Fonts.Roboto, text, x + (width / 2 - getStringWidth(text) / 2), y + (height / 2 - getStringHeight(text) / 2), false, false);
		
		mouseDown = false;
	}
}
