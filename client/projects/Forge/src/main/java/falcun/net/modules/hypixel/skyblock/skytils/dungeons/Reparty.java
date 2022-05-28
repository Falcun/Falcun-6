package falcun.net.modules.hypixel.skyblock.skytils.dungeons;

import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.api.modules.inheritance.FalcunEventBusModule;
import falcun.net.modules.ModuleCategory;

@FalcunModuleInfo(fileName = "Reparty", name = "Reparty", description = "Reparties the players in your party", version = "1.0.0", category = ModuleCategory.SKYBLOCK)
public class Reparty extends FalcunModule implements FalcunEventBusModule {

    @FalcunSetting("Auto Reparty")
    FalcunValue<Boolean> autoReparty = new FalcunValue<Boolean>(true);
}
