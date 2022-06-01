package net.mattbenson.gui.menu.pages.cosmetics;

public enum CosmeticType {
	CAPES("CAPES"), BANDANAS("BANDANAS"), EMOTES("EMOTES"), FLAGS("FLAGS");
	
	String type;
	
	CosmeticType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return type;
	}
}
