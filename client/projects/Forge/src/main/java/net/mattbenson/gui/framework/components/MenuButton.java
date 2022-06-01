package net.mattbenson.gui.framework.components;

import java.awt.Color;

import net.mattbenson.gui.framework.MenuComponent;
import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;

public class MenuButton extends MenuComponent {
	protected String text;
	protected int minOffset = 2;
	protected boolean mouseDown = false;
	protected boolean active = false;
	protected ButtonState lastState = ButtonState.NORMAL;
	
	public MenuButton(String text, int x, int y){
		super(x, y, -1, -1);
		this.text = text;
	}
	
	public MenuButton(String text, int x, int y, int width, int height) {
		super(x, y, width, height);
		this.text = text;
	}
	
	@Override
	public void onInitColors() {
		setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(35, 35, 35, 255));
		setColor(DrawType.BACKGROUND, ButtonState.ACTIVE, new Color(65, 65, 65, 255));
		setColor(DrawType.BACKGROUND, ButtonState.HOVER, new Color(50, 50, 50, 255));
		setColor(DrawType.BACKGROUND, ButtonState.HOVERACTIVE, new Color(75, 75, 75, 255));
		setColor(DrawType.BACKGROUND, ButtonState.DISABLED, new Color(100, 100, 100, 255));	
		
		setColor(DrawType.LINE, ButtonState.NORMAL, new Color(35, 35, 35, 255));
		setColor(DrawType.LINE, ButtonState.ACTIVE, new Color(65, 65, 65, 255));
		setColor(DrawType.LINE, ButtonState.HOVER, new Color(50, 50, 50, 255));
		setColor(DrawType.LINE, ButtonState.HOVERACTIVE, new Color(65, 65, 65, 255));
		setColor(DrawType.LINE, ButtonState.DISABLED, new Color(100, 100, 100, 255));

		setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(200, 200, 200, 255));
		setColor(DrawType.TEXT, ButtonState.ACTIVE, new Color(235, 235, 235, 255));
		setColor(DrawType.TEXT, ButtonState.HOVER, new Color(225, 225, 225, 255));
		setColor(DrawType.TEXT, ButtonState.HOVERACTIVE, new Color(235, 235, 235, 255));
		setColor(DrawType.TEXT, ButtonState.DISABLED, new Color(255, 255, 255, 255));
	}
	
	@Override
	public void onMouseClick(int button) {
		if(button == 0) {
			mouseDown = true;
		}
	}
	
	@Override
	public boolean passesThrough() {
		if(disabled || parent == null) {
			return true;
		}
		
		int width = (this.width == -1 && this.height == -1) ? (getStringWidth(text) + minOffset * 2) : this.width;
		int height = (this.width == -1 && this.height == -1) ? (getStringHeight(text) + minOffset * 2) : this.height;
		
		int x = this.getRenderX();
		int y = this.getRenderY();
		int mouseX = parent.getMouseX();
		int mouseY = parent.getMouseY();
		
		if(mouseDown) {
			if(mouseX >= x && mouseX <= x + width) {
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
			if(mouseX >= x && mouseX <= x + width) {
				if(mouseY >= y && mouseY <= y + height + 1) {
					state = ButtonState.HOVER;
					
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
		
		drawRectFalcun(x + 1, y + 1, width - 1, height - 1, backgroundColor);
		
		drawHorizontalLine(x, y, width + 1, 1, lineColor);
		drawVerticalLine(x, y + 1, height - 1, 1, lineColor);
		drawHorizontalLine(x, y + height, width + 1, 1, lineColor);
		drawVerticalLine(x + width, y + 1, height - 1, 1, lineColor);
		drawText(text, x + (width / 2 - getStringWidth(text) / 2), y + (height / 2 - getStringHeight(text) / 2), textColor);
		
		mouseDown = false;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void onAction() {}
}
