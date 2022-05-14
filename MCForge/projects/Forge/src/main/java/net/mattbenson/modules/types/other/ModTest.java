package net.mattbenson.modules.types.other;

import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.mattbenson.modules.NonViewableModule;

public class ModTest extends Module implements NonViewableModule {
	
	public ModTest() {
		super("Mod testing!", ModuleCategory.OTHER);
	}
	
	
}