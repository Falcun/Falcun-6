package net.mattbenson.gui.framework;

public enum MenuPriority {
	LOWEST(0), LOW(1), MEDIUM(2), HIGH(3), HIGHEST(4), SCROLLPANE(5);
	
	int priority;
	
	MenuPriority(int priority) {
		this.priority = priority;
	}
	
	public int getPriority() {
		return priority;
	}
}
