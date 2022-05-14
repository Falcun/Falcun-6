package net.mattbenson.modules.types.mods;

import net.mattbenson.config.ConfigValue;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;

public class GroupLocation extends Module {

	@ConfigValue.Boolean(name = "Display Nametag")
	public boolean isNametag = true;
	
	@ConfigValue.Boolean(name = "Display Health")
	public boolean isHealth = true;
	
	@ConfigValue.Boolean(name = "Display Skin")
	public boolean isSkin = true;
	
	@ConfigValue.Boolean(name = "Display Potions")
	public boolean isPotions = true;
	
	@ConfigValue.Boolean(name = "Display Distance")
	public boolean isDistance = true;
	
	public GroupLocation() {
		super("Group Location", ModuleCategory.GROUPS);
		setEnabled(true);
	}
}
