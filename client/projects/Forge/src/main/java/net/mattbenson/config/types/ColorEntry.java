package net.mattbenson.config.types;

import java.awt.Color;
import java.lang.reflect.Field;

import org.json.JSONObject;

import net.mattbenson.config.ConfigEntry;

public class ColorEntry extends ConfigEntry {
	public ColorEntry(Field field, String key, String description, boolean visible) {
		super(field, key, description, visible);
	}
	
	@Override
	public Class<?> getType() {
		return Color.class;
	}
	
	@Override
	public void appendToConfig(String key, Object value, JSONObject config) {
		if(!(value instanceof Color)) {
			return;
		}
		
		Color color = (Color) value;
		JSONObject obj = new JSONObject();
		
		obj.put("red", color.getRed());
		obj.put("green", color.getGreen());
		obj.put("blue", color.getBlue());
		obj.put("alpha", color.getAlpha());
		
		config.put(key, obj);
	}
}
