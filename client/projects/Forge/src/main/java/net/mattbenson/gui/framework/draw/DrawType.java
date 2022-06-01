package net.mattbenson.gui.framework.draw;

public enum DrawType {
	LINE("line"), BACKGROUND("background"), TEXT("text");
	
	String type;
	
	DrawType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return type;
	}
}
