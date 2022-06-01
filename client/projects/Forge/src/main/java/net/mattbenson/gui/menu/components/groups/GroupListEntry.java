package net.mattbenson.gui.menu.components.groups;

import java.awt.Color;

import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.framework.MenuComponent;
import net.mattbenson.gui.framework.MenuPriority;
import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;
import net.mattbenson.gui.menu.IngameMenu;

public class GroupListEntry extends MenuComponent {
	protected String text;
	
	public GroupListEntry(String text, int x, int y, int width, int height) {
		super(x, y, width, height);
		this.text = text.toUpperCase();
		
		setPriority(MenuPriority.LOW);
	}
	
	@Override
	public void onInitColors() {
		setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(255, 255, 255, IngameMenu.MENU_ALPHA));
		
		setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(18, 17, 22, IngameMenu.MENU_ALPHA));
		
		setColor(DrawType.LINE, ButtonState.NORMAL, new Color(48, 47, 49, IngameMenu.MENU_ALPHA));
	}
	
	@Override
	public void onRender() {
		int x = this.getRenderX();
		int y = this.getRenderY();
		int mouseX = parent.getMouseX();
		int width = this.width;
		
		int lineColor = getColor(DrawType.LINE, ButtonState.NORMAL);
		int textColor = getColor(DrawType.TEXT, ButtonState.NORMAL);
		int backgroundColor = getColor(DrawType.BACKGROUND, ButtonState.NORMAL);

		drawRectFalcun(x, y, width, height, backgroundColor);
		
		drawHorizontalLine(x, y, width + 1, 1, lineColor);
		drawVerticalLine(x, y + 1, height - 1, 1, lineColor);
		drawHorizontalLine(x, y + height, width + 1, 1, lineColor);
		drawVerticalLine(x + width, y + 1, height - 1, 1, lineColor);

		drawShadowUp(x, y, width + 1);
		drawShadowLeft(x, y, height + 1);
		drawShadowDown(x, y + height + 1, width + 1);
		drawShadowRight(x + width + 1, y, height + 1);
		
		drawText(text, x + 15, y + height / 2 - getStringHeight(text) / 2, textColor);
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
