package net.mattbenson.gui.menu.components.groups;

import java.awt.Color;

import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.framework.MenuComponent;
import net.mattbenson.gui.framework.MenuPriority;
import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;
import net.mattbenson.gui.menu.IngameMenu;

public class GroupUserListHeader extends MenuComponent {
	protected String text;
	protected int amount;
	
	public GroupUserListHeader(String text, int amount, int x, int y, int width, int height) {
		super(x, y, width, height);
		
		this.text = text;
		this.amount = amount;
	}
	
	@Override
	public void onInitColors() {
		setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(255, 255, 255, IngameMenu.MENU_ALPHA));
		setColor(DrawType.TEXT, ButtonState.POPUP, new Color(98, 98, 99, IngameMenu.MENU_ALPHA));
		
		setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(18, 17, 22, IngameMenu.MENU_ALPHA));
		
		setColor(DrawType.LINE, ButtonState.NORMAL, new Color(15, 14, 16, IngameMenu.MENU_ALPHA));
	}
	
	@Override
	public void onRender() {
		if(parent == null) {
			return;
		}
		
		int x = this.getRenderX();
		int y = this.getRenderY();
		int mouseX = parent.getMouseX();
		
		int lineColor = getColor(DrawType.LINE, ButtonState.NORMAL);
		int textColor = getColor(DrawType.TEXT, ButtonState.NORMAL);
		int textUsersColor = getColor(DrawType.TEXT, ButtonState.POPUP);
		int backgroundColor = getColor(DrawType.BACKGROUND, ButtonState.NORMAL);

		drawRectFalcun(x, y, width, height, backgroundColor);
		
		drawHorizontalLine(x, y + height, width + 1, 1, lineColor);
		
		drawText(text, x + 30, y + height / 2 - getStringHeight(text) / 2, textColor);
		drawText(amount + "", x + width - 30, y + height / 2 - getStringHeight(amount + "") / 2, textUsersColor);
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
