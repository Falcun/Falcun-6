package net.mattbenson.gui.menu.components.fps;

import java.awt.Color;

import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;
import net.mattbenson.gui.menu.components.cosmetics.CosmeticGenericButton;

public class FPSGenericButton extends CosmeticGenericButton {

	public FPSGenericButton(String text, int x, int y, int width, int height, boolean filledBackground) {
		super(text, x, y, width, height, filledBackground);
	}
	
	@Override
	public void onInitColors() {
		setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(18, 17, 22, 255));
		setColor(DrawType.BACKGROUND, ButtonState.ACTIVE, new Color(18, 17, 22, 255));
		setColor(DrawType.BACKGROUND, ButtonState.HOVER, new Color(18, 17, 22, 255));
		setColor(DrawType.BACKGROUND, ButtonState.HOVERACTIVE, new Color(18, 17, 22, 255));
		setColor(DrawType.BACKGROUND, ButtonState.DISABLED, new Color(100, 100, 100, 255));
		
		setColor(DrawType.LINE, ButtonState.NORMAL, new Color(30, 29, 32, 255));
		setColor(DrawType.LINE, ButtonState.ACTIVE, new Color(30, 29, 32, 255));
		setColor(DrawType.LINE, ButtonState.HOVER, new Color(30, 29, 32, 255));
		setColor(DrawType.LINE, ButtonState.HOVERACTIVE, new Color(30, 29, 32, 255));
		setColor(DrawType.LINE, ButtonState.DISABLED, new Color(100, 100, 100, 255));
		
		setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(150, 150, 150, 255));
		setColor(DrawType.TEXT, ButtonState.ACTIVE, new Color(225, 225, 225, 255));
		setColor(DrawType.TEXT, ButtonState.HOVER, new Color(175, 175, 175, 255));
		setColor(DrawType.TEXT, ButtonState.HOVERACTIVE, new Color(255, 255, 255, 255));
		setColor(DrawType.TEXT, ButtonState.DISABLED, new Color(255, 255, 255, 255));
	}

}
