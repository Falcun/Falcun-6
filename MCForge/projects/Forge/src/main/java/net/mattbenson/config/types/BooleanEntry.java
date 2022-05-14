package net.mattbenson.config.types;

import java.lang.reflect.Field;

import org.json.JSONObject;

import net.mattbenson.config.ConfigEntry;

public class BooleanEntry extends ConfigEntry {
	public BooleanEntry(Field field, String key, String description, boolean visible) {
		super(field, key, description, visible);
	}
	
	@Override
	public Class<?> getType() {
		return boolean.class;
	}
	
	@Override
	public void appendToConfig(String key, Object value, JSONObject config) {
		if(!(value instanceof Boolean)) {
			return;
		}
		
		config.put(key, Boolean.parseBoolean(String.valueOf(value)));
	}
}
