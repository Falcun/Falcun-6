package net.mattbenson.gui.menu.components.hud;

import java.awt.Color;

import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.framework.MenuPriority;
import net.mattbenson.gui.framework.components.MenuDropdown;
import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;

public class hudList extends MenuDropdown {
	protected int cursorWidth = 25;
	
	public hudList(Class<?> theEnum, int x, int y, int width, int height) {
		super(theEnum, x, y);
		this.height = height;
		this.width = width;
	}
	
	@Override
	public void onInitColors() {
		super.onInitColors();
		
		setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(162, 162, 162, 255));
		setColor(DrawType.TEXT, ButtonState.HOVER, new Color(182, 182, 182, 255));
		
		setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(18, 17, 22, 255));
		setColor(DrawType.BACKGROUND, ButtonState.HOVER, new Color(24, 24, 27, 255));
		
		setColor(DrawType.LINE, ButtonState.NORMAL, new Color(46, 46, 48, 255));
		setColor(DrawType.LINE, ButtonState.HOVER, new Color(53, 53, 55, 255));
		setColor(DrawType.LINE, ButtonState.POPUP, new Color(120, 120, 120, 255));
	}
	
	@Override
	public void onPreSort() {
		int x = this.getRenderX();
		int y = this.getRenderY();
		int width = this.width;
		int mouseX = parent.getMouseX();
		int mouseY = parent.getMouseY();

		ButtonState state = ButtonState.NORMAL;

		if (!disabled) {
			boolean inRange = false;
			
			if (mouseX >= x && mouseX <= x + width + arrowOffset - 1) {
				if (mouseY >= y && mouseY <= y + height + 1) {
					state = ButtonState.HOVER;
					
					if(mouseDown) {
						if(mouseX < x + cursorWidth - 1) {
							if(index - 1 >= 0) {
								index--;
								onAction();
							} else {
								index = values.length - 1;
								onAction();
							}
						} else if(mouseX > x + width - cursorWidth - 1) {
							if(index + 1 < values.length) {
								index++;
								onAction();
							} else {
								index = 0;
								onAction();
							}
						}
					}
				}
			}
		} else {
			state = ButtonState.DISABLED;
		}
		
		if(state == ButtonState.HOVER || state == ButtonState.HOVERACTIVE) {
			setPriority(MenuPriority.HIGH);
		} else {
			setPriority(MenuPriority.MEDIUM);
		}
		
		lastState = state;
	}
	
	@Override
	public void onRender() {
		int x = this.getRenderX();
		int y = this.getRenderY();
		int width = this.width + 1;
		int height = this.height;
		
		int popupColor = getColor(DrawType.LINE, ButtonState.POPUP);
		int backgroundColor = getColor(DrawType.BACKGROUND, lastState);
		int lineColor = getColor(DrawType.LINE, ButtonState.NORMAL);
		int textColor = getColor(DrawType.TEXT, ButtonState.NORMAL);
		
		drawHorizontalLine(x, y, width + 1, 1, lineColor);
		drawVerticalLine(x, y + 1, height - 1, 1, lineColor);
		drawHorizontalLine(x, y + height, width + 1, 1, lineColor);
		drawVerticalLine(x + width, y + 1, height - 1, 1, lineColor);
		
		int mouseX = parent.getMouseX();
		int mouseY = parent.getMouseY();
		int defBg = getColor(DrawType.BACKGROUND, ButtonState.NORMAL);
		int cache = backgroundColor;
		
		if(mouseX > x + cursorWidth - 1) {
			backgroundColor = defBg;
		}
		
		drawRectFalcun(x, y, cursorWidth, height + 1, popupColor);
		drawRectFalcun(x + 1, y + 1, cursorWidth - 2, height - 1, backgroundColor);
		drawText("<", x + 3 + (cursorWidth / 2) - getStringWidth("<") / 2, y + height / 2 - getStringHeight("<") / 2, textColor);
		
		if(mouseX < x + width - cursorWidth - 1) {
			backgroundColor = defBg;
		} else {
			backgroundColor = cache;
		}
		
		drawRectFalcun(x + width - cursorWidth, y, cursorWidth, height + 1, popupColor);
		drawRectFalcun(x + width - cursorWidth + 1, y + 1, cursorWidth - 2, height - 1, backgroundColor);
		drawText(">", x + width - cursorWidth + 3 + (cursorWidth / 2) - getStringWidth(">") / 2, y + height / 2 - getStringHeight(">") / 2, textColor);
		
		String text = values[index].toUpperCase();
		if (text.equals("HUNDRED")) {
			text = "100%";
		} else if (text.equals("NINTY")) {
			text = "90%";
		} else if (text.equals("EIGHTY")) {
			text = "80%";
		} else if (text.equals("SEVENTY")) {
			text = "70%";
		} else if (text.equals("SIXTY")) {
			text = "60%";
		} else if (text.equals("FIFTY")) {
			text = "50%";
		} else if (text.equals("FORTY")) {
			text = "40%";
		} else if (text.equals("THIRTY")) {
			text = "30%";
		} else if (text.equals("TWENTY")) {
			text = "20%";
		} else if (text.equals("TEN")) {
			text = "10%";
		}
		drawText(text, x + width / 2 - getStringWidth(text) / 2, y + height / 2 - getStringHeight(text) / 2, textColor);
		
		mouseDown = false;
	}
	
	@Override
	public void drawText(String text, int x, int y, int color) {
		Fonts.Roboto.drawString(text, x, y, color);
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
