package net.mattbenson.modules.types.other;

import net.mattbenson.config.ConfigValue;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;

public class VoiceChat extends Module {
	@ConfigValue.Boolean(name = "Show Overlay")
	public boolean showOverlay = false;
	
	@ConfigValue.Boolean(name = "Push to Talk")
	public boolean push2Tal = true;
	
	@ConfigValue.Keybind(name = "Push to Talk Key")
	public int keyBind = 0;
	
	@ConfigValue.Keybind(name = "Mute Microphone Key")
	public int keyBind2 = 0;
	
	@ConfigValue.Keybind(name = "Vo1ce Chat Gui")
	public int keyBind3 = 0;
	
	@ConfigValue.Keybind(name = "Toggle Mute All Key")
	public int keyBind4 = 0;
	
	public VoiceChat() {
		super("Voice Chat", ModuleCategory.OTHER);
	}
}
