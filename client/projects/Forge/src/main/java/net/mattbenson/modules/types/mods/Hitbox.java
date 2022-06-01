package net.mattbenson.modules.types.mods;

import java.awt.Color;

import net.mattbenson.config.ConfigValue;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;

public class Hitbox extends Module {

	@ConfigValue.Integer(name = "Line Width", min = 1, max = 20)
	public int lineWidth = 2;
    
	@ConfigValue.Color(name = "Line Color")
	public Color lineColor = Color.RED;
    
	@ConfigValue.List(name = "Hitbox Modes", values = {"All","Enderpearls Only", "Items Only", "Enderpearls & Items Only"})
	public String hitboxMode = "All";
    
	public Hitbox() {
		super("Hitbox", ModuleCategory.MODS);
	}
}
