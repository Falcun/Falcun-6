package net.mattbenson.gui.menu.pages.hud;

public enum opacityType {
	HUNDRED("HUNDRED"), NINTY("NINTY"), EIGHTY("EIGHTY"), SEVENTY("SEVENTY"), SIXTY("SIXTY"), FIFTY("FIFTY"), FORTY("FORTY"), THIRTY("THIRTY"), TWENTY("TWENTY"), TEN("TEN");
	
	String type;
	
	opacityType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
}
