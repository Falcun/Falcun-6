package net.mattbenson.gui.framework.components;

import org.lwjgl.input.Mouse;

import net.mattbenson.gui.framework.MenuComponent;
import net.mattbenson.gui.framework.MenuPriority;

public class MenuDraggable extends MenuComponent {
	protected boolean mouseDown = false;
	protected boolean dragging = false;
	protected boolean wantToDrag = false;
	protected int xSaved = -1;
	protected int ySaved = -1;
	protected int lastX = -1;
	protected int lastY = -1;
		
	public MenuDraggable(int x, int y, int width, int height){
		super(x, y, width, height);
		setPriority(MenuPriority.LOW);
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
			dragging = true;
		}
	}
	
	@Override
	public void onPreSort() {
		if(dragging) {
			setPriority(MenuPriority.HIGHEST);
		} else {
			setPriority(MenuPriority.LOWEST);
		}
	}
	
	@Override
	public void onRender() {
		int x = this.getRenderX();
		int y = this.getRenderY();
		int mouseX = parent.getMouseX();
		int mouseY = parent.getMouseY();
		
		if(!disabled) {
			if(mouseX >= x && mouseX <= x + width) {
				if(mouseY >= y && mouseY <= y + height) {
					if(mouseDown && !wantToDrag) {
						wantToDrag = true;
						xSaved = parent.getX() - mouseX;
						ySaved = parent.getY() - mouseY;
					}
				}
			}
		}
		
		if((xSaved != x || ySaved != y) && xSaved != -1 && ySaved != -1) {
			if(lastX != mouseX || lastY != mouseY) {
				lastX = mouseX;
				lastY = mouseY;
				onAction();
				getParent().setLocation(mouseX + xSaved, mouseY + ySaved);
			}
		}
		
		if(wantToDrag) {
			dragging = Mouse.isButtonDown(0);
			wantToDrag = dragging;
			
			if(!wantToDrag) {
				xSaved = -1;
				ySaved = -1;
			}
		}
		
		mouseDown = false;
	}
	
	public void onAction() {}
}