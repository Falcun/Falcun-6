package net.mattbenson.config.types;

import java.lang.reflect.Field;

import org.json.JSONObject;

import net.mattbenson.config.ConfigEntry;

public class FloatEntry extends ConfigEntry {
	private float min;
	private float max;
	
	public FloatEntry(Field field, float min, float max, String key, String description, boolean visible) {
		super(field, key, description, visible);
		
		this.min = min;
		this.max = max;
	}
	
	@Override
	public Class<?> getType() {
		return float.class;
	}
	
	@Override
	public void appendToConfig(String key, Object value, JSONObject config) {
		if(!(value instanceof Float)) {
			return;
		}
		
		config.put(key, Float.valueOf(String.valueOf(value)));
	}
	
	public float getMin() {
		return min;
	}
	
	public float getMax() {
		return max;
	}
}
