package net.mattbenson.gui.hud;

import net.mattbenson.modules.Module;

public abstract class HUDElement implements IHUD {
	private Module parent;
	private String identifier;
	private int x;
	private int y;
	private int width;
	private int height;
	private double scale;
	private boolean visible;
	
	public HUDElement(String identifier, int width, int height) {
		this.identifier = identifier;
		this.width = width;
		this.height = height;
		this.scale = 1;
		this.visible = true;
	}
	
	public Module getParent() {
		return parent;
	}

	public void setParent(Module parent) {
		this.parent = parent;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
