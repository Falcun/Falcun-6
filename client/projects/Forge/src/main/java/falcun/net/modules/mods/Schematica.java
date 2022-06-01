package falcun.net.modules.mods;

import falcun.net.api.colors.FalcunColor;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.*;
import falcun.net.modules.ModuleCategory;

import java.awt.*;

@FalcunModuleInfo(fileName = "Schemetica", name = "Schemetica", description = "Schemetica to u know do the schematic", version = "6.0.0", category = ModuleCategory.MISC)
public class Schematica extends FalcunModule {
    @FalcunSetting("[Beta] Mode")
    FalcunValue<Boolean> betaMode = new FalcunValue<>(false);

    @FalcunSetting("[Beta] Use smart string placing")
    FalcunValue<Boolean> betaString = new FalcunValue<>(false);

    @FalcunSetting("[Beta] Only Destory Placed Blocks")
    FalcunValue<Boolean> destoryPlaced = new FalcunValue<>(true);

    @FalcunSetting("[Beta] Auto Tick Delay milliseconds")
    @FalcunBounds(min = 25, max = 1000)
    FalcunValue<Integer> autotickDelay = new FalcunValue<>(500);

    @FalcunSetting("[Beta] Only print in Creative")
    FalcunValue<Boolean> BetaOnlyCreative = new FalcunValue<>(true);

    @FalcunSetting("[Beta] Block Break Delay milliseconds")
    @FalcunBounds(min = 50, max = 1000)
    FalcunValue<Integer> betaBreakDelay = new FalcunValue<>(200);

    @FalcunSetting("Timeout")
    @FalcunBounds(min = 0, max = 100)
    FalcunValue<Integer> timeout = new FalcunValue<>(0);

    @FalcunSetting("Place Delay")
    @FalcunBounds(min = 0, max = 10)
    FalcunValue<Integer> placeDelay = new FalcunValue<>(0);

    @FalcunSetting("Tick Delay")
    @FalcunBounds(min = 0, max = 20)
    FalcunValue<Integer> tickDelay = new FalcunValue<>(0);

    @FalcunSetting("Place Distance")
    @FalcunBounds(min = 0, max = 8)
    FalcunValue<Integer> placeDistance = new FalcunValue<>(5);

    @FalcunSetting("Render Distance")
    @FalcunBounds(min = 2, max = 16)
    FalcunValue<Integer> renderDistance = new FalcunValue<>(8);

    @FalcunSetting("Max Traces Y")
    @FalcunBounds(min = 1, max = 256)
    FalcunValue<Integer> maxTracersY = new FalcunValue<>(256);

    @FalcunSetting("Place Instantly")
    FalcunValue<Boolean> placeInstantly = new FalcunValue<>(true);

    @FalcunSetting("Destroy Blocks")
    FalcunValue<Boolean> destryBlocks = new FalcunValue<>(false);

    @FalcunSetting("Destroy Instantly")
    FalcunValue<Boolean> destryBlocksInstantly = new FalcunValue<>(false);

    @FalcunSetting("Place Adjacent")
    FalcunValue<Boolean> placeAdjacent = new FalcunValue<>(true);

    @FalcunSetting("Highlight")
    FalcunValue<Boolean> highlight = new FalcunValue<>(true);

    @FalcunSetting("Highlight Air")
    FalcunValue<Boolean> highlightAir = new FalcunValue<>(false);

    @FalcunSetting("Trace Repeaters")
    FalcunValue<Boolean> traceRepeaters = new FalcunValue<>(true);

    @FalcunSetting("Missing ESP")
    FalcunValue<Boolean> missingESP = new FalcunValue<>(false);

    @FalcunSetting("Missing Tracers")
    FalcunValue<Boolean> missingTracers = new FalcunValue<>(true);

    @FalcunSetting("Line Width")
    @FalcunBounds(min = 0.1F, max = 5F)
    FalcunValue<Float> lineWidth = new FalcunValue<>(1F);

    @FalcunSetting("ESP/Tracer Color")
    FalcunValue<FalcunColor> espColor = new FalcunValue<>(new FalcunColor(Color.RED.getRGB()));

    @FalcunSetting("Fix Dispensers")
    FalcunValue<Boolean> fixDispensers = new FalcunValue<>(true);

    @FalcunSetting("Auto tick repeaters")
    FalcunValue<Boolean> autoTickRepeaters = new FalcunValue<>(false);

    @FalcunSetting("Move with arrow keys")
    FalcunValue<Boolean> moveKeys = new FalcunValue<>(false);

    @FalcunSetting("Auto Save/Load Schematics")
    FalcunValue<Boolean> autoSaveAndLoad = new FalcunValue<>(false);

    @FalcunSetting("Save Recent Schematics Data")
    FalcunValue<Boolean> autoSaveRecent = new FalcunValue<>(true);

    @FalcunSetting("360 Mode")
    FalcunValue<Boolean> infintieMode = new FalcunValue<>(false);

    @FalcunSetting("Break Bad Dispensers")
    FalcunValue<Boolean> breakBadDispensers = new FalcunValue<>(true);

    @FalcunSetting("Break Bad Pistons")
    FalcunValue<Boolean> breakBadPiston = new FalcunValue<>(true);

    @FalcunSetting("Break Bad Slabs")
    FalcunValue<Boolean> breakBadSlabs = new FalcunValue<>(true);

    @FalcunSetting("Auto Y1")
    FalcunValue<Boolean> autoY1 = new FalcunValue<>(false);

    @FalcunSetting("Auto Y1 is height 255")
    FalcunValue<Boolean> autoY1auto = new FalcunValue<>(false);

    @FalcunSetting("Slot 1")
    FalcunValue<Boolean> slot1 = new FalcunValue<>(false);

    @FalcunSetting("Slot 2")
    FalcunValue<Boolean> slot2 = new FalcunValue<>(true);

    @FalcunSetting("Slot 3")
    FalcunValue<Boolean> slot3 = new FalcunValue<>(true);

    @FalcunSetting("Slot 4")
    FalcunValue<Boolean> slot4 = new FalcunValue<>(true);

    @FalcunSetting("Slot 5")
    FalcunValue<Boolean> slot5 = new FalcunValue<>(true);

    @FalcunSetting("Slot 6")
    FalcunValue<Boolean> slot6 = new FalcunValue<>(true);

    @FalcunSetting("Slot 7")
    FalcunValue<Boolean> slot7 = new FalcunValue<>(true);

    @FalcunSetting("Slot 8")
    FalcunValue<Boolean> slot8 = new FalcunValue<>(true);

    @FalcunSetting("Slot 9")
    FalcunValue<Boolean> slot9 = new FalcunValue<>(true);

    @FalcunSetting("Trace All Key")
    FalcunValue<FalcunKeyBind> keyBind = new FalcunValue<>(new FalcunKeyBind(0));


}
