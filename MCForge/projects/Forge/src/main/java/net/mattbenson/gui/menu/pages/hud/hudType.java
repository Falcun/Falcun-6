package net.mattbenson.gui.menu.pages.hud;

public enum hudType {
	IMAGE("IMAGE"), TEXT("TEXT");
	
	String type;
	
	hudType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
}
