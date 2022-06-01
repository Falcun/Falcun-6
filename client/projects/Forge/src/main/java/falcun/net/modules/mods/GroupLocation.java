package falcun.net.modules.mods;

import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.modules.ModuleCategory;

@FalcunModuleInfo(fileName = "GroupLocation", name = "GroupLocation", description = "GroupLocation", version = "1.0.0", category = ModuleCategory.MISC)
public class GroupLocation extends FalcunModule {
    @FalcunSetting("Display Nametag")
    FalcunValue<Boolean> isNametag = new FalcunValue<>(true);

    @FalcunSetting("Display Health")
    FalcunValue<Boolean> isHealth = new FalcunValue<>(true);

    @FalcunSetting("Display Skin")
    FalcunValue<Boolean> isSkin = new FalcunValue<>(true);

    @FalcunSetting("Display Potions")
    FalcunValue<Boolean> isPotions = new FalcunValue<>(true);

    @FalcunSetting("Display Distance")
    FalcunValue<Boolean> iSDistance = new FalcunValue<>(true);
}
