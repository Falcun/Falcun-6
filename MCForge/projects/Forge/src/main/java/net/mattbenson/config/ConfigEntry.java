package net.mattbenson.config;

import java.lang.reflect.Field;

import net.mattbenson.Falcun;

public abstract class ConfigEntry implements IConfigEntry {
	private Field field;
	private String key;
	private String description;
	private boolean visible;
	
	public ConfigEntry(Field field, String key, String description, boolean visible) {
		this.field = field;
		this.key = key;
		this.description = description;
		this.visible = visible;
	}
	
	public Field getField() {
		return field;
	}
	
	public String getKey() {
		return key;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Object getValue(Object obj) {
		try {
			return getField().get(obj);
		} catch(IllegalArgumentException | IllegalAccessException e) {
			Falcun.getInstance().log.error("Failed to returning value for module " + obj + "," + key + ".", e);
		}
		
		return null;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public boolean hasDescription() {
		return description != null && description.length() > 0;
	}
	
	public void setValue(Object obj, Object value) {
		try {
			field.set(obj, value);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			Falcun.getInstance().log.error("Failed to set config value for module " + obj + ".", e);
		}
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public boolean isInstance(Object object) {
		if(object.getClass() == getType()) {
			return true;
		}
		
		return false;
	}
}
