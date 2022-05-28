package falcun.net.modules.hypixel.skyblock.skytils.dungeons;

import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.api.modules.inheritance.FalcunEventBusModule;
import falcun.net.modules.ModuleCategory;

@FalcunModuleInfo(fileName = "DungeonSettings", name = "Dungeon Settings", description = "Misc dungeon settings", version = "1.0.0", category = ModuleCategory.SKYBLOCK)
public class DungeonSettings extends FalcunModule implements FalcunEventBusModule {

    @FalcunSetting("Hide Damage Display In Boss")
    public static FalcunValue<Boolean> hideDamageInBoss = new FalcunValue<>(true);

    @FalcunSetting("Hide Floor 4 Crowd Messages")
    public static FalcunValue<Boolean> hideFloor4CrowdMessages = new FalcunValue<>(true);

    @FalcunSetting("Hide Mage Damage Display")
    public static FalcunValue<Boolean> hideMageDamage = new FalcunValue<>(true);

    @FalcunSetting("Hide Non-starred Mobs Nametags")
    public static FalcunValue<Boolean> hideNonstarredMobsNametags = new FalcunValue<>(true);
}
