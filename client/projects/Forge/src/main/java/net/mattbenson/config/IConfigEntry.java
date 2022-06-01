package net.mattbenson.config;

import org.json.JSONObject;

public interface IConfigEntry {
	Class<?> getType();
	void appendToConfig(String key, Object value, JSONObject config);
}
