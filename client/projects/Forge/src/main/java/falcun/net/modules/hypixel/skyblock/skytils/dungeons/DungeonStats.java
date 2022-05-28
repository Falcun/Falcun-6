package falcun.net.modules.hypixel.skyblock.skytils.dungeons;

import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.api.modules.inheritance.FalcunEventBusModule;
import falcun.net.modules.ModuleCategory;

@FalcunModuleInfo(fileName = "DungeonStats", name = "Dungeon Stats", description = "Displays stats about the dungeon run", version = "1.0.0", category = ModuleCategory.SKYBLOCK)
public class DungeonStats extends FalcunModule implements FalcunEventBusModule {
    @FalcunSetting("Dungeon Score Estimate")
    public static FalcunValue<Boolean> scoreEstimate = new FalcunValue<>(true);

    @FalcunSetting("Death Counter")
    public static FalcunValue<Boolean> deathCounter = new FalcunValue<>(true);

    @FalcunSetting("Dungeon Timer")
    public static FalcunValue<Boolean> dungeonTimer = new FalcunValue<>(true);

    @FalcunSetting("Boss Phase Timer") // spirit bear sadan necron
    public static FalcunValue<Boolean> bossPhaseTimer = new FalcunValue<>(true);

    @FalcunSetting("Milestone") // spirit bear sadan necron
    public static FalcunValue<Boolean> vMilestone = new FalcunValue<>(true);

    @FalcunSetting("Dungeon Timeline")
    public static FalcunValue<Boolean> dungeonTimeline = new FalcunValue<>(true);


}
