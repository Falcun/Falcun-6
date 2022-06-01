package net.mattbenson.gui.menu.components.waypoints;

import java.awt.Color;

import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.framework.MenuComponent;
import net.mattbenson.gui.framework.MenuPriority;
import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;

public class WaypointTextBarrier extends MenuComponent {
	protected String text;
	
	public WaypointTextBarrier(String text, int x, int y, int width, int height) {
		super(x, y, width, height);
		this.text = text;
		setPriority(MenuPriority.HIGHEST);
	}
	
	@Override
	public void onInitColors() {
		super.onInitColors();
		
		setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(18, 17, 22, 255));
		setColor(DrawType.LINE, ButtonState.NORMAL, new Color(66, 66, 68, 255));
		setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(255, 255, 255, 255));
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
		
		drawText(text + "", x + width / 2 - getStringWidth(text) / 2 + 2, y + height / 2 - getStringHeight(text) / 2, textColor);
	}
	
	@Override
	public void drawText(String text, int x, int y, int color) {
		Fonts.Roboto.drawString(text, x, y, color);
	}
	
	@Override
	public int getStringWidth(String text) {
		return Fonts.Roboto.getStringWidth(text);
	}
	
	@Override
	public int getStringHeight(String text) {
		return Fonts.Roboto.getStringHeight(text);
	}
	
	public String getText() {
		return text;
	}
}
