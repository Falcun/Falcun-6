package falcun.net.modules.hypixel.skyblock.skytils.dungeons;

import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.api.modules.inheritance.FalcunEventBusModule;
import falcun.net.modules.ModuleCategory;

@FalcunModuleInfo(fileName = "DungeonTerminalSolvers", name = "Terminal Solvers", description = "Solvers for terminals", version = "1.0.0", category = ModuleCategory.SKYBLOCK)
public class DungeonTerminalSolvers extends FalcunModule implements FalcunEventBusModule {

    @FalcunSetting("Middle Click Terminals")
    FalcunValue<Boolean> middleClickTerminals = new FalcunValue<Boolean>(true);

    @FalcunSetting("Click In Order Solver")
    FalcunValue<Boolean> clickInOrderSolver = new FalcunValue<Boolean>(true);

    @FalcunSetting("Select All Color Solver")
    FalcunValue<Boolean> selectAllColorSolver = new FalcunValue<Boolean>(true);

    @FalcunSetting("Start With Solver")
    FalcunValue<Boolean> startWithSolver = new FalcunValue<Boolean>(true);

    @FalcunSetting("Item Frame Alignment Solver")
    FalcunValue<Boolean> itemFrameAlignmentSolver = new FalcunValue<Boolean>(true);

    @FalcunSetting("Shoot The Target Solver")
    FalcunValue<Boolean> shootTheTargetSolver = new FalcunValue<Boolean>(true);
}
