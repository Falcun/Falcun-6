package net.mattbenson.gui.framework;

import net.mattbenson.gui.framework.draw.DrawImpl;
import net.mattbenson.gui.framework.draw.MenuColorType;

public class MenuComponent extends MenuColorType implements DrawImpl, MenuComponentImpl {
	protected int x;
	protected int y;
	protected int renderOffsetX;
	protected int renderOffsetY;
	protected int width;
	protected int height;
	protected MenuPriority priority;
	protected boolean disabled;
	protected Menu parent;
	
	public MenuComponent(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.renderOffsetX = 0;
		this.renderOffsetY = 0;
		this.width = width;
		this.height = height;
		priority = MenuPriority.MEDIUM;
		disabled = false;
		onInitColors();
	}
	
	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getRenderOffsetX() {
		return renderOffsetX;
	}

	public void setRenderOffsetX(int renderOffsetX) {
		this.renderOffsetX = renderOffsetX;
	}

	public int getRenderOffsetY() {
		return renderOffsetY;
	}

	public void setRenderOffsetY(int renderOffsetY) {
		this.renderOffsetY = renderOffsetY;
	}

	public int getRenderX() {
		return x + renderOffsetX;
	}
	
	public int getRenderY() {
		return y + renderOffsetY;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean passesThrough() {
		return true;
	}
	
	public MenuPriority getPriority() {
		return priority;
	}

	public void setPriority(MenuPriority priority) {
		this.priority = priority;
	}

	public boolean isDisabled() {
		return disabled;
	}
	
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
	public void setParent(Menu parent) {	
		this.parent = parent;
	}
	
	public Menu getParent() {
		return parent;
	}
}
