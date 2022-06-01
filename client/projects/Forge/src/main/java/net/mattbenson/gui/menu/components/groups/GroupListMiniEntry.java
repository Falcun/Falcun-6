package net.mattbenson.gui.menu.components.groups;

import java.awt.Color;

import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.framework.MenuComponent;
import net.mattbenson.gui.framework.MenuPriority;
import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;
import net.mattbenson.gui.menu.IngameMenu;

public class GroupListMiniEntry extends MenuComponent {
	protected String name;
	protected String value;
	
	public GroupListMiniEntry(String name, String value, int x, int y, int width, int height) {
		super(x, y, width, height);
		this.name = name.toUpperCase();
		this.value = value.toUpperCase();
		
		setPriority(MenuPriority.MEDIUM);
	}
	
	@Override
	public void onInitColors() {
		setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(255, 255, 255, IngameMenu.MENU_ALPHA));
		setColor(DrawType.TEXT, ButtonState.POPUP, new Color(112, 112, 113, IngameMenu.MENU_ALPHA));
		
		setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(18, 17, 22, IngameMenu.MENU_ALPHA));
		setColor(DrawType.BACKGROUND, ButtonState.POPUP, new Color(17,17, 19, IngameMenu.MENU_ALPHA));
		
		setColor(DrawType.LINE, ButtonState.NORMAL, new Color(75, 74, 76, IngameMenu.MENU_ALPHA));
		setColor(DrawType.LINE, ButtonState.POPUP, new Color(48, 48, 50, IngameMenu.MENU_ALPHA));
	}
	
	@Override
	public void onRender() {
		int x = this.getRenderX();
		int y = this.getRenderY();
		int mouseX = parent.getMouseX();
		int width = this.width;
		
		int lineColor = getColor(DrawType.LINE, ButtonState.NORMAL);
		int lineValueColor = getColor(DrawType.LINE, ButtonState.POPUP);
		int textColor = getColor(DrawType.TEXT, ButtonState.POPUP);
		int valueTextColor = getColor(DrawType.TEXT, ButtonState.NORMAL);
		int backgroundColor = getColor(DrawType.BACKGROUND, ButtonState.NORMAL);
		int valueBackgroundColor = getColor(DrawType.BACKGROUND, ButtonState.POPUP);
		
		int userWidth = getStringWidth(name) + 9;
		
		drawRectFalcun(x, y, userWidth, height, backgroundColor);
		
		drawHorizontalLine(x, y, userWidth + 1, 1, lineColor);
		drawVerticalLine(x, y + 1, height - 1, 1, lineColor);
		drawHorizontalLine(x, y + height, userWidth + 1, 1, lineColor);
		drawVerticalLine(x + userWidth, y + 1, height - 1, 1, lineColor);
		
		drawText(name, x + userWidth / 2 - getStringWidth(name) / 2 + 2, y + height / 2 - getStringHeight(name) / 2, textColor);
		
		int endPos = width - userWidth;
		
		drawHorizontalLine(x + userWidth + 1, y, endPos, 1, lineValueColor);
		drawHorizontalLine(x + userWidth + 1, y + height, endPos, 1, lineValueColor);
		drawVerticalLine(x + endPos + userWidth, y + 1, height - 1, 1, lineValueColor);
		
		drawText(value, x + userWidth + endPos / 2 - getStringWidth(value) / 2, y + height / 2 - getStringHeight(value) / 2, valueTextColor);
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
