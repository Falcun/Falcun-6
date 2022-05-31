package falcun.net.modules.mods;

import falcun.net.api.colors.FalcunColor;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunBounds;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.modules.ModuleCategory;

import java.awt.*;

@FalcunModuleInfo(fileName = "MapWriter", name = "MapWriter", description = "MapWriter", version = "1.0.0", category = ModuleCategory.MISC)
public class MapWriter extends FalcunModule {
    enum Modes{
        CIRCLE, RECTANGLE
    }
    @FalcunSetting("Map Modes")
    FalcunValue<Modes> mode = new FalcunValue<>(Modes.CIRCLE);

    enum Sizes{
        SMALL, LARGE, DISABLED
    }

    @FalcunSetting("Coords Modes")
    FalcunValue<Sizes> coordsMode = new FalcunValue<>(Sizes.SMALL);

    @FalcunSetting("Show border")
    FalcunValue<Boolean> showBorder = new FalcunValue<>(false);

    @FalcunSetting("Show Grid")
    FalcunValue<Boolean> showGrid = new FalcunValue<>(false);

    @FalcunSetting("Grid Color")
    FalcunValue<FalcunColor> gridColor = new FalcunValue<>(new FalcunColor(Color.GRAY.getRGB()));

    @FalcunSetting("Grid With")
    @FalcunBounds(min = 0.1D, max = 1D)
    FalcunValue<Double> gridWidth = new FalcunValue<>(0.25D);

    @FalcunSetting("Show Factions Overlay")
    FalcunValue<Boolean> showFactionsOverlay = new FalcunValue<>(false);

    @FalcunSetting("Show Group Overlay")
    FalcunValue<Boolean> showGroupOverlay = new FalcunValue<>(true);

    @FalcunSetting("Render Radius")
    @FalcunBounds(min = 4, max = 10)
    FalcunValue<Integer> renderRadius = new FalcunValue<>(6);

    @FalcunSetting("Ticks between Updates")
    @FalcunBounds(min = 20, max = 500)
    FalcunValue<Integer> ticksBetweenUpdates = new FalcunValue<>(200);
}
