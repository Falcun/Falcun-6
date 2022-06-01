package net.mattbenson.gui.menu.components.mods;

import java.awt.Color;

import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.framework.components.MenuLabel;
import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;

public class FeatureText extends MenuLabel {
	
	public FeatureText(String text, String tooltip, int x, int y) {
		super(text, tooltip, x, y);
	}
	
	public FeatureText(String text, int x, int y) {
		super(text, x, y);
	}
	
	@Override
	public void onInitColors() {
		super.onInitColors();
		
		setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(255, 255, 255, 255));
		setColor(DrawType.TEXT, ButtonState.HOVER, new Color(255, 255, 255, 255));
		setColor(DrawType.TEXT, ButtonState.DISABLED, new Color(255, 255, 255, 255));
	}
	
	@Override
	public void drawText(String text, int x, int y, int color) {
		if (text.equals("BETA MODE")) {
			Fonts.Roboto.drawString(text, x, y, -131311111);
		} else {
			Fonts.Roboto.drawString(text, x, y, color);
		}
	}
	
	@Override
	public int getStringWidth(String text) {
		return Fonts.Roboto.getStringWidth(text);
	}
	
	@Override
	public int getStringHeight(String text) {
		return Fonts.Roboto.getStringHeight(text);
	}
}
