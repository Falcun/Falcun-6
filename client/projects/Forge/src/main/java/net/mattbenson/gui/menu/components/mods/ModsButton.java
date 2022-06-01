package net.mattbenson.gui.menu.components.mods;

import java.awt.Color;

import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.framework.components.MenuButton;
import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;
import net.mattbenson.gui.menu.IngameMenu;
import net.mattbenson.utils.DrawUtils;

public class ModsButton extends MenuButton {
	public ModsButton(String text, int x, int y) {
		super(text, x, y, 120, 20);
	}
	
	@Override
	public void onInitColors() {
		super.onInitColors();
		
		setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(43, 43, 43, 255));
		setColor(DrawType.BACKGROUND, ButtonState.ACTIVE, new Color(68, 68, 68, 255));
		setColor(DrawType.BACKGROUND, ButtonState.HOVER, new Color(58, 58, 58, 255));
		setColor(DrawType.BACKGROUND, ButtonState.HOVERACTIVE, new Color(82, 82, 82, 255));
		setColor(DrawType.BACKGROUND, ButtonState.DISABLED, new Color(150, 150, 150, 255));
		
		setColor(DrawType.LINE, ButtonState.NORMAL, new Color(43, 43, 43, 255));
		setColor(DrawType.LINE, ButtonState.ACTIVE, new Color(68, 68, 68, 255));
		setColor(DrawType.LINE, ButtonState.HOVER, new Color(58, 58, 58, 255));
		setColor(DrawType.LINE, ButtonState.HOVERACTIVE, new Color(82, 82, 82, 255));
		setColor(DrawType.LINE, ButtonState.DISABLED, new Color(150, 150, 150, 255));
		
		setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(0, 200, 0, 255));
		setColor(DrawType.TEXT, ButtonState.ACTIVE, new Color(200, 0, 0, 255));
		setColor(DrawType.TEXT, ButtonState.HOVER, new Color(0, 255, 0, 255));
		setColor(DrawType.TEXT, ButtonState.HOVERACTIVE, new Color(255, 0, 0, 255));
		setColor(DrawType.TEXT, ButtonState.DISABLED, new Color(150, 150, 150, 255));
	}
	
	@Override
	public boolean passesThrough() {
		if(disabled) {
			return true;
		}
		
		int width = (this.width == -1 && this.height == -1) ? (getStringWidth(text) + minOffset * 2) : this.width;
		int height = (this.width == -1 && this.height == -1) ? (getStringHeight(text) + minOffset * 2) : this.height;
		
		int x = this.getRenderX();
		int y = this.getRenderY();
		int mouseX = parent.getMouseX();
		int mouseY = parent.getMouseY();
		
		if(mouseDown) {
			if(mouseX >= x - 20 && mouseX <= x + width) {
				if(mouseY >= y && mouseY <= y + height + 1) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	@Override
	public void onPreSort() {
		int x = this.getRenderX();
		int y = this.getRenderY();
		int width = (this.width == -1 && this.height == -1) ? (getStringWidth(text) + minOffset * 2) : this.width;
		int height = (this.width == -1 && this.height == -1) ? (getStringHeight(text) + minOffset * 2) : this.height;
		int mouseX = parent.getMouseX();
		int mouseY = parent.getMouseY();
		
		ButtonState state = active ? ButtonState.ACTIVE : ButtonState.NORMAL;
		
		if(!disabled) {
			if(mouseX >= x - 20 && mouseX <= x + width) {
				if(mouseY >= y && mouseY <= y + height + 1) {
					state = ButtonState.HOVER;
					
					if(active) {
						state = ButtonState.HOVERACTIVE;
					}
					
					if(mouseDown) {
						active = !active;
						onAction();
					}
				}
			}
		} else {
			state = ButtonState.DISABLED;
		}
		
		lastState = state;
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
		
		drawRectFalcun(x, y, width, height, backgroundColor);
		
		drawHorizontalLine(x, y, width + 1, 1, lineColor);
		drawVerticalLine(x, y + 1, height - 1, 1, lineColor);
		drawHorizontalLine(x, y + height, width + 1, 1, lineColor);
		drawVerticalLine(x + width, y + 1, height - 1, 1, lineColor);
		
		Fonts.Roboto.drawString(text, x + (width / 2 - getStringWidth(text) / 2), y + height / 2 - (getStringHeight(text) / 2), textColor);		
		mouseDown = false;
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
