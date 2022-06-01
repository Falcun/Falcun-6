package net.mattbenson.gui.framework.components;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.mattbenson.gui.framework.MenuComponent;
import net.mattbenson.gui.framework.MenuPriority;
import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;

public class MenuComboBox extends MenuComponent {
	protected String[] values;
	protected String lastValueString = "";
	protected boolean[] items;
	protected boolean open = false;
	protected boolean mouseDown = false;
	protected boolean mouseDragging = false;
	protected int textOffset = 25;
	protected int totalHeight = 0;
	protected int maxHeight = 0;
	protected int maxWidth = 0;
	protected int arrowOffset = 15;
	protected ButtonState lastState = ButtonState.NORMAL;
	
	public MenuComboBox(String[] options, int x, int y) {
		super(x, y, 0, 0);
		
		values = options;
		items = new boolean[values.length];
		
		if(values.length == 0) {
			disabled = true;
		}
		
		if(!disabled) {
			for(String value : values) {
				int tWidth = getStringWidth(value);
				int tHeight = getStringHeight(value);
				
				totalHeight += tHeight;
				
				if(maxHeight < tHeight) {
					maxHeight = tHeight;
				}
				
				if(maxWidth < tWidth) {
					maxWidth = tWidth;
				}
			}
		}
		
		this.width = maxWidth;
		this.height = maxHeight + 1;
	}
	

	
	public MenuComboBox(Class<?> theEnum, int x, int y) {
		super(x, y, 0, 0);
		
		if(theEnum.isEnum()) {
			List<String> tempVals = new ArrayList<>();
			
			for(Object object : theEnum.getEnumConstants()) {
				String string = String.valueOf(object);
				tempVals.add((string.toUpperCase().substring(0, 1) + string.toLowerCase().substring(1, string.length())).replaceAll("_", " "));
			}
			
			values = tempVals.toArray(new String[tempVals.size()]);
			items = new boolean[values.length];
		}
		
		if(values.length == 0) {
			disabled = true;
		}
		
		if(!disabled) {
			for(String value : values) {
				int tWidth = getStringWidth(value);
				int tHeight = getStringHeight(value);
				
				totalHeight += tHeight;
				
				if(maxHeight < tHeight) {
					maxHeight = tHeight;
				}
				
				if(maxWidth < tWidth) {
					maxWidth = tWidth;
				}
			}
		}
		
		this.width = maxWidth;
		this.height = maxHeight + 1;
	}
	
	@Override
	public void onInitColors() {
		setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(35, 35, 35, 255));
		setColor(DrawType.BACKGROUND, ButtonState.ACTIVE, new Color(65, 65, 65, 255));
		setColor(DrawType.BACKGROUND, ButtonState.HOVER, new Color(50, 50, 50, 255));
		setColor(DrawType.BACKGROUND, ButtonState.HOVERACTIVE, new Color(65, 65, 65, 255));
		setColor(DrawType.BACKGROUND, ButtonState.DISABLED, new Color(100, 100, 100, 255));	
		
		setColor(DrawType.LINE, ButtonState.NORMAL, new Color(10, 10, 10, 255));
		setColor(DrawType.LINE, ButtonState.ACTIVE, new Color(10, 10, 10, 255));
		setColor(DrawType.LINE, ButtonState.HOVER, new Color(20, 20, 20, 255));
		setColor(DrawType.LINE, ButtonState.HOVERACTIVE, new Color(20, 20, 20, 255));
		setColor(DrawType.LINE, ButtonState.POPUP, new Color(10, 10, 10, 255));
		setColor(DrawType.LINE, ButtonState.DISABLED, new Color(100, 100, 100, 255));

		setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(200, 200, 200, 255));
		setColor(DrawType.TEXT, ButtonState.ACTIVE, new Color(100, 120, 255, 255));
		setColor(DrawType.TEXT, ButtonState.HOVER, new Color(225, 225, 225, 255));
		setColor(DrawType.TEXT, ButtonState.HOVERACTIVE, new Color(110, 130, 255, 255));
		setColor(DrawType.TEXT, ButtonState.DISABLED, new Color(255, 255, 255, 255));
	}
	
	@Override
	public boolean onExitGui(int key) {
		if(open) {
			open = false;
		}
		
		return false;
	}

	@Override
	public void onMouseClick(int button) {
		if(button == 0) {
			mouseDown = true;
		}
	}
	
	@Override
	public void onMouseClickMove(int button) {
		if(button == 0) {
			mouseDragging = true;
		}
	}
		
	@Override
	public boolean passesThrough() {
		if(disabled) {
			return true;
		}
		
		if(mouseDown) {
			int x = this.getRenderX();
			int y = this.getRenderY();
			int width = this.width + textOffset;
			int height = this.height;
			int mouseX = parent.getMouseX();
			int mouseY = parent.getMouseY();		
			
			if(mouseX >= x && mouseX <= x + width + arrowOffset - 1) {
				if(mouseY >= y && mouseY <= y + height) {
					return false;
				}
			}
		}
		
		return !open;
	}
	
	@Override
	public void onPreSort() {
		int x = this.getRenderX();
		int y = this.getRenderY();
		int width = this.width + textOffset;
		int mouseX = parent.getMouseX();
		int mouseY = parent.getMouseY();

		ButtonState state = open ? ButtonState.ACTIVE : ButtonState.NORMAL;

		if (!disabled) {
			if (mouseX >= x && mouseX <= x + width + arrowOffset - 1) {
				if (mouseY >= y && mouseY <= y + height + 1) {
					state = open ? ButtonState.HOVERACTIVE : ButtonState.HOVER;
					
					if(mouseDown) {
						open = true;
					}
				}
			}
		} else {
			state = ButtonState.DISABLED;
		}
		
		if(open) {
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
		int width = this.width + textOffset;
		int backgroundColor = getColor(DrawType.BACKGROUND, lastState);
		int lineColor = getColor(DrawType.LINE, lastState);
		int textColor = getColor(DrawType.TEXT, ButtonState.NORMAL);
		
		drawRectFalcun(x + 1, y + 1, width - 2 + arrowOffset, height - 1, backgroundColor);
		
		drawHorizontalLine(x, y, width + 1, 1, lineColor);
		drawVerticalLine(x, y + 1, height - 1, 1, lineColor);
		drawHorizontalLine(x, y + height, width + 1, 1, lineColor);
		drawVerticalLine(x + width, y + 1, height - 1, 1, lineColor);
		drawHorizontalLine(x + width, y, arrowOffset, 1, lineColor);
		drawHorizontalLine(x + width, y + height, arrowOffset, 1, lineColor);
		drawVerticalLine(x + width + arrowOffset - 1, y + 1, height - 1, 1, lineColor);
		
		int arrowX = x + width - 1 + arrowOffset / 2;
		int arrowY = y + (height - 1) / 2;
		
		if(open) {
			drawHorizontalLine(arrowX - 1, arrowY + 1, 5, 1, textColor);
			drawHorizontalLine(arrowX - 2, arrowY + 2, 7, 1, textColor);
			drawHorizontalLine(arrowX - 3, arrowY + 3, 9, 1, textColor);
			drawHorizontalLine(arrowX, arrowY, 3, 1, textColor);
			drawPixel(arrowX + 1, arrowY - 1, textColor);
		} else {
			drawHorizontalLine(arrowX - 3, arrowY - 1, 9, 1, textColor);
			drawHorizontalLine(arrowX - 2, arrowY, 7, 1, textColor);
			drawHorizontalLine(arrowX - 1, arrowY + 1, 5, 1, textColor);
			drawHorizontalLine(arrowX, arrowY + 2, 3, 1, textColor);
			drawPixel(arrowX + 1, arrowY + 3, textColor);
		}
		
		drawText(lastValueString, x + 1 + (width / 2) - (getStringWidth(lastValueString) / 2), y + 1, textColor);
		drawDropdown();
		mouseDown = false;
	}
		
	public void drawDropdown() {
		if (open) {
			int x = getRenderX();
			int y = getRenderY();
			int mouseX = parent.getMouseX();
			int mouseY = parent.getMouseY();
			int backgroundColor = getColor(DrawType.BACKGROUND, ButtonState.POPUP);
			int lineColor = getColor(DrawType.LINE, ButtonState.POPUP);
			int width = this.width + textOffset + arrowOffset - 1;
			int height = totalHeight + values.length;
			
			drawRectFalcun(x, y + this.height + 1, width, height, backgroundColor);
			drawHorizontalLine(x, y + this.height + 1, width + 1, 1, lineColor);
			drawVerticalLine(x, y + this.height + 2, height - 1, 1, lineColor);
			drawHorizontalLine(x, y + this.height + 1 + height, width + 1, 1, lineColor);
			drawVerticalLine(x + width, y + this.height + 2, height - 1, 1, lineColor);
			
			y += 2;
			
			boolean inHover = false;
			
			for(int i = 0; i < values.length; i++) {
				String value = values[i];
				int sHeight = getStringHeight(value);
				
				y += sHeight + 1;
				boolean hover = false;
				
				if (mouseX >= x + 1 && mouseX <= x + width - 1) {
					if (mouseY >= y && mouseY <= y + sHeight) {
						hover = true;
						drawRectFalcun(x + 1, y, width - 1, sHeight, getColor(DrawType.BACKGROUND, ButtonState.HOVERACTIVE));
						
						if(mouseDown) {
							items[i] = !items[i];
							
							String label = getValuesFormatted();
							int labelWidth = getStringWidth(label);
							
							while(labelWidth >= width - arrowOffset) {
								label = label.substring(0, label.length() - 1);
								labelWidth = getStringWidth(label);
							}
							
							lastValueString = label;
							onAction();
						}
						
						inHover = true;
					}
				}
				
				drawText(value, x + 1 + (width / 2) - (getStringWidth(value) / 2), y, getColor(DrawType.TEXT, (items[i] ? (hover ? ButtonState.HOVERACTIVE : ButtonState.ACTIVE) : (hover ? ButtonState.HOVER : ButtonState.NORMAL))));
			}
			
			if(open && !inHover && mouseDown && lastState != ButtonState.HOVER && lastState != ButtonState.HOVERACTIVE) {
				open = false;
			}
		}
	}
	
	public String getValuesFormatted() {
		String[] values = getValues();
		StringBuilder builder = new StringBuilder();
		
		for(String value : values) {
			if(builder.length() > 0) {
				builder.append(", ");
			}
			
			builder.append(value.replace('_', ' '));
		}
		
		return builder.toString();
	}
	
	public String[] getValues() {
		List<String> values = new ArrayList<>();
		
		for(int i = 0; i < this.values.length; i++) {
			if(items[i]) {
				values.add(this.values[i].replace(' ', '_'));
			}
		}
		
		return values.toArray(new String[values.size()]);
	}
	
	public void setValues(String[] values) {
		if(values == null) {
			return;
		}
		
		for(int i = 0; i < this.values.length; i++) {
			for(int ii = 0; ii < values.length; ii++) {
				if(values[ii].equalsIgnoreCase(this.values[i].replace(' ', '_'))) {
					items[i] = true;
					break;
				} else if(items[i]) {
					items[i] = false;
				}
			}
		}
		
		String label = getValuesFormatted();
		int labelWidth = getStringWidth(label);
		
		while(labelWidth >= this.width + textOffset) {
			label = label.substring(0, label.length() - 1);
			labelWidth = getStringWidth(label);
		}
		
		lastValueString = label;
	}
	
	public void onAction() {}
}