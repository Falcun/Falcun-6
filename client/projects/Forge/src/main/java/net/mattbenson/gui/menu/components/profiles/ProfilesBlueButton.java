package net.mattbenson.gui.menu.components.profiles;

import java.awt.Color;

import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;
import net.mattbenson.gui.menu.components.macros.MacroButton;

public class ProfilesBlueButton extends MacroButton {

	public ProfilesBlueButton(String text, int x, int y, int width, int height) {
		super(text, x, y, width, height, true);
	}
	
	@Override
	public void onInitColors() {
		setColor(DrawType.LINE, ButtonState.NORMAL, new Color(14, 32, 48, 255));
		setColor(DrawType.LINE, ButtonState.ACTIVE, new Color(29, 66, 99, 255));
		setColor(DrawType.LINE, ButtonState.HOVER, new Color(28, 60, 89, 255));
		setColor(DrawType.LINE, ButtonState.HOVERACTIVE, new Color(34, 79, 120, 255));
		setColor(DrawType.LINE, ButtonState.DISABLED, new Color(100, 100, 100, 255));
		
		setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(200, 200, 200, 255));
		setColor(DrawType.TEXT, ButtonState.ACTIVE, new Color(235, 235, 235, 255));
		setColor(DrawType.TEXT, ButtonState.HOVER, new Color(225, 225, 225, 255));
		setColor(DrawType.TEXT, ButtonState.HOVERACTIVE, new Color(235, 235, 235, 255));
		setColor(DrawType.TEXT, ButtonState.DISABLED, new Color(255, 255, 255, 255));
	}
}
