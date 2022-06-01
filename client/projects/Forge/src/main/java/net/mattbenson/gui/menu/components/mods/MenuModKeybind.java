package net.mattbenson.gui.menu.components.mods;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.framework.MenuComponent;
import net.mattbenson.gui.framework.MenuPriority;
import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;

public class MenuModKeybind extends MenuComponent {
	public final static int mouseOffset = 500;
	
	protected boolean mouseDown;
	protected ButtonState lastState;

	protected boolean binding;
	protected int bind = Keyboard.CHAR_NONE;
	
	public MenuModKeybind(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	@Override
	public void onInitColors() {
		setColor(DrawType.LINE, ButtonState.NORMAL, new Color(43, 43, 43, 255));
		setColor(DrawType.LINE, ButtonState.ACTIVE, new Color(68, 68, 68, 255));
		setColor(DrawType.LINE, ButtonState.HOVER, new Color(58, 58, 58, 255));
		setColor(DrawType.LINE, ButtonState.HOVERACTIVE, new Color(82, 82, 82, 255));
		setColor(DrawType.LINE, ButtonState.DISABLED, new Color(150, 150, 150, 255));
		
		setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(162, 162, 162, 255));
		setColor(DrawType.TEXT, ButtonState.ACTIVE, new Color(182, 182, 182, 255));
		setColor(DrawType.TEXT, ButtonState.HOVER, new Color(182, 182, 182, 255));
		setColor(DrawType.TEXT, ButtonState.HOVERACTIVE, new Color(182, 182, 182, 255));
		setColor(DrawType.TEXT, ButtonState.DISABLED, new Color(100, 100, 100, 255));
	}
	
	@Override
	public boolean onExitGui(int key) {
		if(binding) {
			if(key == Keyboard.KEY_ESCAPE) {
				onKeyDown(' ', Keyboard.CHAR_NONE);
				return true;
			}
			
			binding = false;
			return true;
		}
		
		return false;
	}
	
	@Override
	public void onKeyDown(char character, int key) {
		if(binding) {
			bind = key;
			binding = false;
			onAction();
		}
	}
	
	@Override
	public void onMouseClick(int button) {
		if(binding) {
			onKeyDown(' ', button - mouseOffset);			
			return;
		}
		
		if(button == 0) {
			mouseDown = true;
		}
	}
	
	@Override
	public void onPreSort() {
		int x = this.getRenderX();
		int y = this.getRenderY();
		int mouseX = parent.getMouseX();
		int mouseY = parent.getMouseY();

		ButtonState state = binding ? ButtonState.ACTIVE : ButtonState.NORMAL;
		
		if (!disabled) {
			boolean updated = false;
			
			if (mouseX >= x && mouseX <= x + width) {
				if (mouseY >= y && mouseY <= y + height + 1) {
					state = binding ? ButtonState.HOVERACTIVE : ButtonState.HOVER;
					
					if(mouseDown) {
						binding = true;
						updated = true;
					}
				}
			}
			
			if(binding && !updated && mouseDown) {
				binding = false;
			}
		} else {
			state = ButtonState.DISABLED;
		}
		
		if(binding) {
			setPriority(MenuPriority.HIGHEST);
		} else {
			if(state == ButtonState.HOVER || state == ButtonState.HOVERACTIVE) {
				setPriority(MenuPriority.HIGH);
			} else {
				setPriority(MenuPriority.MEDIUM);
			}
		}
		
		lastState = state;
	}
	
	@Override
	public boolean passesThrough() {
		if(disabled) {
			return true;
		}
		
		if(mouseDown) {
			int x = this.getRenderX();
			int y = this.getRenderY();
			int mouseX = parent.getMouseX();
			int mouseY = parent.getMouseY();		
			
			if(mouseX >= x && mouseX <= x + width) {
				if(mouseY >= y && mouseY <= y + height) {
					return false;
				}
			}
		}
		
		return !binding;
	}
	
	@Override
	public void onRender() {
		int x = this.getRenderX();
		int y = this.getRenderY();
		int width = this.width;
		int height = this.height;
		
		int lineColor = getColor(DrawType.LINE, lastState);
		int textColor = getColor(DrawType.TEXT, ButtonState.NORMAL);
		
		drawHorizontalLine(x, y, width + 1, 1, lineColor);
		drawVerticalLine(x, y + 1, height - 1, 1, lineColor);
		drawHorizontalLine(x, y + height, width + 1, 1, lineColor);
		drawVerticalLine(x + width, y + 1, height - 1, 1, lineColor);
		
		String text = "CLICK TO BIND";
		
		if(binding) {
			text = "PRESS A KEY";
		} else if(getBind() < 0 && getBind() + mouseOffset >= 0) {
			String name = "";
			
			int key = getBind() + mouseOffset;
			
			if(key == 0) {
				name = "MOUSE 1";
			} else if(key == 1) {
				name = "MOUSE 2";
			} else if(key == 2) {
				name = "MOUSE 3";
			} else if(key == 3) {
				name = "MOUSE 4";
			} else if(key == 4) {
				name = "MOUSE 5";
			}
			
			text = "BOUND: " + name.toUpperCase();
		} else if(isBound()) {
			text = "BOUND: " + Keyboard.getKeyName(getBind()).toUpperCase();
		}
		
		drawText(text, x + width / 2 - getStringWidth(text) / 2, y + height / 2 - getStringHeight(text) / 2, textColor);
		
		mouseDown = false;
	}

	@Override
	public void drawText(String text, int x, int y, int color) {
		Fonts.Roboto.drawString(text, x, y, color);
	}
	
	@Override
	public int getStringWidth(String string) {
		return Fonts.Roboto.getStringWidth(string);
	}
	
	@Override
	public int getStringHeight(String string) {
		return Fonts.Roboto.getStringHeight(string);
	}
	
	public boolean isBound() {
		return bind != Keyboard.CHAR_NONE;
	}
	
	public int getBind() {
		return bind;
	}
	
	public void setBind(int bind) {
		this.bind = bind;
	}
	
	public void onAction() {}
}
