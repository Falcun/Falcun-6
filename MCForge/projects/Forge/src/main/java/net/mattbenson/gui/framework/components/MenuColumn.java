package net.mattbenson.gui.framework.components;

import net.mattbenson.gui.framework.MenuComponent;

public class MenuColumn extends MenuComponent {		
	protected int column = 0;
	
	public MenuColumn(int column) {
		super(0, 0, 0, 0);
		this.column = column;
	}
	
	public int getColumn() {
		return column;
	}
	
	public void setColumn(int column) {
		this.column = column;
	}
}