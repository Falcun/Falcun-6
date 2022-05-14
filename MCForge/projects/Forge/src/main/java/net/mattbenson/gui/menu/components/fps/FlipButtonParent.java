package net.mattbenson.gui.menu.components.fps;

import net.mattbenson.gui.framework.MenuPriority;
import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;
import net.mattbenson.gui.menu.components.waypoints.WaypointTextBarrier;

public class FlipButtonParent extends WaypointTextBarrier {

	public FlipButtonParent(String text, int x, int y, int width, int height) {
		super(text, x, y, width, height);
		setPriority(MenuPriority.LOW);
	}

	@Override
	public void onRender() {
		int x = this.getRenderX();
		int y = this.getRenderY();
		
		int backgroundColor = getColor(DrawType.BACKGROUND, ButtonState.NORMAL);
		int lineColor = getColor(DrawType.LINE, ButtonState.NORMAL);
		int textColor = getColor(DrawType.TEXT, ButtonState.NORMAL);
		
		drawRectFalcun(x, y, width, height, backgroundColor);
		
		drawHorizontalLine(x, y, width + 1, 1, lineColor);
		drawVerticalLine(x, y + 1, height - 1, 1, lineColor);
		drawHorizontalLine(x, y + height, width + 1, 1, lineColor);
		drawVerticalLine(x + width, y + 1, height - 1, 1, lineColor);
		
		drawText(text, x + 10, y + height / 2 - getStringHeight(text) / 2, textColor);
	}
}
