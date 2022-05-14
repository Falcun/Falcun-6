package net.mattbenson.gui.framework.components;

import java.awt.Color;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Mouse;

import net.mattbenson.gui.framework.MenuComponent;
import net.mattbenson.gui.framework.MenuPriority;
import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;

public class MenuScrollPane extends MenuComponent {
	protected List<MenuComponent> components;
	protected String text;
	protected boolean mouseDown = false;
	protected boolean dragging = false;
	protected boolean mouseDragging = false;
	protected boolean wantsToDrag = false;
	protected int minOffset = 5;
	protected int theY = 0;
	protected int scrollAmount = 75;
	protected int scrollerWidth = 10; 
	protected int scrollerHeight = 5;
	protected int scroll = 0;
	protected ButtonState lastState = ButtonState.NORMAL;

	public MenuScrollPane(String text, int x, int y, int width, int height){
		super(x, y, width, height);
		this.text = text;
		components = new CopyOnWriteArrayList<>();
	}
	
	public MenuScrollPane(int x, int y, int width, int height){
		super(x, y, width, height);
		this.text = "";
		components = new CopyOnWriteArrayList<>();
	}
	

	@Override
	public void onInitColors() {
		setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(35, 35, 35, 255));
		setColor(DrawType.BACKGROUND, ButtonState.ACTIVE, new Color(65, 65, 65, 255));
		setColor(DrawType.BACKGROUND, ButtonState.HOVER, new Color(50, 50, 50, 255));
		setColor(DrawType.BACKGROUND, ButtonState.HOVERACTIVE, new Color(65, 65, 65, 255));
		setColor(DrawType.BACKGROUND, ButtonState.DISABLED, new Color(100, 100, 100, 255));	
		
		setColor(DrawType.LINE, ButtonState.NORMAL, new Color(35, 35, 35, 255));
		setColor(DrawType.LINE, ButtonState.ACTIVE, new Color(65, 65, 65, 255));
		setColor(DrawType.LINE, ButtonState.HOVER, new Color(50, 50, 50, 255));
		setColor(DrawType.LINE, ButtonState.HOVERACTIVE, new Color(65, 65, 65, 255));
		setColor(DrawType.LINE, ButtonState.DISABLED, new Color(100, 100, 100, 255));

		setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(200, 200, 200, 255));
		setColor(DrawType.TEXT, ButtonState.ACTIVE, new Color(235, 235, 235, 255));
		setColor(DrawType.TEXT, ButtonState.HOVER, new Color(225, 225, 225, 255));
		setColor(DrawType.TEXT, ButtonState.HOVERACTIVE, new Color(235, 235, 235, 255));
		setColor(DrawType.TEXT, ButtonState.DISABLED, new Color(255, 255, 255, 255));
	}
	
	@Override
	public void onMouseClick(int button) {
		if(button == 0) {
			if(getPriority().getPriority() <= MenuPriority.MEDIUM.getPriority()) {
				mouseDown = true;
			}
		}
		
		Collections.sort(components, (a, b) -> Integer.compare(a.getPriority().getPriority(), b.getPriority().getPriority()));
		Collections.reverse(components);
		
		for(MenuComponent component : components) {
			if(component.getParent() == null) {
				component.setParent(getParent());
			}
			
			if((component.getRenderY() >= getRenderY() - component.getHeight() && component.getRenderY() <= getRenderY()  + height) || !component.passesThrough()) {
				if((parent.getMouseY() > getRenderY() && parent.getMouseY() < getRenderY() + this.height) || !component.passesThrough()) {
					component.onMouseClick(button);
				}
			}
		}
	}
	
	@Override
	public void onMouseClickMove(int button) {	
		Collections.sort(components, (a, b) -> Integer.compare(a.getPriority().getPriority(), b.getPriority().getPriority()));
		Collections.reverse(components);
		
		for(MenuComponent component : components) {
			if(component.getParent() == null) {
				component.setParent(getParent());
			}
			
			if(component.getRenderY() >= getRenderY() -component.getHeight() && component.getRenderY() <= getRenderY()  + height) {
				if(parent.getMouseY() > getRenderY() && parent.getMouseY() < getRenderY() + this.height || wantsToDrag || !component.passesThrough()) {
					component.onMouseClickMove(button);
				}
			}
		}
		
		mouseDragging = true;
	}
	
	@Override
	public void onKeyDown(char character, int key) {
		for(MenuComponent component : components) {
			if(component.getParent() == null) {
				component.setParent(getParent());
			}
			
			if((component.getRenderY() >= getRenderY() && component.getRenderY() + component.getHeight() <= getRenderY() + height) || !component.passesThrough()) {
				component.onKeyDown(character, key);
			}
		}
	}
	
	@Override
	public boolean onExitGui(int key) {
		boolean cancel = false;
		
		for(MenuComponent component : components) {
			if(component.getParent() == null) {
				component.setParent(getParent());
			}
			
			if((component.getRenderY() >= y && component.getRenderY() + component.getHeight() <= y + height) || !component.passesThrough()) {
				if(component.onExitGui(key))
					cancel = true;
			}
		}
		
		return cancel;
	}
	
	@Override
	public void onMouseScroll(int scroll) {
		this.scroll = scroll;
	}
	
	@Override
	public boolean passesThrough() {
		if (disabled) {
			return true;
		}	
		int x = this.getRenderX();
		int y = this.getRenderY();
		int mouseX = parent.getMouseX();
		int mouseY = parent.getMouseY();
		
		if(mouseDown || mouseDragging) {
			if(mouseX >= x && mouseX <= x + width) {
				if(mouseY >= y && mouseY <= y + height) {
					return false;
				}
			}
		}
		
		boolean passesThrough = true;
		for(MenuComponent component : components) {
			if(component.getParent() == null) {
				component.setParent(getParent());
			}
			
			if(!component.passesThrough()) {
				passesThrough = false;
			}
		}
		
		return !wantsToDrag && passesThrough;
	}
	
	@Override
	public void onPreSort() {
		ButtonState state = ButtonState.NORMAL;
		
		if(disabled) {
			state = ButtonState.DISABLED;
		}
		
		MenuPriority highest = MenuPriority.LOWEST;
		
		if(wantsToDrag) {
			highest = MenuPriority.HIGH;
		}
		
		for(MenuComponent component : components) {
			if(component.getParent() == null) {
				component.setParent(getParent());
			}
			
			if(component.getPriority().getPriority() > highest.getPriority()) {
				highest = component.getPriority();
			}
		}
		
		setPriority(highest);
		
		lastState = state;
	}
	
	@Override
	public void onRender() {
		int x = this.getRenderX();
		int y = this.getRenderY();
		final int mouseX = parent.getMouseX();
		final int mouseY = parent.getMouseY();
		int height = this.height;
		
		Collections.sort(components, (a, b) -> Integer.compare(a.getPriority().getPriority(), b.getPriority().getPriority()));
		int maxY = 0;
		
		for(MenuComponent component : components) {
			if(component.getParent() == null)
				component.setParent(getParent());
			
			int tempY = component.getY() + component.getHeight();
			
			if(tempY > maxY)
				maxY = tempY;
		}
		
		maxY -= height;
		
		maxY += 3;
		
		int backgroundColor = getColor(DrawType.BACKGROUND, lastState);
		int lineColor = getColor(DrawType.LINE, lastState);
		int textColor = getColor(DrawType.TEXT, lastState);

		drawRectFalcun(x + 1, y + 1, width - 1, height - 1, backgroundColor);
		
		int scrollerX = x + width - scrollerWidth;
		int scrollerY = y + 1;
		int scrollerHeight = height - 1;
		ButtonState scrollerState = ButtonState.HOVER;
		
		if((mouseX >= scrollerX && mouseX <= scrollerX + scrollerWidth) || (wantsToDrag && dragging)) {
			if((mouseY >= scrollerY && mouseY <= scrollerY + scrollerHeight) || (wantsToDrag && dragging)) {
				scrollerState = ButtonState.ACTIVE;
				
				if(!wantsToDrag) {
					wantsToDrag = mouseDown;
				}
			}
		}
		
		int desiredChange = theY;
		
		float scrollerSizeDelta = (float)height / (maxY + height);
		
		if(scrollerSizeDelta <= 1) {
			if (mouseX >= x && mouseX <= x + width) {
				if (mouseY >= y && mouseY <= y + height) {
					
					if(scroll > 0) {
						if(desiredChange + (scrollAmount * 5) <= 0) {
							desiredChange += (scrollAmount * 5);
						} else {
							desiredChange = 0;
						}
					} else if(scroll < 0) {
						if(desiredChange - (scrollAmount * 5) >= -maxY) {
							desiredChange -= (scrollAmount * 5);
						} else {
							desiredChange = -maxY;
						}
					}
				}
			}
		}
		
		int newSize = Math.round(scrollerSizeDelta * scrollerHeight);
		
		if(scrollerSizeDelta > 1) {
			newSize = 0;
		}
		
		for(MenuComponent component : components) {
			if(component.getParent() == null) {
				component.setParent(getParent());
			}
			
			component.setRenderOffsetX(x);
			component.setRenderOffsetY(y + theY);
			
			if(component instanceof MenuColorPicker) {
				component.setRenderOffsetX(-component.getX() + x + width - component.getWidth() - 10);
			}
			
			if(component.getWidth() > width) {
				component.setWidth(width - 1);
			}
			
			if(component.getWidth() > width - scrollerWidth && scrollerSizeDelta < 1) {
				component.setWidth(width - scrollerWidth - 1);
			}
		}
		
		
		Collections.sort(components, (a, b) -> Integer.compare(a.getPriority().getPriority(), b.getPriority().getPriority()));
		Collections.reverse(components);
		
		int passThroughIndex = -1;
		int index = components.size();
		
		for(MenuComponent component : components) {
			if(!component.passesThrough() && passThroughIndex == -1)
				passThroughIndex = index;
			
			index--;
		}
		
		Collections.reverse(components);	
		
		final int oldIndex = index;
		
		index = oldIndex;
		
		for(MenuComponent component : components) {
			boolean inViewport = component.getRenderY() >= y && component.getRenderY() <= y + height;
			boolean override = !component.passesThrough();
			
			if(!inViewport && !override) {
				component.onPreSort();
			}
			
			if(inViewport || override) {
				if(index >= passThroughIndex - 1){
					parent.setMouseX(mouseX);
					parent.setMouseY(mouseY);
					
					if(wantsToDrag || (parent.getMouseY() <= getRenderY() || parent.getMouseY() >= getRenderY() + this.height) && !wantsToDrag && !((component.getPriority() == getPriority() && getPriority().getPriority() > MenuPriority.HIGH.getPriority()))) {
						parent.setMouseX(Integer.MAX_VALUE);
						parent.setMouseY(Integer.MAX_VALUE);
					}
				} else if(component instanceof MenuDraggable) {
					index++;
					continue;
				} else {
					parent.setMouseX(Integer.MAX_VALUE);
					parent.setMouseY(Integer.MAX_VALUE);
				}
				
				component.onPreSort();
				component.onRender();
			}
			
			index++;
		}
		
		if(passThroughIndex == -1) {
			theY = desiredChange;
			
			if(wantsToDrag && (mouseDown || dragging) && scrollerSizeDelta < 1) {
				float scrollerDelta = (float)(mouseY - (y + minOffset * 2)) / (height - minOffset * 4);
				
				if(scrollerDelta > 1) {
					scrollerDelta = 1;
				} else if(scrollerDelta < 0) {
					scrollerDelta = 0;
				}
				
				theY = Math.round(-scrollerDelta * maxY);
			}
		} else {
			if(scrollerState == ButtonState.ACTIVE) {
				scrollerState = ButtonState.HOVER;
			}
		}
		
		drawRectFalcun(x, y, width, minOffset * 2, backgroundColor);
		
		if(text.length() > 0) {
			int minus = getStringWidth(text) + minOffset * 3;
			
			drawText(text, x + 5, y + getStringHeight(text) / 2, textColor);
			drawHorizontalLine(x + minus, y, width - minus + 1, 1, lineColor);
			drawHorizontalLine(x, y, minOffset, 1, lineColor);
		} else {
			drawHorizontalLine(x, y, width + 1, 1, lineColor);
		}
				
		drawVerticalLine(x, y + 1, height - 1, 1, lineColor);
		drawHorizontalLine(x, y + height, width + 1, 1, lineColor);
		drawVerticalLine(x + width, y + 1, height - 1, 1, lineColor);
		
		float scrollerDelta = (float)-theY / (scrollerHeight + maxY);
		int newY = scrollerY + Math.round(scrollerHeight * scrollerDelta);
		
		if(newSize > 4) {
			drawRectFalcun(scrollerX, newY + 1, scrollerWidth, newSize - 4, getColor(DrawType.BACKGROUND, scrollerState));
		}
		
		mouseDown = false;
		
		if(wantsToDrag) {
			dragging = Mouse.isButtonDown(0);
			wantsToDrag = dragging; 
		}
	}
	
	public void drawExtras() {
		for(MenuComponent component : components) {
			if(component instanceof MenuColorPicker) {
				MenuColorPicker picker = (MenuColorPicker) component;
				picker.drawPicker();
			} else if(component instanceof MenuLabel) {
				MenuLabel label = (MenuLabel) component;
				label.drawTooltip();
			} else if(component instanceof MenuCheckbox) {
				MenuCheckbox checkbox = (MenuCheckbox) component;
				checkbox.drawTooltip();
			} else if(component instanceof MenuComboBox) {
				MenuComboBox box = (MenuComboBox) component;
				box.drawDropdown();
			} else if(component instanceof MenuDropdown) {
				MenuDropdown dropdown = (MenuDropdown) component;
				dropdown.drawDropdown();
			}
		}
	}

	public void addComponent(MenuComponent component) {
		theY = 0;
		components.add(component);
	}
	
	public List<MenuComponent> getComponents() {
		return components;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void onAction() {}
}	