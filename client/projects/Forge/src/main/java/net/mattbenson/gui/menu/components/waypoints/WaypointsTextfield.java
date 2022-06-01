package net.mattbenson.gui.menu.components.waypoints;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.framework.TextPattern;
import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;
import net.mattbenson.gui.menu.IngameMenu;
import net.mattbenson.gui.menu.components.mods.SearchTextfield;
import net.mattbenson.utils.DrawUtils;

public class WaypointsTextfield extends SearchTextfield {
	protected int placeholderText;
	protected int textPadding = 5;
	
	public WaypointsTextfield(TextPattern pattern, int x, int y, int width, int height, int placeholderText) {
		super(pattern, x, y, width, height);
		this.placeholderText = placeholderText;
	}
	
	@Override
	public void onInitColors() {
		super.onInitColors();
		
		setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(200, 200, 200, 255));
		setColor(DrawType.TEXT, ButtonState.ACTIVE, new Color(235, 235, 235, 255));
		setColor(DrawType.TEXT, ButtonState.HOVER, new Color(225, 225, 225, 255));
		setColor(DrawType.TEXT, ButtonState.HOVERACTIVE, new Color(235, 235, 235, 255));
		setColor(DrawType.TEXT, ButtonState.DISABLED, new Color(255, 255, 255, 255));
		
		setColor(DrawType.LINE, ButtonState.NORMAL, new Color(46, 46, 48, IngameMenu.MENU_ALPHA));
		setColor(DrawType.LINE, ButtonState.ACTIVE, new Color(54, 54, 57, IngameMenu.MENU_ALPHA));
		setColor(DrawType.LINE, ButtonState.HOVER, new Color(50, 50, 52, IngameMenu.MENU_ALPHA));
		setColor(DrawType.LINE, ButtonState.HOVERACTIVE, new Color(59, 59, 62, IngameMenu.MENU_ALPHA));
		setColor(DrawType.LINE, ButtonState.DISABLED, new Color(120, 120, 120, IngameMenu.MENU_ALPHA));

		setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(18, 17, 22, IngameMenu.MENU_ALPHA));
		setColor(DrawType.BACKGROUND, ButtonState.ACTIVE, new Color(29, 29, 32, IngameMenu.MENU_ALPHA));
		setColor(DrawType.BACKGROUND, ButtonState.HOVER, new Color(25, 25, 28, IngameMenu.MENU_ALPHA));
		setColor(DrawType.BACKGROUND, ButtonState.HOVERACTIVE, new Color(36, 36, 40, IngameMenu.MENU_ALPHA));
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
		
		int textColorDisabled = getColor(DrawType.TEXT, ButtonState.DISABLED);
		
		DrawUtils.drawRoundedRect(x - 4, y - 4, x + width + 5, y + height + 5, 0, 83886080);
		DrawUtils.drawRoundedRect(x - 3, y - 3, x + width + 4, y + height + 4, 0, 369098752);
		DrawUtils.drawRoundedRect(x - 2, y - 2, x + width + 3, y + height + 3, 0, 587202560);

		DrawUtils.drawRoundedRect(x - 1, y - 1, x + width + 2, y + height + 2, 0, lineColor);
		DrawUtils.drawRoundedRect(x, y, x + width + 1, y + height + 1, 0, lineColor);
		DrawUtils.drawRoundedRect(x, y, x + width + 1, y + height + 1, 0, backgroundColor);
		
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
		while(labelWidth >= width + textPadding * 2) {
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
		
		int xAdd = textPadding;
		
		if(textToDraw.isEmpty() && !isFocused()) {
			textToDraw = "" + placeholderText;
			xAdd = width / 2 - Fonts.RobotoItalic.getStringWidth("" + placeholderText) / 2;
			textColor = PLACEHOLDER_COLOR.getRGB();
			
			if(textToDraw.length() == 3) {
				textColor = textColorDisabled;
			}
		}
		
		Fonts.RobotoItalic.drawString(textToDraw, x + xAdd, y + height / 2 - (int)Fonts.RobotoItalic.getStringHeight(textToDraw) / 2, textColor);
				
		if(lastState == ButtonState.HOVER && mouseDown) {
			focused = true;
			lineTime = getLinePrediction();
			
			int position = x + textPadding;
			
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
