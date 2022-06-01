package net.mattbenson.config.types;

import java.lang.reflect.Field;

import org.json.JSONObject;

import net.mattbenson.config.ConfigEntry;

public class StringEntry extends ConfigEntry {
	public StringEntry(Field field, String key, String description, boolean visible) {
		super(field, key, description, visible);
	}
	
	@Override
	public Class<?> getType() {
		return String.class;
	}
	
	@Override
	public void appendToConfig(String key, Object value, JSONObject config) {
		if(!(value instanceof String)) {
			return;
		}
		
		config.put(key, String.valueOf(value));
	}
}
