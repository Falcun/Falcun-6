package net.mattbenson.gui.menu.components.macros;

import java.awt.Color;

import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.framework.MenuComponent;
import net.mattbenson.gui.framework.MenuPriority;
import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;
import net.mattbenson.gui.menu.IngameMenu;

public class MacroBase extends MenuComponent {
	protected String text;
	protected int spacing = 15;
	protected int textWidth = 165;
	
	public MacroBase(String text, int x, int y, int width, int height) {
		super(x, y, width, height);
		
		StringBuilder builder = new StringBuilder();
		
		for(char character : text.toUpperCase().toCharArray()) {
			if(getStringWidth(builder.toString() + character) > (textWidth - spacing * 2)) {
				break;
			}
			
			builder.append(character);
		}
		
		this.text = builder.toString();
		
		setPriority(MenuPriority.LOW);
	}
	
	@Override
	public void onInitColors() {
		setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(255, 255, 255, IngameMenu.MENU_ALPHA));
		
		setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(18, 17, 22, IngameMenu.MENU_ALPHA));
		
		setColor(DrawType.LINE, ButtonState.NORMAL, new Color(46, 46, 48, IngameMenu.MENU_ALPHA));
		
		setColor(DrawType.LINE, ButtonState.POPUP, new Color(68, 67, 69, IngameMenu.MENU_ALPHA));
	}
	
	@Override
	public void onRender() {
		int x = this.getRenderX();
		int y = this.getRenderY();
		int mouseX = parent.getMouseX();
		int width = this.width;
		
		int linePopupColor = getColor(DrawType.LINE, ButtonState.POPUP);
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
		
		drawHorizontalLine(x, y, textWidth + 1, 1, linePopupColor);
		drawVerticalLine(x, y + 1, height - 1, 1, linePopupColor);
		drawHorizontalLine(x, y + height, textWidth + 1, 1, linePopupColor);
		drawVerticalLine(x + textWidth, y + 1, height - 1, 1, linePopupColor);
		
		drawText(text, x + spacing, y + height / 2 - getStringHeight(text) / 2, textColor);
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
