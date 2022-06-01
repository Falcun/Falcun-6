package net.mattbenson.gui.menu.components.macros;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.framework.TextPattern;
import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;
import net.mattbenson.gui.menu.IngameMenu;
import net.mattbenson.gui.menu.components.mods.SearchTextfield;
import net.mattbenson.utils.DrawUtils;

public class MacroSlimTextField extends SearchTextfield {
	public MacroSlimTextField(TextPattern pattern, int x, int y, int width, int height) {
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
		int x = this.getRenderX();
		int y = this.getRenderY();
		int width = this.width + minOffset * 2;
		int height = this.height;
		int mouseX = parent.getMouseX();
		
		if(tab) {
			if(!Keyboard.isKeyDown(Keyboard.KEY_TAB)) {
				tab = false;
			}
		}
		
		int backgroundColor = getColor(DrawType.BACKGROUND, lastState);
		int lineColor = getColor(DrawType.LINE, lastState);
		int textColor = getColor(DrawType.TEXT, lastState);
		
		drawRectFalcun(x, y + height - 3, width, 3, lineColor);
		
		String textToDraw = text;
		
		if(isPasswordField()) {
			StringBuilder builder = new StringBuilder();
			
			for(int i = 0; i < textToDraw.length(); i++) {
				builder.append("*");
			}
			
			textToDraw = builder.toString();
		}
		
		boolean drawPointer = false;
		
		if(focused) {
			if((System.currentTimeMillis() - lineTime) % lineRefreshTime * 2 >= lineRefreshTime) {
				drawPointer = true;
			}
		}
		
		int labelWidth = (int)Fonts.RobotoItalic.getStringWidth(textToDraw + 1);
		int comp = 0;
		int toRender = index;
		while(labelWidth >= width) {
			if(comp < index){
				textToDraw = textToDraw.substring(1);
				labelWidth = (int)Fonts.RobotoItalic.getStringWidth(textToDraw + 1);
				toRender--;
			} else if(comp > index){
				textToDraw = textToDraw.substring(0, textToDraw.length() - 1);
				labelWidth = (int)Fonts.RobotoItalic.getStringWidth(textToDraw + 1);
			}
			
			comp++;
		}
		
		if(drawPointer) {
			if(toRender > textToDraw.length()) {
				toRender = textToDraw.length() - 1; 
			}
			
			if(toRender < 0) {
				toRender = 0;
			}
			
			int textHeight = (int)Fonts.RobotoItalic.getStringHeight("ABC");
			
			drawVerticalLine(x + (int)Fonts.RobotoItalic.getStringWidth(textToDraw.substring(0, toRender)) + 1, y + height / 2 - textHeight / 2, textHeight, 1, textColor);
		}
		
		int renderIndex = comp;
		int renderStopIndex = comp + textToDraw.length();
		
		while(index > text.length()) {
			index--;
		}
		
		Fonts.RobotoItalic.drawString(textToDraw, x, y + height / 2 - (int)Fonts.RobotoItalic.getStringHeight(textToDraw) / 2, textColor);
				
		if(lastState == ButtonState.HOVER && mouseDown) {
			focused = true;
			lineTime = getLinePrediction();
			
			int position = x;
			
			if(mouseX < position) {
				index = 0;
				return;
			}
			
			float bestDiff = 1000;
			int bestIndex = -1;
			
			for(int i = renderIndex; i < renderStopIndex; i++) {
				if(text.length() <= i) {
					continue;
				}
				
				int diff = Math.abs(mouseX - position); 
				
				if(bestDiff > diff) {
					bestDiff = diff;
					bestIndex = i;
				}
				
				position += (int)Fonts.RobotoItalic.getStringWidth(text.charAt(i) + "");
			}
			
			if(mouseX > position) {
				index = text.length();
			} else if(bestIndex != -1) {
				index = bestIndex;
			} else {
				index = 0;
			}
		}
		
		mouseDown = false;
	}
}
