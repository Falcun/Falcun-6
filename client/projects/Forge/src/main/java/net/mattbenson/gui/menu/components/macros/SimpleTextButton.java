package net.mattbenson.gui.menu.components.macros;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.framework.TextPattern;
import net.mattbenson.gui.framework.components.MenuButton;
import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;
import net.mattbenson.gui.menu.IngameMenu;
import net.mattbenson.utils.DrawUtils;

public class SimpleTextButton extends MenuButton {
	protected boolean leftColorChange;
	
	public SimpleTextButton(String text, int x, int y, int width, int height, boolean leftColorChange) {
		super(text, x, y, width, height);
		this.leftColorChange = leftColorChange;
	}

	@Override
	public void onInitColors() {
		setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(18, 17, 22, IngameMenu.MENU_ALPHA));
		setColor(DrawType.BACKGROUND, ButtonState.ACTIVE, new Color(18, 17, 22, IngameMenu.MENU_ALPHA));
		setColor(DrawType.BACKGROUND, ButtonState.HOVER, new Color(18, 17, 22, IngameMenu.MENU_ALPHA));
		setColor(DrawType.BACKGROUND, ButtonState.HOVERACTIVE, new Color(18, 17, 22, IngameMenu.MENU_ALPHA));
		setColor(DrawType.BACKGROUND, ButtonState.DISABLED, new Color(255, 255, 255, IngameMenu.MENU_ALPHA));
		
		setColor(DrawType.LINE, ButtonState.NORMAL, new Color(52, 52, 53, IngameMenu.MENU_ALPHA));
		setColor(DrawType.LINE, ButtonState.ACTIVE, new Color(52, 52, 53, IngameMenu.MENU_ALPHA));
		setColor(DrawType.LINE, ButtonState.HOVER, new Color(52, 52, 53, IngameMenu.MENU_ALPHA));
		setColor(DrawType.LINE, ButtonState.HOVERACTIVE, new Color(52, 52, 53, IngameMenu.MENU_ALPHA));
		setColor(DrawType.LINE, ButtonState.DISABLED, new Color(255, 255, 255, IngameMenu.MENU_ALPHA));
		setColor(DrawType.LINE, ButtonState.POPUP, new Color(28, 28, 30, IngameMenu.MENU_ALPHA));
		
		setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(56, 56, 58, IngameMenu.MENU_ALPHA));
		setColor(DrawType.TEXT, ButtonState.ACTIVE, new Color(90, 90, 94, IngameMenu.MENU_ALPHA));
		setColor(DrawType.TEXT, ButtonState.HOVER, new Color(75, 75, 78, IngameMenu.MENU_ALPHA));
		setColor(DrawType.TEXT, ButtonState.HOVERACTIVE, new Color(100, 100, 104, IngameMenu.MENU_ALPHA));
		setColor(DrawType.TEXT, ButtonState.DISABLED, new Color(255, 255, 255, IngameMenu.MENU_ALPHA));
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
		int linePopupColor = getColor(DrawType.LINE, ButtonState.POPUP);
		
		drawRectFalcun(x, y, width, height, backgroundColor);
		
		drawHorizontalLine(x, y, width + 1, 1, lineColor);
		drawVerticalLine(x, y + 1, height - 1, 1, leftColorChange ? linePopupColor : lineColor);
		drawHorizontalLine(x, y + height, width + 1, 1, lineColor);
		drawVerticalLine(x + width, y + 1, height - 1, 1, lineColor);

		drawText(text, x + (width / 2 - getStringWidth(text) / 2) + 2, y + (height / 2 - getStringHeight(text) / 2), textColor);
		
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
