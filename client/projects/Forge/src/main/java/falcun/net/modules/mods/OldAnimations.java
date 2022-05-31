package falcun.net.modules.mods;

import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.modules.ModuleCategory;

@FalcunModuleInfo(fileName = "OldAnimations", name = "OldAnimations", description = "OldAnimations", version = "1.0.0", category = ModuleCategory.MISC)
public class OldAnimations extends FalcunModule {
    @FalcunSetting("Blocking")
    FalcunValue<Boolean> OLD_BLOCKING = new FalcunValue<>(true);

    @FalcunSetting("Blocking Hitting")
    FalcunValue<Boolean> OLD_BLOCKING_HITTING = new FalcunValue<>(true);

    @FalcunSetting("Item Held")
    FalcunValue<Boolean> OLD_ITEM_HELD = new FalcunValue<>(true);

    @FalcunSetting("Bow")
    FalcunValue<Boolean> OLD_BOW = new FalcunValue<>(true);

    @FalcunSetting("Eat/Break Animation")
    FalcunValue<Boolean> OLD_EAT_USE_ANIMATION = new FalcunValue<>(true);

    @FalcunSetting("Armor Damage Flash")
    FalcunValue<Boolean> ARMOR = new FalcunValue<>(true);

    @FalcunSetting("Disable Health Flash")
    FalcunValue<Boolean> DISABLE_HEALTH_FLASH = new FalcunValue<>(true);
}
