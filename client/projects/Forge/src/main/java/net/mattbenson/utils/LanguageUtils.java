package net.mattbenson.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import net.mattbenson.Falcun;

public class LanguageUtils {
	private Map<String, String> mappedStrings;
	
	public LanguageUtils() {
		mappedStrings = new HashMap<>();
	}
	
	public void registerLanguageResource(String id, String language) {
		File file = Paths.get(Falcun.ASSETS_DIR.getAbsolutePath(), id, "lang", language + ".lang").toFile();
		
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
			String line;
			
			while((line = reader.readLine()) != null) {
				line = line.trim();
				
				if(line.startsWith("#")) {
					continue;
				}
				
				String[] parts = line.split("=", 2);
				
				if(parts.length != 2) {
					continue;
				}
				
				mappedStrings.put(parts[0], parts[1]);
			}
		} catch (IOException e) {
			Falcun.getInstance().log.error("Failed to load input stream for language", e);
		}
	}
	
	public String get(String key) {
		return mappedStrings.getOrDefault(key, key);
	}
}
