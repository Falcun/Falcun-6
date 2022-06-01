package net.mattbenson.input;

import java.util.stream.Collectors;

import net.mattbenson.Falcun;
import net.mattbenson.cosmetics.emotes.EmoteSettings;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.input.KeyDownEvent;
import net.mattbenson.events.types.input.KeyUpEvent;
import net.mattbenson.gui.GuiEmoteSelector;
import net.mattbenson.gui.framework.BindType;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.types.mods.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class KeybindManager {
	@SubscribeEvent
	public void onKeyUp(KeyUpEvent event) {
		if(isInvalidScreen(Minecraft.getMinecraft().currentScreen)) {
			return;
		}
		
		for(Module module : Falcun.getInstance().moduleManager.getModules().stream().filter(entry ->
				entry.isBound() &&
				entry.getBindType() == BindType.HOLD &&
				entry.getKeyBind() == event.getKey())
				.collect(Collectors.toList())) {
			
			module.setEnabled(false);
		}
	}
	
	@SubscribeEvent
	public void onKeyDown(KeyDownEvent event) {
		if(isInvalidScreen(Minecraft.getMinecraft().currentScreen)) {
			return;
		}
		
		
		for(Module module : Falcun.getInstance().moduleManager.getModules().stream().filter(entry -> 
				entry.isBound() &&
				entry.getKeyBind() == event.getKey())
				.collect(Collectors.toList())) {
			
			if(module instanceof Gui) {
				module.setEnabled(true);
				continue;
			}
	
			module.setEnabled(module.getBindType() == BindType.TOGGLE ? !module.isEnabled() : true);
		}
		
		EmoteSettings settings = Falcun.getInstance().emoteSettings;
		
		if(settings != null) {
			if(event.getKey() == settings.getKeyBind()) {
				Minecraft.getMinecraft().displayGuiScreen(new GuiEmoteSelector());
			}
		}
	}
	
	public static boolean isInvalidScreen(GuiScreen screen) {
		if(screen != null) {
			return true;
		}
		
		return false;
	}
}
