package net.mattbenson.modules.types.other;

import net.mattbenson.config.ConfigValue;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;

public class SwordFOV extends Module {
	@ConfigValue.Double(name = "FOV Scape", min = 60, max = 120)
	public double handFOVScale = 100;
	
	@ConfigValue.Boolean(name = "Sword Only")
	public boolean fovSwordOnly = false;
	
	public SwordFOV() {
		super("Hand FOV", ModuleCategory.OTHER);
	}
}
