package falcun.net.modules.hypixel.skyblock.skytils.dungeons;

import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.api.modules.inheritance.FalcunEventBusModule;
import falcun.net.modules.ModuleCategory;

@FalcunModuleInfo(fileName = "DungeonNames", name = "Dungeon Names", description = "Shows names on heads in GUIs", version = "1.0.0", category = ModuleCategory.SKYBLOCK)
public class DungeonNames extends FalcunModule implements FalcunEventBusModule {

    @FalcunSetting("Ghost Leap Names")
    public static FalcunValue<Boolean> ghostLeapNames = new FalcunValue<>(true);

    @FalcunSetting("Sprit Leap Names")
    public static FalcunValue<Boolean> spiritLeapNames = new FalcunValue<>(true);

    @FalcunSetting("Revive Stone Names")
    public static FalcunValue<Boolean> reviveStoneNames = new FalcunValue<>(true);
}
