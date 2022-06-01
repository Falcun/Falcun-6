package net.mattbenson.gui.menu.pages.hud;

public enum sizeType {
	SMALL("SMALL"), MEDIUM("MEDIUM"), LARGE("LARGE");
	
	String type;
	
	sizeType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
}
