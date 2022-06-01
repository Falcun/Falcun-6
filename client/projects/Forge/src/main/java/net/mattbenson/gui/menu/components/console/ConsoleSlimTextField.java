package net.mattbenson.gui.menu.components.console;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.framework.TextPattern;
import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;
import net.mattbenson.gui.menu.IngameMenu;
import net.mattbenson.gui.menu.components.mods.SearchTextfield;

public class ConsoleSlimTextField extends SearchTextfield {
	public ConsoleSlimTextField(TextPattern pattern, int x, int y, int width, int height) {
		super(pattern, x, y, width, height);
	}
	
	@Override
	public void onInitColors() {
		super.onInitColors();
		
		setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(127, 127, 127, IngameMenu.MENU_ALPHA));
		setColor(DrawType.TEXT, ButtonState.ACTIVE, new Color(200, 200, 200, IngameMenu.MENU_ALPHA));
		setColor(DrawType.TEXT, ButtonState.HOVER, new Color(150, 150, 150, IngameMenu.MENU_ALPHA));
		setColor(DrawType.TEXT, ButtonState.HOVERACTIVE, new Color(225, 225, 225, IngameMenu.MENU_ALPHA));
		setColor(DrawType.TEXT, ButtonState.DISABLED, new Color(255, 255, 255, IngameMenu.MENU_ALPHA));
		
		setColor(DrawType.LINE, ButtonState.NORMAL, new Color(36, 35, 38, IngameMenu.MENU_ALPHA));
		setColor(DrawType.LINE, ButtonState.ACTIVE, new Color(200, 200, 200, IngameMenu.MENU_ALPHA));
		setColor(DrawType.LINE, ButtonState.HOVER, new Color(50, 50, 52, IngameMenu.MENU_ALPHA));
		setColor(DrawType.LINE, ButtonState.HOVERACTIVE, new Color(225, 225, 225, IngameMenu.MENU_ALPHA));
		setColor(DrawType.LINE, ButtonState.DISABLED, new Color(255, 255, 255, IngameMenu.MENU_ALPHA));

		setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(18, 17, 22, IngameMenu.MENU_ALPHA));
		setColor(DrawType.BACKGROUND, ButtonState.ACTIVE, new Color(18, 17, 22, IngameMenu.MENU_ALPHA));
		setColor(DrawType.BACKGROUND, ButtonState.HOVER, new Color(18, 17, 22, IngameMenu.MENU_ALPHA));
		setColor(DrawType.BACKGROUND, ButtonState.HOVERACTIVE, new Color(18, 17, 22, IngameMenu.MENU_ALPHA));
		setColor(DrawType.BACKGROUND, ButtonState.DISABLED, new Color(100, 100, 100, IngameMenu.MENU_ALPHA));	
	}
	
	@Override
	public void onRender() {

	}
}
