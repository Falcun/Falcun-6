package net.mattbenson.macros;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.input.KeyDownEvent;
import net.mattbenson.events.types.input.MouseDownEvent;
import net.mattbenson.gui.menu.components.mods.MenuModKeybind;
import net.minecraft.client.Minecraft;

public class MacroManager {
	private List<Macro> macros;
	
	public MacroManager() {
		this.macros = new CopyOnWriteArrayList<>();
	}
	
	public List<Macro> getMacros() {
		return macros;
	}
	
	@SubscribeEvent
	public void onMouseDown(MouseDownEvent event) {
		if(Minecraft.getMinecraft().currentScreen != null) {
			return;
		}
		
		for(Macro macro : macros) {
			if(!macro.isEnabled()) {
				continue;
			}
			
			if(macro.getKey() == event.getButton() - MenuModKeybind.mouseOffset) {
				Minecraft.getMinecraft().thePlayer.sendChatMessage(macro.getCommand());
			}
		}
	}
	
	@SubscribeEvent
	public void onKeyDown(KeyDownEvent event) {
		if(Minecraft.getMinecraft().currentScreen != null) {
			return;
		}
		
		for(Macro macro : macros) {
			if(!macro.isEnabled()) {
				continue;
			}
			
			if(macro.getKey() == event.getKey() && event.getKey() >= 0) {
				Minecraft.getMinecraft().thePlayer.sendChatMessage(macro.getCommand());
			}
		}
	}
}
