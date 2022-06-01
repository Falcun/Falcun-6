package net.mattbenson.config;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.mattbenson.Falcun;
import net.mattbenson.Wrapper;
import net.mattbenson.console.Console;
import net.mattbenson.events.types.falcun.ConfigChangeEvent;
import net.mattbenson.file.FileHandler;
import net.mattbenson.gui.framework.BindType;
import net.mattbenson.gui.hud.HUDElement;
import net.mattbenson.gui.menu.pages.FPSPage;
import net.mattbenson.hud.HUD;
import net.mattbenson.macros.Macro;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.types.hud.Image1;
import net.mattbenson.modules.types.hud.Image2;
import net.mattbenson.modules.types.hud.Image3;
import net.mattbenson.modules.types.hud.Image4;
import net.mattbenson.modules.types.hud.Image5;
import net.mattbenson.modules.types.hud.Text1;
import net.mattbenson.modules.types.hud.Text2;
import net.mattbenson.modules.types.hud.Text3;
import net.mattbenson.modules.types.hud.Text4;
import net.mattbenson.modules.types.hud.Text5;
import net.mattbenson.modules.types.render.Crosshair;
import net.mattbenson.network.utils.StringUtils;
import net.mattbenson.utils.HUDAssetUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;

public class Config {
	private ConfigManager parent;
	private String name;
	private FileHandler file;
	
	public Config(ConfigManager parent, String name, File file) {
		this.parent = parent;
		this.name = name;
		this.file = new FileHandler(file);
		
		try {
			this.file.init();
			
			if(this.file.isFresh()) {
				save();
			}
		} catch (IOException e) {
			Falcun.getInstance().log.error("Failed to initiate config " + name + ".", e);
		}
	}
	
	public void load() {
		String content = "";
		
		try {
			content = file.getContent(false);
		} catch (IOException e) {
			Falcun.getInstance().log.error("Failed to load config " + name + " from file.", e);
		}
		
		load(content);
	}
	
	public void load(String content) {
		parent.updateLast(this);
		
		try {
			JSONObject obj = new JSONObject(content);
			
			for(Module mod : Falcun.getInstance().moduleManager.getModules()) {
				mod.setEnabled(false);
			}
			
			Falcun.getInstance().macroManager.getMacros().clear();
			Falcun.getInstance().consoleManager.getConsoles().clear();
			Falcun.getInstance().newHudManager.getComponents().clear();
			
			FPSPage.TILE_ENTITIES.clear();
			FPSPage.ENTITIES.clear();
			FPSPage.BLOCKS.clear();
			FPSPage.PARTICLES.clear();
			
			for(String key : obj.keySet()) {
				if(key.equalsIgnoreCase("macros") || key.equalsIgnoreCase("crosshair-data") || key.equalsIgnoreCase("console") || key.equalsIgnoreCase("newhud")) {
					continue;
				}
				
				Module module = Falcun.getInstance().moduleManager.getModule(key);
				
				if(module == null) {
					Falcun.getInstance().log.debug("Loaded config " + name + " with left over setting " + key + " which is no longer used.");
					continue;
				}
				
				JSONObject json = obj.getJSONObject(key);
				
				if(!json.has("enabled") || !json.has("bind")) {
					continue;
				}
				
				boolean enabled = json.getBoolean("enabled");
				int bind = json.getInt("bind");
				BindType bindType = BindType.getBind(json.getString("bindtype"));
				
				module.setEnabled(enabled);
				module.setKeyBind(bind);
				module.setBindType(bindType);
				
				JSONObject settings = json.getJSONObject("settings");
				
				for(String setting : settings.keySet()) {
					for(ConfigEntry entry : module.getEntries()) {
						if(entry.getKey().equalsIgnoreCase(setting)) {
							Object value = settings.get(setting);
							
							if(value instanceof JSONObject) {
								JSONObject color = (JSONObject) value;
								value = new Color(color.getInt("red"), color.getInt("green"), color.getInt("blue"), color.getInt("alpha"));
							} else if(entry.getType() == float.class) {
								value = (float)settings.getDouble(setting);
							}
							
							entry.setValue(module, value);
							break;
						}
					}
				}
				
				JSONObject hud = json.getJSONObject("hud");
				
				for(String element : hud.keySet()) {
					String identifier = element;
					JSONObject elementObj = hud.getJSONObject(identifier);
					
					for(HUDElement modElement : module.getHUDElements()) {
						if(modElement.getIdentifier().equalsIgnoreCase(identifier)) {
							modElement.setX(elementObj.getInt("x"));
							modElement.setY(elementObj.getInt("y"));
							modElement.setScale(elementObj.getDouble("scale"));
							modElement.setVisible(elementObj.getBoolean("visible"));
							break;
						}
					}
				}
			}
			
			JSONArray macroList = obj.getJSONArray("macros");
			
			for(int i = 0; i < macroList.length(); i++) {
				JSONObject macro = macroList.getJSONObject(i);
				
				Falcun.getInstance().macroManager.getMacros().add(new Macro(macro.getString("name"), macro.getString("command"), macro.getInt("key"), macro.getBoolean("enabled")));
			}
			
			
		JSONArray consoleList = obj.getJSONArray("console");
			
			for(int i = 0; i < consoleList.length(); i++) {
				JSONObject console = consoleList.getJSONObject(i);
				Falcun.getInstance().consoleManager.getConsoles().add(new Console(console.getString("username"), console.getString("password")));
			}
			
		JSONArray newHUDList = obj.getJSONArray("newhud");
			
			for(int i = 0; i < newHUDList.length(); i++) {
				JSONObject newHUDs = newHUDList.getJSONObject(i);
				Falcun.getInstance().newHudManager.getComponents().add(new HUD(newHUDs.getString("username"), newHUDs.getString("file"), newHUDs.getInt("width"), newHUDs.getInt("height"), newHUDs.getInt("red"), newHUDs.getInt("green"), newHUDs.getInt("blue"), newHUDs.getInt("opacity"), newHUDs.getString("image"), newHUDs.getInt("posX"), newHUDs.getInt("posY")));
			
			if (newHUDs.getString("image").equals("image1")) {
				Image1.location = HUDAssetUtils.getResource("/" + newHUDs.getString("file"));
				Image1.hud.setX(newHUDs.getInt("posX"));
				Image1.hud.setY(newHUDs.getInt("posY"));
				Image1.hud.setWidth(newHUDs.getInt("width"));
				Image1.hud.setHeight(newHUDs.getInt("height"));
				Falcun.getInstance().moduleManager.getModule(Image1.class).setEnabled(true);
			} 
			
			if (newHUDs.getString("image").equals("image2")) {
				Image2.location = HUDAssetUtils.getResource("/" + newHUDs.getString("file"));
				Image2.hud.setX(newHUDs.getInt("posX"));
				Image2.hud.setY(newHUDs.getInt("posY"));
				Image2.hud.setWidth(newHUDs.getInt("width"));
				Image2.hud.setHeight(newHUDs.getInt("height"));
				Falcun.getInstance().moduleManager.getModule(Image2.class).setEnabled(true);
			} 
			if (newHUDs.getString("image").equals("image3")) {
				Image3.location = HUDAssetUtils.getResource("/" + newHUDs.getString("file"));
				Image3.hud.setX(newHUDs.getInt("posX"));
				Image3.hud.setY(newHUDs.getInt("posY"));
				Image3.hud.setWidth(newHUDs.getInt("width"));
				Image3.hud.setHeight(newHUDs.getInt("height"));
				Falcun.getInstance().moduleManager.getModule(Image3.class).setEnabled(true);
			} 
			
			if (newHUDs.getString("image").equals("image4")) {
				Image4.location = HUDAssetUtils.getResource("/" + newHUDs.getString("file"));
				Image4.hud.setX(newHUDs.getInt("posX"));
				Image4.hud.setY(newHUDs.getInt("posY"));
				Image4.hud.setWidth(newHUDs.getInt("width"));
				Image4.hud.setHeight(newHUDs.getInt("height"));
				Falcun.getInstance().moduleManager.getModule(Image4.class).setEnabled(true);
			} 
			
			if (newHUDs.getString("image").equals("image5")) {
				Image5.location = HUDAssetUtils.getResource("/" + newHUDs.getString("file"));
				Image5.hud.setX(newHUDs.getInt("posX"));
				Image5.hud.setY(newHUDs.getInt("posY"));
				Image5.hud.setWidth(newHUDs.getInt("width"));
				Image5.hud.setHeight(newHUDs.getInt("height"));
				Falcun.getInstance().moduleManager.getModule(Image5.class).setEnabled(true);
			} 
			
			if (newHUDs.getString("image").equals("text1")) {
				Text1.hud.setX(newHUDs.getInt("posX"));
				Text1.hud.setY(newHUDs.getInt("posY"));
				Text1.hud.setWidth(newHUDs.getInt("width"));
				Text1.hud.setHeight(newHUDs.getInt("height"));
				Falcun.getInstance().moduleManager.getModule(Text1.class).setEnabled(true);
			} 
			if (newHUDs.getString("image").equals("text2")) {
				Text2.hud.setX(newHUDs.getInt("posX"));
				Text2.hud.setY(newHUDs.getInt("posY"));
				Text2.hud.setWidth(newHUDs.getInt("width"));
				Text2.hud.setHeight(newHUDs.getInt("height"));
				Falcun.getInstance().moduleManager.getModule(Text2.class).setEnabled(true);
			} 
			if (newHUDs.getString("image").equals("text3")) {
				Text3.hud.setX(newHUDs.getInt("posX"));
				Text3.hud.setY(newHUDs.getInt("posY"));
				Text3.hud.setWidth(newHUDs.getInt("width"));
				Text3.hud.setHeight(newHUDs.getInt("height"));
				Falcun.getInstance().moduleManager.getModule(Text3.class).setEnabled(true);
			} 
			if (newHUDs.getString("image").equals("text4")) {
				Text4.hud.setX(newHUDs.getInt("posX"));
				Text4.hud.setY(newHUDs.getInt("posY"));
				Text4.hud.setWidth(newHUDs.getInt("width"));
				Text4.hud.setHeight(newHUDs.getInt("height"));
				Falcun.getInstance().moduleManager.getModule(Text4.class).setEnabled(true);
			} 
			if (newHUDs.getString("image").equals("text5")) {
				Text5.hud.setX(newHUDs.getInt("posX"));
				Text5.hud.setY(newHUDs.getInt("posY"));
				Text5.hud.setWidth(newHUDs.getInt("width"));
				Text5.hud.setHeight(newHUDs.getInt("height"));
				Falcun.getInstance().moduleManager.getModule(Text5.class).setEnabled(true);
			} 
			}

			
			JSONObject fps = obj.getJSONObject("fps");
			
			JSONArray blocks = fps.getJSONArray("blocks");
			JSONArray entities = fps.getJSONArray("entities");
			JSONArray tileentities = fps.getJSONArray("tile-entities");
			JSONArray particles = fps.getJSONArray("particles");
			
			List<String> list = new ArrayList<>();
			
			for(int i = 0; i < blocks.length(); i++) {
				list.add(blocks.getString(i));
			}
			
			for(String item : list) {
				try {
					String string = new String(item);
				
					Class<?> clazz = Class.forName(string);
					
					if(Block.class.isAssignableFrom(clazz)) {
						Class<? extends Block> block = (Class<? extends Block>)clazz;
						
						if(!FPSPage.BLOCKS.contains(block)) {
							FPSPage.BLOCKS.add(block);
						}
					}
				} catch(IllegalArgumentException | LinkageError | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			
			list.clear();
			for(int i = 0; i < entities.length(); i++) {
				list.add(entities.getString(i));
			}
			
			for(String item : list) {
				try {
					String string = new String(item);
					
					if(string.contains(" ")) {
						string = string.split(" ", 2)[1];
					}
					
					Class<?> clazz = Class.forName(string);
					
					if(Entity.class.isAssignableFrom(clazz)) {
						Class<? extends Entity> entity = (Class<? extends Entity>)clazz;
						
						if(!FPSPage.ENTITIES.contains(entity)) {
							FPSPage.ENTITIES.add(entity);
						}
					}
				} catch(IllegalArgumentException | LinkageError | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}	
			
			list.clear();
			for(int i = 0; i < tileentities.length(); i++) {
				list.add(tileentities.getString(i));
			}
			
			for(String item : list) {
				try {
					String string = new String(item);
				
					Class<?> clazz = Class.forName(string);
					
					if(TileEntity.class.isAssignableFrom(clazz)) {
						Class<? extends TileEntity> tileEntity = (Class<? extends TileEntity>) clazz;
						
						if(!FPSPage.TILE_ENTITIES.contains(tileEntity)) {
							FPSPage.TILE_ENTITIES.add(tileEntity);
						}
					}
				} catch(IllegalArgumentException | LinkageError | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			
			list.clear();
			for(int i = 0; i < particles.length(); i++) {
				list.add(particles.getString(i));
			}
			
			for(String item : list) {
				try {
					String string = new String(item);
					EnumParticleTypes particle = EnumParticleTypes.valueOf(string);
					
					if(particle == null) {
						continue;
					}
					
					if(!FPSPage.PARTICLES.contains(particle)) {
						FPSPage.PARTICLES.add(particle);
					}
				} catch(IllegalArgumentException e) {
					e.printStackTrace();
				}
			}
			
			list.clear();
			
			int[][] data = Crosshair.crosshair;
			
			for(int i = 0; i < data.length; i++) {
				for(int ii = 0; ii < data[i].length; ii++) {
					data[i][ii] = 0;
				}
			}
			
			String[] parts = obj.getString("crosshair-data").split("]");
			
			List<List<Integer>> crosshairData = new ArrayList<>();
			
			for(String part : parts) {
				String formatted = part;
				
				if(formatted.startsWith("[")) {
					formatted = formatted.substring(1);
				}
				
				formatted = formatted.trim();
				
				List<Integer> row = new ArrayList<>();
				
				for(String rowEntry : formatted.split(",")) {
					String entry = rowEntry.trim();
					
					if(!StringUtils.isInteger(entry)) {
						continue;
					}
					
					row.add(Integer.parseInt(entry));
				}
				
				if(row.size() > 0) {
					crosshairData.add(row);
				}
			}
			
			for(int i = 0; i < crosshairData.size(); i++) {
				List<Integer> crosshairList = crosshairData.get(i);
				
				if(i >= data.length) {
					continue;
				}
				for(int ii = 0; ii < crosshairList.size(); ii++) {
					int row = crosshairList.get(ii);
					
					if(ii >= data[i].length) {
						continue;
					}
					
					data[i][ii] = row;
				}
			}
			
		} catch(JSONException e) {
			Falcun.getInstance().log.error("Failed to load config " + name + ", inproper json.", e);
		}
		
		Wrapper.getInstance().post(new ConfigChangeEvent());
	}
	
	public void save(String content) {
		try {
			file.writeToFile(content, false);
		} catch (IOException e) {
			Falcun.getInstance().log.error("Failed to save config " + name + ".", e);
		}
		Wrapper.getInstance().post(new ConfigChangeEvent());
	}
	
	public void save() {
		save(toString());
	}
	
	@Override
	public String toString() {
		JSONObject config = new JSONObject();
		
		for(Module mod : Falcun.getInstance().moduleManager.getModules()) {
			JSONObject obj = new JSONObject();
			JSONObject settingsObj = new JSONObject();
			
			JSONObject hudObj = new JSONObject();
			
			for(HUDElement element : mod.getHUDElements()) {
				JSONObject elementObj = new JSONObject();
				
				elementObj.put("x", element.getX());
				elementObj.put("y", element.getY());
				elementObj.put("scale", element.getScale());
				elementObj.put("visible", element.isVisible());
				
				hudObj.put(element.getIdentifier(), elementObj);
			}
			
			obj.put("enabled", mod.isEnabled());
			obj.put("bind", mod.getKeyBind());
			obj.put("bindtype", mod.getBindType().toString());
			
			for(ConfigEntry entry : mod.getEntries()) {
				entry.appendToConfig(entry.getKey(), entry.getValue(mod), settingsObj);
			}
			
			obj.put("settings", settingsObj);
			obj.put("hud", hudObj);
			config.put(mod.getName(), obj);
		}
		
		JSONArray macros = new JSONArray();
		
		for(Macro macro : Falcun.getInstance().macroManager.getMacros()) {
			JSONObject obj = new JSONObject();
			
			obj.put("name", macro.getName());
			obj.put("command", macro.getCommand());
			obj.put("key", macro.getKey());
			obj.put("enabled", macro.isEnabled());
			
			macros.put(obj);
		}
		
		config.put("macros", macros);
		
		
		
		JSONArray console = new JSONArray();
		
		for(Console consoles : Falcun.getInstance().consoleManager.getConsoles()) {
			JSONObject obj = new JSONObject();
			obj.put("username", consoles.getName());
			obj.put("password", consoles.getCommand());
			console.put(obj);
		}
		
		config.put("console", console);
		
		
		
		JSONArray newhud = new JSONArray();
		
		for(HUD newhuds : Falcun.getInstance().newHudManager.getComponents()) {
			JSONObject obj = new JSONObject();
			obj.put("username", newhuds.getName());
			obj.put("file", newhuds.getFile());
			obj.put("width", newhuds.getWidth());
			obj.put("height", newhuds.getHeight());
			obj.put("red", newhuds.getr());
			obj.put("green", newhuds.getg());
			obj.put("blue", newhuds.getb());
			obj.put("opacity", newhuds.getOpacity());
			obj.put("image", newhuds.getImage());
			obj.put("posX", newhuds.getPosX());
			obj.put("posY", newhuds.getPosY());
			
			newhud.put(obj);
		}
		
		config.put("newhud", newhud);

		
		JSONObject fps = new JSONObject();
		
		List<String> buffer = new ArrayList<>();
		
		for(Class<?> clazz : FPSPage.BLOCKS) {
			buffer.add(clazz.getCanonicalName());
		}
		
		fps.put("blocks", new JSONArray(buffer.toArray(new String[buffer.size()])));
		buffer.clear();
		
		for(Class<?> clazz : FPSPage.ENTITIES) {
			buffer.add(clazz.getCanonicalName());
		}
		
		fps.put("entities", new JSONArray(buffer.toArray(new String[buffer.size()])));
		buffer.clear();
		
		for(Class<?> clazz : FPSPage.TILE_ENTITIES) {
			buffer.add(clazz.getCanonicalName());
		}
		
		fps.put("tile-entities", new JSONArray(buffer.toArray(new String[buffer.size()])));
		buffer.clear();
		
		for(EnumParticleTypes particle : FPSPage.PARTICLES) {
			buffer.add(particle.toString());
		}
		
		fps.put("particles", new JSONArray(buffer.toArray(new String[buffer.size()])));
		buffer.clear();
		
		config.put("fps", fps);
		
		StringBuilder crosshairData = new StringBuilder();
		int[][] data = Crosshair.crosshair;
		
		for(int i = 0; i < data.length; i++) {
			StringBuilder inner = new StringBuilder();
			
			inner.append("[");
			for(int ii = 0; ii < data[i].length; ii++) {
				if(inner.length() > 1) {
					inner.append(",");
				}
				
				inner.append(data[i][ii]);
			}
			
			inner.append("]");
			
			if(!crosshairData.toString().isEmpty()) {
				crosshairData.append(", ");
			}
			
			crosshairData.append(inner.toString());
		}
		
		config.put("crosshair-data", crosshairData);
		
		return config.toString(4);
	}

	public String getName() {
		return name;
	}

	public void delete() {
		if(parent.lastLoadedConfig == this) {
			parent.lastLoadedConfig = null;
		}
		
		file.getFile().delete();
	}
	
	public FileHandler getFileHandler() {
		return file;
	}

	public boolean isEnabled() {
		if(parent.getLoadedConfig() == null) {
			return false;
		}
		
		return parent.getLoadedConfig().getName().equalsIgnoreCase(getName());
	}
}
