package net.mattbenson.gui.framework.components;

import java.awt.Color;
import java.awt.Point;

import org.lwjgl.input.Keyboard;

import net.mattbenson.gui.framework.BindType;
import net.mattbenson.gui.framework.MenuComponent;
import net.mattbenson.gui.framework.MenuPriority;
import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class MenuCheckbox extends MenuComponent {
	protected String text;
	protected String tooltip;
	protected boolean checked;
	
	protected ButtonState lastState = ButtonState.NORMAL;
	protected boolean checkable = true;
	protected boolean wantsToBind = false;
	protected int textOffset = 4;
	protected int minOffset = 2;
	protected int optionWindowWidth = 75;
	protected int optionWindowHeight = 36;
	protected boolean mouseDown = false;
	protected Point startPos = null;
		
	public MenuCheckbox(String text, String tooltip, int x, int y, int width, int height) {
		super(x, y, width, height);
		this.text = text;
		this.tooltip = tooltip;
	}

	public MenuCheckbox(String text, int x, int y, int width, int height) {
		super(x, y, width, height);
		this.text = text;
		this.tooltip = "";
	}
	
	@Override
	public void onInitColors() {
		setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(35, 35, 35, 255));
		setColor(DrawType.BACKGROUND, ButtonState.ACTIVE, new Color(81, 108, 255, 255));
		setColor(DrawType.BACKGROUND, ButtonState.HOVER, new Color(50, 50, 50, 255));
		setColor(DrawType.BACKGROUND, ButtonState.HOVERACTIVE, new Color(100, 120, 255, 255));
		setColor(DrawType.BACKGROUND, ButtonState.POPUP, new Color(10, 10, 10, 255));	
		setColor(DrawType.BACKGROUND, ButtonState.DISABLED, new Color(100, 100, 100, 255));	

		setColor(DrawType.LINE, ButtonState.NORMAL, new Color(10, 10, 10, 255));
		setColor(DrawType.LINE, ButtonState.ACTIVE, new Color(10, 10, 10, 255));
		setColor(DrawType.LINE, ButtonState.HOVER, new Color(20, 20, 20, 255));
		setColor(DrawType.LINE, ButtonState.HOVERACTIVE, new Color(30, 30, 30, 255));
		setColor(DrawType.LINE, ButtonState.POPUP, new Color(100, 120, 255, 255));
		setColor(DrawType.LINE, ButtonState.DISABLED, new Color(100, 100, 100, 255));

		setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(200, 200, 200, 255));
		setColor(DrawType.TEXT, ButtonState.ACTIVE, new Color(235, 235, 235, 255));
		setColor(DrawType.TEXT, ButtonState.HOVER, new Color(225, 225, 225, 255));
		setColor(DrawType.TEXT, ButtonState.HOVERACTIVE, new Color(235, 235, 235, 255));
		setColor(DrawType.TEXT, ButtonState.POPUP, new Color(100, 100, 100, 255));	
		setColor(DrawType.TEXT, ButtonState.DISABLED, new Color(255, 255, 255, 255));
	}
	
	@Override
	public void onMouseClick(int button) {
		if(button == 0) {
			mouseDown = true;
		}
	}

	@Override
	public boolean passesThrough() {
		if(disabled) {
			return true;
		}
		
		int mouseX = parent.getMouseX();
		int mouseY = parent.getMouseY();
		
		if (startPos != null) {
			boolean passed = true;
			
			if (mouseDown) {
				if (mouseX >= startPos.x && mouseX <= startPos.x + optionWindowWidth) {
					if (mouseY >= startPos.y && mouseY <= startPos.y + optionWindowHeight) {
						passed = false;
					}
				}
			}
			
			return passed;
		} else if(mouseDown) {
			int x = this.getRenderX();
			int y = this.getRenderY();
			int width = getStringWidth(text) + this.width + textOffset;
			int height = this.height;
			
			if(mouseX >= x && mouseX <= x + width) {
				if(mouseY >= y && mouseY <= y + height + 1) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	@Override
	public void onPreSort() {
		int x = this.getRenderX();
		int y = this.getRenderY();
		int width = getStringWidth(text) + this.width + textOffset;
		int height = this.height;
		int mouseX = parent.getMouseX();
		int mouseY = parent.getMouseY();

		ButtonState state = checked ? ButtonState.ACTIVE : ButtonState.NORMAL;
		
		if (!disabled) {
			if (mouseX >= x && mouseX <= x + width) {
				if (mouseY >= y && mouseY <= y + height) {
					state = checked ? ButtonState.HOVERACTIVE : ButtonState.HOVER;
					
					if(tooltip.length() > 0) {
						setPriority(MenuPriority.HIGHEST);
					}
					
					if(mouseDown && checkable) {
						setChecked(!isChecked());
					}
				}
			}

		} else {
			state = ButtonState.DISABLED;
		}

		if(!checkable) {
			state = ButtonState.DISABLED;
		}
		
		if(state == ButtonState.HOVER || state == ButtonState.HOVERACTIVE) {
			setPriority(MenuPriority.HIGH);
		} else {
			setPriority(MenuPriority.MEDIUM);
		}
		
		lastState = state;
	}
	
	@Override
	public void onRender() {
		int x = this.getRenderX();
		int y = this.getRenderY();
		
		int backgroundColor = getColor(DrawType.BACKGROUND, lastState);
		int lineColor = getColor(DrawType.LINE, lastState);
		int textColor = getColor(DrawType.TEXT, lastState);
		
		drawRectFalcun(x + 2, y + 2, width - 3, height - 3, backgroundColor);
		
		drawHorizontalLine(x, y, width + 1, 1, lineColor);
		drawVerticalLine(x, y + 1, height - 1, 1, lineColor);
		drawHorizontalLine(x, y + height, width + 1, 1, lineColor);
		drawVerticalLine(x + width, y + 1, height - 1, 1, lineColor);
		drawText(text, x + width + textOffset, y + 1, textColor);
		
		drawTooltip();
		
		mouseDown = false;
	}
	
	public void drawTooltip() {
		if (tooltip.length() > 0 && (lastState == ButtonState.HOVER || lastState == ButtonState.HOVERACTIVE)) {
			int tipWidth = getStringWidth(tooltip) + minOffset * 2;
			int tipHeight = getStringHeight(tooltip) + minOffset * 2;
			int lineColor = getColor(DrawType.LINE, ButtonState.POPUP);
			int mouseX = parent.getMouseX();
			int mouseY = parent.getMouseY() - tipHeight;
			
			ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft(), 1);
			
			if(mouseX + tipWidth >= res.getScaledWidth()) {
				mouseX -= tipWidth;
			}

			drawRectFalcun(mouseX, mouseY, tipWidth, tipHeight, getColor(DrawType.BACKGROUND, ButtonState.POPUP));
			drawHorizontalLine(mouseX, mouseY, tipWidth + 1, 1, lineColor);
			drawVerticalLine(mouseX, mouseY + 1, tipHeight - 1, 1, lineColor);
			drawHorizontalLine(mouseX, mouseY + tipHeight, tipWidth + 1, 1, lineColor);
			drawVerticalLine(mouseX + tipWidth, mouseY + 1, tipHeight - 1, 1, lineColor);
			drawText(tooltip, mouseX + minOffset, mouseY + minOffset, getColor(DrawType.TEXT, ButtonState.POPUP));
		}
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
		onAction();
	}
	
	public boolean getRValue() {
		return isChecked();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public boolean isCheckable() {
		return checkable;
	}

	public void setCheckable(boolean checkable) {
		this.checkable = checkable;
	}

	public void onAction() {}
	public void onBindAction() {}
}