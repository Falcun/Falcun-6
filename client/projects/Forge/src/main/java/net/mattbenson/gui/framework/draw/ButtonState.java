package net.mattbenson.gui.framework.draw;

public enum ButtonState {
	NORMAL(""), HOVER("Hover"), ACTIVE("Active"), POPUP("Popup"), DISABLED("Disabled"), HOVERACTIVE("HoverActive");

	String state;
	
	ButtonState(String state) {
		this.state = state;
	}
	
	@Override
	public String toString() {
		return state;
	}
}
