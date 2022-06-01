package net.mattbenson.gui.menu.pages.groups;

public enum GroupSubTab {
	SETTINGS("SETTINGS"), SCHEMATICS("SCHEMATICS"), WAYPOINTS("WAYPOINTS"), ADMIN("ADMIN");
	
	String text;
	
	GroupSubTab(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
}
