package net.mattbenson.gui.framework;

public enum BindType {
	HOLD("Hold"), TOGGLE("Toggle");
	
	String type;
	
	BindType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return this.type;
	}
	
	public static BindType getBind(String type) {
		for(BindType bind : values()) {
			if(bind.type.equalsIgnoreCase(type)) {
				return bind;
			}
		}
		
		return TOGGLE;
	}
}
