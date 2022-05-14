package net.mattbenson.gui.menu;

public enum Category {
	MODS("MODS"), MACROS("MACROS"), WAYPOINTS("WAYPOINTS"), PROFILES("PROFILES"), COSMETICS("COSMETICS"), CONSOLE("CONSOLE"), FPS("FPS"), GROUPS("GROUPS"), HUD("HUD");
	
	private String name;
	
	Category(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
