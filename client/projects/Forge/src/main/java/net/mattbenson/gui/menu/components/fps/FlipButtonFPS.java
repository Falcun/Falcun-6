package net.mattbenson.gui.menu.components.fps;

import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;
import net.mattbenson.gui.menu.components.macros.FlipButton;
import net.mattbenson.gui.menu.pages.fps.BlacklistModule;

public class FlipButtonFPS extends FlipButton {
	private BlacklistModule module;
	
	public FlipButtonFPS(BlacklistModule module, int x, int y, int width, int height) {
		super(x, y, width, height);
		this.module = module;
	}

	@Override
	public void onRender() {
		int x = this.getRenderX();
		int y = this.getRenderY();
		int width = (this.width == -1 && this.height == -1) ? (getStringWidth(text) + minOffset * 2) : this.width;
		int height = (this.width == -1 && this.height == -1) ? (getStringHeight(text) + minOffset * 2) : this.height;
		
		int linePopupColor = getColor(DrawType.LINE, ButtonState.POPUP);
		
		int backgroundColor = getColor(DrawType.BACKGROUND, lastState);
		int lineColor = getColor(DrawType.LINE, lastState);
		int textColor = getColor(DrawType.TEXT, lastState);
				
		drawHorizontalLine(x, y, width + 1, 1, linePopupColor);
		drawVerticalLine(x, y + 1, height - 1, 1, linePopupColor);
		drawHorizontalLine(x, y + height, width + 1, 1, linePopupColor);
		drawVerticalLine(x + width, y + 1, height - 1, 1, linePopupColor);
		
		int color = active ? NORMAL_ON : NORMAL_OFF;
		
		if(lastState == ButtonState.HOVER || lastState == ButtonState.HOVERACTIVE) {
			color = active ? HOVER_ON : HOVER_OFF;
		}
		
		if(active) {
			drawRectFalcun(x + 1, y + 1, width / 2, height - 1, color);
		} else {
			drawRectFalcun(x + width - width / 2, y + 1, width / 2, height - 1, color);
		}
		
		mouseDown = false;
	}
	
	public BlacklistModule getModule() {
		return module;
	}
	
}
