package net.mattbenson.config.types;

import java.lang.reflect.Field;

import org.json.JSONObject;

import net.mattbenson.config.ConfigEntry;

public class IntEntry extends ConfigEntry {
	private int min;
	private int max;
	private boolean isKeyBind;
	
	public IntEntry(Field field, int min, int max, String key, String description, boolean visible) {
		super(field, key, description, visible);
		
		this.min = min;
		this.max = max;
	}
	
	public IntEntry(Field field, String key, String description, boolean visible) {
		super(field, key, description, visible);
		this.isKeyBind = true;
	}
	
	@Override
	public Class<?> getType() {
		return int.class;
	}
	
	@Override
	public void appendToConfig(String key, Object value, JSONObject config) {
		if(!(value instanceof Integer)) {
			return;
		}
		
		config.put(key, Integer.valueOf(String.valueOf(value)));
	}
	
	public int getMin() {
		return min;
	}
	
	public int getMax() {
		return max;
	}
	
	public boolean isKeyBind() {
		return isKeyBind;
	}
}
