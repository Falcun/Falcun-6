package net.mattbenson.modules.types.general;

import java.awt.Color;

import net.mattbenson.config.ConfigValue;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;

public class Settings extends Module {
	@ConfigValue.Boolean(name = "F5 Nametags", description = "Shows your own nametags in f5 mode")
	public boolean F5Nametags = true;
	
	@ConfigValue.Boolean(name = "Sort Tab To Prioritize Falcun Users")
	public boolean sortTab = true;
	
	@ConfigValue.Boolean(name = "Show Falcun Logo On Tab")
	public boolean showFalcunLogoOnTab = true;
	
	@ConfigValue.Boolean(name = "Inventory Search")
	public boolean inventorySearch = true;
	
	@ConfigValue.Color(name = "Inventory Search Color")
	public Color inventorySearchColor = new Color(127,255,0, 150);
	
	public Settings() {
		super("General Settings", ModuleCategory.HIDDEN);
	}
	
}
