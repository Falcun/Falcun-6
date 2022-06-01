package falcun.net.modules.render;

import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.modules.ModuleCategory;

@FalcunModuleInfo(fileName = "Cooldowns", name = "Cooldowns", description = "Cooldowns", version = "1.0.0", category = ModuleCategory.MISC)
public class Cooldowns extends FalcunModule {
    @FalcunSetting("Enderpearl Timer")
    FalcunValue<Boolean> ep = new FalcunValue<>(true);

    @FalcunSetting("Crapple Timer")
    FalcunValue<Boolean> cp = new FalcunValue<>(true);

    @FalcunSetting("Gapple Timer")
    FalcunValue<Boolean> gp = new FalcunValue<>(true);



}
