package net.mattbenson.gui.framework.components;

import java.awt.Color;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.lwjgl.input.Mouse;

import net.mattbenson.gui.framework.MenuComponent;
import net.mattbenson.gui.framework.MenuPriority;
import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;

public class MenuSlider extends MenuComponent {
	protected boolean isFloat = false;
	protected float value;
	protected float minValue;
	protected float maxValue;
	protected int minOffset = 2;
	protected DecimalFormat format;
	protected boolean wantToDrag = false;
	protected boolean mouseDragging = false;
	protected boolean mouseDown = false;
	protected ButtonState lastState = ButtonState.NORMAL;

	public MenuSlider(int startValue, int minValue, int maxValue, int x, int y, int width, int height) {
		super(x, y, width, height);
		
		init(startValue, minValue, maxValue, 1);
	}
	
	public MenuSlider(float startValue, float minValue, float maxValue, int precision, int x, int y, int width, int height) {
		super(x, y, width, height);
		isFloat = true;
		
		init(startValue, minValue, maxValue, precision);
	}
	
	private void initPrecision(int precision) {
		this.format = new DecimalFormat();
		format.setMaximumFractionDigits(precision);
		format.setRoundingMode(RoundingMode.HALF_DOWN);
	}
	
	private void init(float startValue, float minValue, float maxValue, int precision) {
		this.value = startValue;
		this.minValue = minValue;
		this.maxValue = maxValue;
		
		if(minValue > value)
			value = minValue;
	
		if(value > maxValue)
			value = maxValue;
		
		if(this.minValue > this.maxValue)
			this.maxValue = this.minValue;
		
		initPrecision(precision < 1 ? 1 : precision);
	}
	
	@Override
	public void onInitColors() {
		setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(35, 35, 35, 255));
		setColor(DrawType.BACKGROUND, ButtonState.ACTIVE, new Color(65, 65, 65, 255));
		setColor(DrawType.BACKGROUND, ButtonState.HOVER, new Color(50, 50, 50, 255));
		setColor(DrawType.BACKGROUND, ButtonState.HOVERACTIVE, new Color(65, 65, 65, 255));
		setColor(DrawType.BACKGROUND, ButtonState.DISABLED, new Color(100, 100, 100, 255));	
		setColor(DrawType.BACKGROUND, ButtonState.POPUP, new Color(50, 85, 139, 255));
		
		setColor(DrawType.LINE, ButtonState.NORMAL, new Color(10, 10, 10, 255));
		setColor(DrawType.LINE, ButtonState.ACTIVE, new Color(10, 10, 10, 255));
		setColor(DrawType.LINE, ButtonState.HOVER, new Color(20, 20, 20, 255));
		setColor(DrawType.LINE, ButtonState.HOVERACTIVE, new Color(20, 20, 20, 255));
		setColor(DrawType.LINE, ButtonState.POPUP, new Color(83, 124, 189, 255));
		setColor(DrawType.LINE, ButtonState.DISABLED, new Color(100, 100, 100, 255));

		setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(200, 200, 200, 255));
		setColor(DrawType.TEXT, ButtonState.ACTIVE, new Color(235, 235, 235, 255));
		setColor(DrawType.TEXT, ButtonState.HOVER, new Color(225, 225, 225, 255));
		setColor(DrawType.TEXT, ButtonState.HOVERACTIVE, new Color(235, 235, 235, 255));
		setColor(DrawType.TEXT, ButtonState.DISABLED, new Color(255, 255, 255, 255));
		setColor(DrawType.TEXT, ButtonState.POPUP, new Color(81, 108, 255, 255));
	}
	
	@Override
	public void onMouseClick(int button) {
		if(button == 0)
			mouseDown = true;
	}
	
	@Override
	public void onMouseClickMove(int button) {
		if(button == 0)
			mouseDragging = true;
	}

	@Override
	public boolean passesThrough() {
		if(disabled || parent == null)
			return true;
		
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
		return !wantToDrag;
	}
	
	@Override
	public void onPreSort() {
		int x = this.getRenderX();
		int y = this.getRenderY();
		int width = this.width;
		int height = this.height;
		int mouseX = parent.getMouseX();
		int mouseY = parent.getMouseY();
		
		ButtonState state = ButtonState.NORMAL;
		
		if(!disabled) {
			if(mouseX >= x && mouseX <= x + width) {
				if(mouseY >= y && mouseY <= y + height) {
					state = ButtonState.HOVER;
				}
			}
		} else {
			state = ButtonState.DISABLED;
		}
		
		if(wantToDrag) {
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
	public void onRender() {
		int x = this.getRenderX();
		int y = this.getRenderY();
		int width = this.width;
		int height = this.height;
		int mouseX = parent.getMouseX();
		
		int backgroundColor = getColor(DrawType.BACKGROUND, lastState);
		int lineColor = getColor(DrawType.LINE, lastState);
		int textColor = getColor(DrawType.TEXT, lastState);

		drawRectFalcun(x + 1, y + 1, width - 1, height - 1, backgroundColor);
		drawHorizontalLine(x, y, width + 1, 1, lineColor);
		drawVerticalLine(x, y + 1, height - 1, 1, lineColor);
		drawHorizontalLine(x, y + height, width + 1, 1, lineColor);
		drawVerticalLine(x + width, y + 1, height - 1, 1, lineColor);
		
		String data = "";
		
		if(isFloat) {
			data = getValue() + "/" + getMaxValue();
		} else {
			data = getIntValue() + "/" + Math.round(getMaxValue());
		}
		
		float diff = maxValue - minValue;
		
		int linePos = x + Math.round((width) * (value - minValue) / (diff));
		
		if(linePos + 1 >= x + width) {
			linePos -= minOffset;
		} else if(linePos - 1 <= x) {
			linePos += minOffset;
		}
		
		drawText(data, x + width / 2 - getStringWidth(data) / 2, y + height / 2 - getStringHeight(data) / 2, textColor);
		drawVerticalLine(linePos - 1, y + 1, height - 1, 1, getColor(DrawType.LINE, ButtonState.ACTIVE));
		drawVerticalLine(linePos, y + 1, height - 1, 1, getColor(DrawType.TEXT, ButtonState.POPUP));
		drawVerticalLine(linePos + 1, y + 1, height - 1, 1, getColor(DrawType.LINE, ButtonState.ACTIVE));
		
		if(wantToDrag || (mouseDown && lastState == ButtonState.HOVER)) {
			if(mouseDown) {
				wantToDrag = true;
			}
			
			float wantedValue = minValue + (mouseX - minOffset - x) * diff / (width - minOffset * 2);
			
			if(wantedValue > maxValue) {
				wantedValue = maxValue;
			} else if(minValue > wantedValue) {
				wantedValue = minValue;
			}

			final float oldValue = value;
			
			value = wantedValue;
			
			if(oldValue != value) {
				onAction();
			}
		}
		
		if(wantToDrag) {
			mouseDragging = Mouse.isButtonDown(0);
			wantToDrag = mouseDragging;
		}
		
		mouseDragging = false;
		mouseDown = false;
	}
	
	public int getIntValue() {
		return Math.round(value);
	}
	
	public float getValue() {
		try {
			return Float.valueOf(format.format(value));
		} catch (NumberFormatException e) {
			return Math.round(value * 10) / 10f;
		}
	}

	public void setValue(float value) {
		this.value = value;
		onAction();
	}

	public float getMinValue() {
		return minValue;
	}

	public void setMinValue(float minValue) {
		this.minValue = minValue;
	}

	public float getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
	}

	public void onAction() {
		
	}
}
