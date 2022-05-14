package net.mattbenson.gui.framework.components;

import net.mattbenson.gui.framework.MenuComponent;
import net.mattbenson.gui.framework.MenuPriority;

public class MenuNoDrag extends MenuComponent {
	protected boolean mouseDown = false;
		
	public MenuNoDrag(int x, int y, int width, int height){
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
	public boolean passesThrough() {
		if (disabled) {
			return true;
		}
		
		int x = this.getRenderX();
		int y = this.getRenderY();
		int mouseX = parent.getMouseX();
		int mouseY = parent.getMouseY();

		if(mouseDown) {
			if(mouseX >= x && mouseX <= x + width) {
				if(mouseY >= y && mouseY <= y + height) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	@Override
	public void onRender() {
		mouseDown = false;
	}
	
	public void onAction() {}
}