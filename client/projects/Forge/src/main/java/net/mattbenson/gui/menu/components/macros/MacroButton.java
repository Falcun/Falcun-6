package net.mattbenson.gui.menu.components.macros;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.framework.TextPattern;
import net.mattbenson.gui.framework.components.MenuButton;
import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;
import net.mattbenson.utils.DrawUtils;

public class MacroButton extends MenuButton {
	private boolean approve;
	
	public MacroButton(String text, int x, int y, int width, int height, boolean approve) {
		super(text, x, y, width, height);
		this.approve = approve;
		onInitColors();
	}

	@Override
	public void onInitColors() {
		if(approve) {
			setColor(DrawType.LINE, ButtonState.NORMAL, new Color(33, 74, 19, 255));
			setColor(DrawType.LINE, ButtonState.ACTIVE, new Color(50, 112, 29, 255));
			setColor(DrawType.LINE, ButtonState.HOVER, new Color(47, 105, 27, 255));
			setColor(DrawType.LINE, ButtonState.HOVERACTIVE, new Color(60, 133, 34, 255));
			setColor(DrawType.LINE, ButtonState.DISABLED, new Color(100, 100, 100, 255));
		} else {
			setColor(DrawType.LINE, ButtonState.NORMAL, new Color(75, 12, 14, 255));
			setColor(DrawType.LINE, ButtonState.ACTIVE, new Color(99, 15, 18, 255));
			setColor(DrawType.LINE, ButtonState.HOVER, new Color(92, 16, 18, 255));
			setColor(DrawType.LINE, ButtonState.HOVERACTIVE, new Color(110, 19, 21, 255));
			setColor(DrawType.LINE, ButtonState.DISABLED, new Color(100, 100, 100, 255));
		}
		
		setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(200, 200, 200, 255));
		setColor(DrawType.TEXT, ButtonState.ACTIVE, new Color(235, 235, 235, 255));
		setColor(DrawType.TEXT, ButtonState.HOVER, new Color(225, 225, 225, 255));
		setColor(DrawType.TEXT, ButtonState.HOVERACTIVE, new Color(235, 235, 235, 255));
		setColor(DrawType.TEXT, ButtonState.DISABLED, new Color(255, 255, 255, 255));
	}
	
	@Override
	public void onRender() {
		int x = this.getRenderX();
		int y = this.getRenderY();
		int width = (this.width == -1 && this.height == -1) ? (getStringWidth(text) + minOffset * 2) : this.width;
		int height = (this.width == -1 && this.height == -1) ? (getStringHeight(text) + minOffset * 2) : this.height;
		
		int lineColor = getColor(DrawType.LINE, lastState);
		int textColor = getColor(DrawType.TEXT, lastState);
		
		drawHorizontalLine(x, y, width + 1, 1, lineColor);
		drawVerticalLine(x, y + 1, height - 1, 1, lineColor);
		drawHorizontalLine(x, y + height, width + 1, 1, lineColor);
		drawVerticalLine(x + width, y + 1, height - 1, 1, lineColor);

		drawShadowUp(x, y, width + 1);
		drawShadowLeft(x, y, height + 1);
		drawShadowDown(x, y + height + 1, width + 1);
		drawShadowRight(x + width + 1, y, height + 1);
		
		drawText(text, x + (width / 2 - getStringWidth(text) / 2), y + (height / 2 - getStringHeight(text) / 2), textColor);
		
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
	
	public void setApprove(boolean approve) {
		this.approve = approve;
		onInitColors();
	}
}
