package falcun.net.modules.mods;

import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.modules.ModuleCategory;

@FalcunModuleInfo(fileName = "PerspectiveMod", name = "Perspective Mod",  description = "Allows you to look around without moving your head", version = "1.0.0", category = ModuleCategory.MISC)
public class PerspectiveMod extends FalcunModule {
	public static PerspectiveMod instance = null;
	public PerspectiveMod(){
		instance  = this;
	}

	public static boolean isInPerspectiveMode(){
		return instance != null && instance.isEnabled();
	}
}
