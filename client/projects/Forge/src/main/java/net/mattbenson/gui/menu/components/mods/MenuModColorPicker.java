package net.mattbenson.gui.menu.components.mods;

import java.awt.Color;

import org.lwjgl.input.Mouse;

import net.mattbenson.gui.framework.components.MenuColorPicker;
import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;

public class MenuModColorPicker extends MenuColorPicker {
	public MenuModColorPicker(int x, int y, int width, int height, int defaultColor) {
		super(x, y, width, height, defaultColor);
	}

	@Override
	public void onInitColors() {
		super.onInitColors();
		
		setColor(DrawType.LINE, ButtonState.NORMAL, new Color(43, 43, 43, 255));
		setColor(DrawType.LINE, ButtonState.ACTIVE, new Color(53, 53, 53, 255));
		setColor(DrawType.LINE, ButtonState.HOVER, new Color(48, 48, 48, 255));
		setColor(DrawType.LINE, ButtonState.HOVERACTIVE, new Color(59, 59, 59, 255));
		setColor(DrawType.LINE, ButtonState.DISABLED, new Color(100, 100, 100, 255));
	}
	
	@Override
	public void onRender() {
		int x = this.getRenderX();
		int y = this.getRenderY();
		int lineColor = getColor(DrawType.LINE, lastState);
		
		drawHorizontalLine(x, y, width + 1, 1, lineColor);
		drawVerticalLine(x, y + 1, height - 1, 1, lineColor);
		drawHorizontalLine(x, y + height, width + 1, 1, lineColor);
		drawVerticalLine(x + width, y + 1, height - 1, 1, lineColor);
		
		drawRectFalcun(x + 2, y + 2, width - 3, height - 3, lineColor);
		
		int index = 0;
		
		for(int h = y; h < y + height - 5; h++) {
			drawRectFalcun(x + 3, h + 3, width - 5, 1, disabled ? lightenColor(index, 7, color).getRGB() : darkenColor(index, 7, color).getRGB());
			index++;
		}
		
		if(startType <= 0) {
			if(alphaSlider.getParent() == null) {
				alphaSlider.setParent(getParent());
			}
			
			alphaSlider.onPreSort();
		}
		
		drawPicker();
		
		if(wantsToDrag) {
			mouseDragging = Mouse.isButtonDown(0);
			wantsToDrag = mouseDragging;
		}
		
		mouseDown = false;
		mouseDragging = false;
	}
}
