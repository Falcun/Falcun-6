package net.mattbenson.config.types;

import java.lang.reflect.Field;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import net.mattbenson.Falcun;
import net.mattbenson.config.ConfigEntry;
import net.mattbenson.modules.Module;

public class ListEntry extends ConfigEntry {
	private Module module;
	private String[] values;
	
	public ListEntry(Module module, Field field, String[] values, String key, String description, boolean visible) {
		super(field, key, description, visible);
		
		this.module = module;
		this.values = values;
	}

	@Override
	public Class<?> getType() {
		return String[].class;
	}
	
	@Override
	public void appendToConfig(String key, Object value, JSONObject config) {
		if(!(value instanceof String)) {
			return;
		}
		
		try {
			config.put(key, String.valueOf(getField().get(module)));
		} catch (JSONException | IllegalArgumentException | IllegalAccessException e) {
			Falcun.getInstance().log.error("Failed to read list entry for " + module + ", " + getField() + ".", e);
		}
	}
	
	public String[] getValues() {
		return values;
	}
}
