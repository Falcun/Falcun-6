package net.mattbenson.gui.menu.components.groups;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.framework.MenuPriority;
import net.mattbenson.gui.framework.components.MenuDropdown;
import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;

public class GroupValueList extends MenuDropdown {
	protected int cursorWidth = 25;
	protected String id;
	
	public GroupValueList(String id, String[] values, int x, int y, int height) {
		super(values, x, y);
		this.id = id;
		this.height = height;
		width += cursorWidth * 2;
		width += getStringWidth(id);
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
		if(parent == null) {
			return;
		}
		
		int x = this.getRenderX();
		int y = this.getRenderY();
		int width = this.width + textOffset;
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
							} else {
								index = values.length - 1;
							}
							
							onAction();
						} else if(mouseX > x + width - cursorWidth - 1) {
							if(index + 1 < values.length) {
								index++;
							} else {
								index = 0;
							}
							
							onAction();
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
		int width = this.width + textOffset + arrowOffset + 1;
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
		drawText("-", x + 3 + (cursorWidth / 2) - getStringWidth("-") / 2, y + height / 2 - getStringHeight("-") / 2, textColor);
		
		if(mouseX < x + width - cursorWidth - 1) {
			backgroundColor = defBg;
		} else {
			backgroundColor = cache;
		}
		
		drawRectFalcun(x + width - cursorWidth, y, cursorWidth, height + 1, popupColor);
		drawRectFalcun(x + width - cursorWidth + 1, y + 1, cursorWidth - 2, height - 1, backgroundColor);
		drawText("+", x + width - cursorWidth + 3 + (cursorWidth / 2) - getStringWidth("+") / 2, y + height / 2 - getStringHeight("+") / 2, textColor);
		
		String text = id + " | " + values[index].toUpperCase();
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
