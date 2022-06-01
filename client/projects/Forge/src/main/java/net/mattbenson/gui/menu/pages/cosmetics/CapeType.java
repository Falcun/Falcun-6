package net.mattbenson.gui.menu.pages.cosmetics;

public enum CapeType {
	NORMAL("NORMAL"), BEND("BEND"), SHOULDERS("SHOULDERS"), BEND_AND_SHOULDERS("BEND & SHOULDERS");
	
	String type;
	
	CapeType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
}
