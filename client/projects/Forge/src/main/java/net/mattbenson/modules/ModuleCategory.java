package net.mattbenson.modules;

public enum ModuleCategory {
	ALL_MODS(0, "ALL MODS", "/gui/maximize.png", false),
	MODS(1, "MODS", "/gui/user.png", false),
	RENDER(2, "RENDER", "/gui/laptop.png", false),
	FACTIONS(3, "FACTIONS", "/gui/badge.png", false),
	OTHER(4, "OTHER", "/gui/other.png", false),
	HIDDEN(5, "", "", true),
	
	FPS_SETTINGS(6, "", "", true),
	
	GROUPS2(7, "GROUPS", "/gui/groups.png", true),
	GROUPS(8, "GROUPS", "/gui/groups.png", false);
	
	private int index;
	private String text;
	private String icon;
	private boolean hidden;
	
	ModuleCategory(int index, String text, String icon, boolean hidden) {
		this.index = index;
		this.text = text;
		this.icon = icon;
		this.hidden = hidden;
	}
	
	public int getIndex() {
		return index;
	}
	
	public String getText() {
		return text;
	}
	
	public String getIcon() {
		return icon;
	}
	
	public boolean isHidden() {
		return hidden;
	}
}
