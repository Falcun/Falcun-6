package net.mattbenson.modules;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.mattbenson.Falcun;
import net.mattbenson.config.ConfigEntry;
import net.mattbenson.config.ConfigValue;
import net.mattbenson.config.types.BooleanEntry;
import net.mattbenson.config.types.ColorEntry;
import net.mattbenson.config.types.DoubleEntry;
import net.mattbenson.config.types.FloatEntry;
import net.mattbenson.config.types.IntEntry;
import net.mattbenson.config.types.ListEntry;
import net.mattbenson.config.types.StringEntry;
import net.mattbenson.gui.framework.BindType;
import net.mattbenson.gui.hud.HUDElement;
import net.minecraft.client.Minecraft;

public class Module implements IModule {
	protected String name;
	
	private int keyBind = Keyboard.CHAR_NONE;
	private BindType bindType = BindType.TOGGLE;
	
	public boolean enabled;
	
	private ModuleCategory category;
	
	private List<ConfigEntry> values;
	private List<HUDElement> hudElements;
	
	protected Minecraft mc;
	
	public Module(String name, ModuleCategory category) {
		this.name = name;
		this.category = category;
		
		values = new ArrayList<>();
		hudElements = new ArrayList<>();
		
		this.mc = Minecraft.getMinecraft();
		
		for(Field field : getClass().getDeclaredFields()) {
			Type type = field.getType();
			
			if(!field.isAccessible()) {
				field.setAccessible(true);
			}
			
			Object annot = null;
			
			if((annot = getVal(field, ConfigValue.Boolean.class)) != null) {
				ConfigValue.Boolean val = (ConfigValue.Boolean) annot;
				
				if(!val.enabled()) {
					continue;
				}
				
				values.add(new BooleanEntry(field, val.name(), val.description(), val.visible()));
			}else if((annot = getVal(field, ConfigValue.Integer.class)) != null) {
				ConfigValue.Integer val = (ConfigValue.Integer) annot;
				
				if(!val.enabled()) {
					continue;
				}
				
				values.add(new IntEntry(field, val.min(), val.max(), val.name(), val.description(), val.visible()));
			} else if((annot = getVal(field, ConfigValue.Float.class)) != null) {
				ConfigValue.Float val = (ConfigValue.Float) annot;
				
				if(!val.enabled()) {
					continue;
				}
				
				values.add(new FloatEntry(field, val.min(), val.max(), val.name(), val.description(), val.visible()));
			} else if((annot = getVal(field, ConfigValue.Double.class)) != null) {
				ConfigValue.Double val = (ConfigValue.Double) annot;
				
				if(!val.enabled()) {
					continue;
				}
				
				values.add(new DoubleEntry(field, val.min(), val.max(), val.name(), val.description(), val.visible()));
			} else if((annot = getVal(field, ConfigValue.Color.class)) != null) {
				ConfigValue.Color val = (ConfigValue.Color) annot;
				
				if(!val.enabled()) {
					continue;
				}
				
				values.add(new ColorEntry(field, val.name(), val.description(), val.visible()));
			} else if((annot = getVal(field, ConfigValue.List.class)) != null) {
				ConfigValue.List val = (ConfigValue.List) annot;
				
				if(!val.enabled()) {
					continue;
				}
				
				values.add(new ListEntry(this, field, val.values(), val.name(), val.description(), val.visible()));
			} else if((annot = getVal(field, ConfigValue.Keybind.class)) != null) {
				ConfigValue.Keybind val = (ConfigValue.Keybind) annot;
				
				if(!val.enabled()) {
					continue;
				}
				
				values.add(new IntEntry(field, val.name(), val.description(), val.visible()));
			} else if((annot = getVal(field, ConfigValue.Text.class)) != null) {
				ConfigValue.Text val = (ConfigValue.Text) annot;
				
				if(!val.enabled()) {
					continue;
				}
				
				values.add(new StringEntry(field, val.name(), val.description(), val.visible()));
			}
		}
	}
	
	private <T extends Annotation> T getVal(Field field, Class<T> annot) {
		if(field.isAnnotationPresent(annot)) {
			return field.getAnnotation(annot);
		}
		
		return null;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public boolean isBound() {
		return keyBind != Keyboard.CHAR_NONE;
	}
	
	public int getKeyBind() {
		return keyBind;
	}
	
	public ModuleCategory getCategory() {
		return category;
	}
	
	public List<ConfigEntry> getEntries() {
		return values;
	}
	
	public List<HUDElement> getHUDElements() {
		return hudElements;
	}
	
	public void setKeyBind(int keyBind) {
		this.keyBind = keyBind;
	}
	
	public void resetKeyBind() {
		this.keyBind = Keyboard.CHAR_NONE;
	}
	
	public BindType getBindType() {
		return bindType;
	}
	
	public void setBindType(BindType bindType) {
		this.bindType = bindType;
	}
	
	public void addHUD(HUDElement element) {
		element.setParent(this);
		getHUDElements().add(element);
	}
	
	public void addHUDDirectly(HUDElement element) {
		element.setParent(this);
		Falcun.getInstance().hudManager.getElements().add(element);
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		
		if(enabled) {
			onEnable();
			
			if(!Falcun.EVENT_BUS.isRegistered(this)) {
				Falcun.EVENT_BUS.register(this);
			}
		} else {
			if(Falcun.EVENT_BUS.isRegistered(this)) {
				Falcun.EVENT_BUS.unregister(this);
			}
			
			onDisable();
		}
	}
	
	public void toggle() {
		setEnabled(!enabled);
	}
}
