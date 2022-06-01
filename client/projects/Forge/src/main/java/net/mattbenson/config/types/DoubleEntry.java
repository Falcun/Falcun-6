package net.mattbenson.config.types;

import java.lang.reflect.Field;

import org.json.JSONObject;

import net.mattbenson.config.ConfigEntry;

public class DoubleEntry extends ConfigEntry {
	private double min;
	private double max;
	
	public DoubleEntry(Field field, double min, double max, String key, String description, boolean visible) {
		super(field, key, description, visible);
		
		this.min = min;
		this.max = max;
	}
	
	@Override
	public Class<?> getType() {
		return double.class;
	}
	
	@Override
	public void appendToConfig(String key, Object value, JSONObject config) {
		if(!(value instanceof Double)) {
			return;
		}
		
		config.put(key, Double.valueOf(String.valueOf(value)));
	}
	
	public double getMin() {
		return min;
	}
	
	public double getMax() {
		return max;
	}
}
