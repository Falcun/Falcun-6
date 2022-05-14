package net.mattbenson.modules.types.render;

import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;

public class Gamma extends Module {
	private float savedBrightness = 0F;
	
	public Gamma() {
		super("Gamma", ModuleCategory.RENDER);
	}
	
	@Override
	public void onEnable() {
		savedBrightness = mc.gameSettings.gammaSetting;
		mc.gameSettings.gammaSetting = 1000;
		mc.gameSettings.saveOptions();
		mc.gameSettings.saveOfOptions();
	}

	@Override
	public void onDisable() {
		mc.gameSettings.gammaSetting = savedBrightness;
		mc.gameSettings.saveOptions();
		mc.gameSettings.saveOfOptions();
	}
}
