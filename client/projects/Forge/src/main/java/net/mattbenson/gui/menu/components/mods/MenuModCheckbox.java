package net.mattbenson.gui.menu.components.mods;

import java.awt.Color;

import net.mattbenson.gui.framework.components.MenuCheckbox;
import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;

public class MenuModCheckbox extends MenuCheckbox {
	public MenuModCheckbox(int x, int y, int width, int height) {
		super("", x, y, width, height);
		textOffset = 0;
	}

	@Override
	public void onInitColors() {
		super.onInitColors();
		
		setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(0, 0, 0, 0));
		setColor(DrawType.BACKGROUND, ButtonState.ACTIVE, new Color(12, 83, 33, 255));
		setColor(DrawType.BACKGROUND, ButtonState.HOVER, new Color(6, 42, 16, 255));
		setColor(DrawType.BACKGROUND, ButtonState.HOVERACTIVE, new Color(18, 126, 48, 255));
		
		setColor(DrawType.LINE, ButtonState.NORMAL, new Color(43, 43, 43, 255));
		setColor(DrawType.LINE, ButtonState.ACTIVE, new Color(53, 53, 53, 255));
		setColor(DrawType.LINE, ButtonState.HOVER, new Color(48, 48, 48, 255));
		setColor(DrawType.LINE, ButtonState.HOVERACTIVE, new Color(59, 59, 59, 255));
		setColor(DrawType.LINE, ButtonState.DISABLED, new Color(100, 100, 100, 255));
	}
	
	@Override
	public void onRender() {
		int x = this.getRenderX();
		int y = this.getRenderY();
		
		int backgroundColor = getColor(DrawType.BACKGROUND, lastState);
		int lineColor = getColor(DrawType.LINE, lastState);
		
		drawHorizontalLine(x, y, width + 1, 1, lineColor);
		drawVerticalLine(x, y + 1, height - 1, 1, lineColor);
		drawHorizontalLine(x, y + height, width + 1, 1, lineColor);
		drawVerticalLine(x + width, y + 1, height - 1, 1, lineColor);
		
		drawRectFalcun(x + 2, y + 2, width - 3, height - 3, lineColor);
		drawRectFalcun(x + 3, y + 3, width - 5, height - 5, backgroundColor);
		
		drawTooltip();
		
		mouseDown = false;
	}
}
