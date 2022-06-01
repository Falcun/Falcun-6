package falcun.net.modules.other;

import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.modules.ModuleCategory;

@FalcunModuleInfo(fileName = "PortalFix", name = "Portal Fix", description = "Fix for the portal", version = "1.0.0", category = ModuleCategory.MISC)
public class PortalFix extends FalcunModule {
	public static PortalFix instance = null;
	public PortalFix(){
		instance = this;
	}
	public static boolean isPortalFix(){
		return instance != null && instance.isEnabled();
	}
}
