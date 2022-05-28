package falcun.net.modules.hypixel.skyblock.skytils.dungeons;

import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.api.modules.inheritance.FalcunEventBusModule;
import falcun.net.modules.ModuleCategory;

@FalcunModuleInfo(fileName = "DungeonPuzzleSolvers", name = "Puzzle Solvers", description = "Solvers for puzzle rooms", version = "1.0.0", category = ModuleCategory.SKYBLOCK)
public class DungeonPuzzleSolvers extends FalcunModule implements FalcunEventBusModule {

    @FalcunSetting("Blaze Solver")
    FalcunValue<Boolean> blazeSolver = new FalcunValue<Boolean>(true);

    @FalcunSetting("Boulder Solver")
    FalcunValue<Boolean> boulderSolver = new FalcunValue<Boolean>(true);

    @FalcunSetting("Creeper Beams Solver")
    FalcunValue<Boolean> creeperBeamsSolver = new FalcunValue<Boolean>(true);

    @FalcunSetting("Ice Fill Solver")
    FalcunValue<Boolean> iceFillSolver = new FalcunValue<Boolean>(true);

    @FalcunSetting("Ice Path Solver")
    FalcunValue<Boolean> icePathSolver = new FalcunValue<Boolean>(true);

    @FalcunSetting("Teleport Maze Solver")
    FalcunValue<Boolean> teleportMazeSolver = new FalcunValue<Boolean>(true);

    @FalcunSetting("Three Weirdos Solver")
    FalcunValue<Boolean> threeWeirdosSolver = new FalcunValue<Boolean>(true);

    @FalcunSetting("Tic Tac Toe Solver")
    FalcunValue<Boolean> ticTacToeSolver = new FalcunValue<Boolean>(true);

    @FalcunSetting("Trivia Solver")
    FalcunValue<Boolean> triviaSolver = new FalcunValue<Boolean>(true);

    @FalcunSetting("Water Board Solver")
    FalcunValue<Boolean> waterBoardSolver = new FalcunValue<Boolean>(true);

    @FalcunSetting("Find Correct Livid")
    FalcunValue<Boolean> findCorrectLivid = new FalcunValue<Boolean>(true);
}
