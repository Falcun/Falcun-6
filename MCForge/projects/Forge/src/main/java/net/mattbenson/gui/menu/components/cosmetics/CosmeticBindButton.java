package net.mattbenson.gui.menu.components.cosmetics;

import java.awt.Color;

import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;

public class CosmeticBindButton extends CosmeticGenericButton {
	private String type;
	private boolean binding;
	
	public CosmeticBindButton(int x, int y, int width, int height) {
		super("", x, y, width, height, true);
		this.type = "";
	}
	
	@Override
	public void onInitColors() {
		super.onInitColors();
		
		setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(18, 17, 22, 255));
		setColor(DrawType.BACKGROUND, ButtonState.ACTIVE, new Color(43, 42, 48, 255));
		setColor(DrawType.BACKGROUND, ButtonState.HOVER, new Color(32, 31, 36, 255));
		setColor(DrawType.BACKGROUND, ButtonState.HOVERACTIVE, new Color(57, 56, 64, 255));
		setColor(DrawType.BACKGROUND, ButtonState.DISABLED, new Color(100, 100, 100, 255));
		
		setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(74, 74, 75, 255));
		setColor(DrawType.TEXT, ButtonState.ACTIVE, new Color(101, 101, 103, 255));
		setColor(DrawType.TEXT, ButtonState.HOVER, new Color(88, 88, 90, 255));
		setColor(DrawType.TEXT, ButtonState.HOVERACTIVE, new Color(118, 118, 120, 255));
		setColor(DrawType.TEXT, ButtonState.DISABLED, new Color(255, 255, 255, 255));
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
						onBind();
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
		
		if(filledBackground) {
			drawRectFalcun(x, y, width, height, backgroundColor);
		}
		
		drawHorizontalLine(x, y, width + 1, 1, lineColor);
		drawVerticalLine(x, y + 1, height - 1, 1, lineColor);
		drawHorizontalLine(x, y + height, width + 1, 1, lineColor);
		drawVerticalLine(x + width, y + 1, height - 1, 1, lineColor);

		drawShadowUp(x, y, width + 1);
		drawShadowLeft(x, y, height + 1);
		drawShadowDown(x, y + height + 1, width + 1);
		drawShadowRight(x + width + 1, y, height + 1);
		
		String text = this.text;
		
		if(active) {
			text = "NOW CLICK AN EMOTE.";
		} else if(type.length() > 0) {
			text = type;
		} else {
			text = "EMPTY";
		}
		
		text = text.replace("emoticons.emotes.", "");
		text = text.replace(".title", "");
		
		drawText(text, x + (width / 2 - getStringWidth(text) / 2), y + (height / 2 - getStringHeight(text) / 2), textColor);
		
		mouseDown = false;
	}
	
	private void onBind() {
		onAction();
	}
	
	public void setBind(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
}
