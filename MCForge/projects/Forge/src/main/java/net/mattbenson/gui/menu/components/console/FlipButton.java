package net.mattbenson.gui.menu.components.console;

import java.awt.Color;

import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.framework.components.MenuButton;
import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;
import net.mattbenson.gui.menu.IngameMenu;

public class FlipButton extends MenuButton {
	public static final int NORMAL_ON = new Color(15, 58, 28, IngameMenu.MENU_ALPHA).getRGB();
	public static final int NORMAL_OFF = new Color(58, 15, 17, IngameMenu.MENU_ALPHA).getRGB();
	
	public static final int HOVER_ON = new Color(23, 92, 44, IngameMenu.MENU_ALPHA).getRGB();
	public static final int HOVER_OFF = new Color(94, 25, 28, IngameMenu.MENU_ALPHA).getRGB();
	
	public FlipButton(int x, int y, int width, int height) {
		super("", x, y, width, height);
	}

	@Override
	public void onInitColors() {
		setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(18, 17, 22, IngameMenu.MENU_ALPHA));
		setColor(DrawType.BACKGROUND, ButtonState.ACTIVE, new Color(18, 17, 22, IngameMenu.MENU_ALPHA));
		setColor(DrawType.BACKGROUND, ButtonState.HOVER, new Color(18, 17, 22, IngameMenu.MENU_ALPHA));
		setColor(DrawType.BACKGROUND, ButtonState.HOVERACTIVE, new Color(18, 17, 22, IngameMenu.MENU_ALPHA));
		setColor(DrawType.BACKGROUND, ButtonState.DISABLED, new Color(255, 255, 255, IngameMenu.MENU_ALPHA));
		setColor(DrawType.BACKGROUND, ButtonState.POPUP, new Color(18, 17, 22, IngameMenu.MENU_ALPHA));
		
		setColor(DrawType.LINE, ButtonState.NORMAL, new Color(52, 52, 53, IngameMenu.MENU_ALPHA));
		setColor(DrawType.LINE, ButtonState.ACTIVE, new Color(52, 52, 53, IngameMenu.MENU_ALPHA));
		setColor(DrawType.LINE, ButtonState.HOVER, new Color(52, 52, 53, IngameMenu.MENU_ALPHA));
		setColor(DrawType.LINE, ButtonState.HOVERACTIVE, new Color(52, 52, 53, IngameMenu.MENU_ALPHA));
		setColor(DrawType.LINE, ButtonState.DISABLED, new Color(255, 255, 255, IngameMenu.MENU_ALPHA));
		setColor(DrawType.LINE, ButtonState.POPUP, new Color(28, 28, 30, IngameMenu.MENU_ALPHA));
		
		setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(200, 200, 200, 255));
		setColor(DrawType.TEXT, ButtonState.ACTIVE, new Color(235, 235, 235, 255));
		setColor(DrawType.TEXT, ButtonState.HOVER, new Color(225, 225, 225, 255));
		setColor(DrawType.TEXT, ButtonState.HOVERACTIVE, new Color(235, 235, 235, 255));
		setColor(DrawType.TEXT, ButtonState.DISABLED, new Color(255, 255, 255, 255));
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
		
		drawRectFalcun(x, y, width, height, backgroundColor);
		
		drawHorizontalLine(x, y, width + 1, 1, lineColor);
		drawVerticalLine(x, y + 1, height - 1, 1, linePopupColor);
		drawHorizontalLine(x, y + height, width + 1, 1, lineColor);
		drawVerticalLine(x + width, y + 1, height - 1, 1, linePopupColor);
		
		x += 10;
		width -= 20;
		
		y += 5;
		height -= 10;
		
		drawHorizontalLine(x, y, width + 1, 1, linePopupColor);
		drawVerticalLine(x, y + 1, height - 1, 1, linePopupColor);
		drawHorizontalLine(x, y + height, width + 1, 1, linePopupColor);
		drawVerticalLine(x + width, y + 1, height - 1, 1, linePopupColor);
		
		int color = NORMAL_ON;
		
		if(lastState == ButtonState.HOVER || lastState == ButtonState.HOVERACTIVE) {
			color = HOVER_ON;
		}
		
		x += 1;
		y += 1;
		width -= 1;
		height -= 1;
		
		drawRectFalcun(x, y, width, height, color);
		
		Fonts.Roboto.drawString("START", x + 3, y + 2, IngameMenu.MENU_HEADER_TEXT_COLOR);
		
		
		mouseDown = false;
	}
	
	@Override
	public void drawText(String string, int x, int y, int color) {
		Fonts.Roboto.drawString(string, x, y, color);
	}
	
	@Override
	public int getStringWidth(String string) {
		return Fonts.Roboto.getStringWidth(string);
	}
	
	@Override
	public int getStringHeight(String string) {
		return Fonts.Roboto.getStringHeight(string);
	}
}
