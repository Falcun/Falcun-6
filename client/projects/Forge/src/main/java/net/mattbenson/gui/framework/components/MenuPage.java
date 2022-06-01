package net.mattbenson.gui.framework.components;

import net.mattbenson.gui.framework.MenuComponent;

public class MenuPage extends MenuComponent {		
	protected int page = 0;
	
	public MenuPage(int page) {
		super(0, 0, 0, 0);
		this.page = page;
	}
	
	public int getPage() {
		return page;
	}
	
	public void setPage(int page) {
		this.page = page;
	}
}