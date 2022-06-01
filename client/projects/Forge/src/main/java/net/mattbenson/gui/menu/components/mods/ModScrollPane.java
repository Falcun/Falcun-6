package net.mattbenson.gui.menu.components.mods;

import java.awt.Color;
import java.util.Collections;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.mattbenson.gui.framework.MenuComponent;
import net.mattbenson.gui.framework.MenuPriority;
import net.mattbenson.gui.framework.components.MenuCheckbox;
import net.mattbenson.gui.framework.components.MenuColorPicker;
import net.mattbenson.gui.framework.components.MenuComboBox;
import net.mattbenson.gui.framework.components.MenuDraggable;
import net.mattbenson.gui.framework.components.MenuDropdown;
import net.mattbenson.gui.framework.components.MenuLabel;
import net.mattbenson.gui.framework.components.MenuScrollPane;
import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;
import net.minecraft.client.Minecraft;

public class ModScrollPane extends MenuScrollPane {
	private boolean fullHeightScroller;
	
	public ModScrollPane(int x, int y, int width, int height, boolean fullHeightScroller) {
		super(x, y, width, height);
		
		this.fullHeightScroller = fullHeightScroller;
	}
	
	@Override
	public void onInitColors() {
		super.onInitColors();
		
		setColor(DrawType.BACKGROUND, ButtonState.POPUP, new Color(18, 17, 22, 255));
		setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(18, 17, 22, 255));
		setColor(DrawType.BACKGROUND, ButtonState.ACTIVE, new Color(25, 24, 29, 255));
		setColor(DrawType.BACKGROUND, ButtonState.HOVER, new Color(25, 24, 29, 255));
		setColor(DrawType.BACKGROUND, ButtonState.HOVERACTIVE, new Color(25, 24, 29, 255));
		setColor(DrawType.BACKGROUND, ButtonState.DISABLED, new Color(100, 100, 100, 255));	
	
		setColor(DrawType.LINE, ButtonState.POPUP, new Color(25, 24, 29, 255));
		setColor(DrawType.LINE, ButtonState.NORMAL, new Color(23, 23, 25, 255));
		setColor(DrawType.LINE, ButtonState.ACTIVE, new Color(35, 35, 38, 255));
		setColor(DrawType.LINE, ButtonState.HOVER, new Color(35, 35, 38, 255));
		setColor(DrawType.LINE, ButtonState.HOVERACTIVE, new Color(35, 35, 38, 255));
		setColor(DrawType.LINE, ButtonState.DISABLED, new Color(100, 100, 100, 255));
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
						if(desiredChange + (scrollAmount) <= 0) {
							desiredChange += (scrollAmount);
						} else {
							desiredChange = 0;
						}
					} else if(scroll < 0) {
						if(desiredChange - (scrollAmount) >= -maxY) {
							desiredChange -= (scrollAmount);
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
		
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GL11.glScissor(getRenderX() - 5, Minecraft.getMinecraft().displayHeight - (getRenderY() + getHeight()), getWidth() + 5, getHeight() - 1);
		
		for(MenuComponent component : components) {
			boolean inViewport = component.getRenderY() >= y - component.getHeight() && component.getRenderY() <= y + component.getHeight() + height;
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
		
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		GL11.glPopMatrix();
		
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
		
		float scrollerDelta = (float)-theY / (scrollerHeight + maxY);
		int newY = scrollerY + Math.round(scrollerHeight * scrollerDelta);
		
		if(newSize > 4) {
			if(scrollerSizeDelta < 1) {
				scrollerHeight -= 3;
				drawRectFalcun(scrollerX - 3, y - 2, scrollerWidth + 6, scrollerHeight + 8, 83886080);
				drawRectFalcun(scrollerX - 2, y - 1, scrollerWidth + 4, scrollerHeight + 6, 369098752);
				drawRectFalcun(scrollerX - 1, y, scrollerWidth + 2, scrollerHeight + 4, 587202560);
		
				drawRectFalcun(scrollerX, y + 1, scrollerWidth, scrollerHeight + 2, getColor(DrawType.LINE, ButtonState.NORMAL));
				drawRectFalcun(scrollerX + 1, y + 2, scrollerWidth - 2, scrollerHeight, getColor(DrawType.BACKGROUND, ButtonState.NORMAL));
		
				drawRectFalcun(scrollerX - 3, newY - 3, scrollerWidth + 6, newSize + 6, 83886080);
				drawRectFalcun(scrollerX - 2, newY - 2, scrollerWidth + 4, newSize + 4, 369098752);
				drawRectFalcun(scrollerX - 1, newY - 1, scrollerWidth + 2, newSize + 2, 587202560);
				
				drawRectFalcun(scrollerX, newY, scrollerWidth, newSize, getColor(DrawType.LINE, scrollerState));
				drawRectFalcun(scrollerX + 1, newY + 1, scrollerWidth - 2, newSize - 2, getColor(DrawType.BACKGROUND, scrollerState));
			}
		}
		
		mouseDown = false;
		
		if(wantsToDrag) {
			dragging = Mouse.isButtonDown(0);
			wantsToDrag = dragging; 
		}
	}
	
	public void setFullHeightScroller(boolean fullHeightScroller) {
		this.fullHeightScroller = fullHeightScroller;
	}
}
