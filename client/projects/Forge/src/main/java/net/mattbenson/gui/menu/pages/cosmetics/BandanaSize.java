package net.mattbenson.gui.menu.pages.cosmetics;

public enum BandanaSize {
	SMALL("SMALL"), MEDIUM("MEDIUM"), LARGE("LARGE");
	
	String type;
	
	BandanaSize(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return type;
	}
}
