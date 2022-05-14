package net.mattbenson.gui.framework.draw;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class MenuColorType {
	Map<String, Color> colors;
	
	public MenuColorType() {
		colors = new HashMap<>();
	}
	
	public int getColor(DrawType type, ButtonState state) {
		if(colors.containsKey(type.toString() + state.toString())) 
			return colors.get(type.toString() + state.toString()).getRGB();
		else
			return colors.get(type.toString()).getRGB();
	}

	public void setColor(DrawType type, ButtonState state, Color color) {
		colors.put(type.toString() + state.toString(), color);
	}
}
