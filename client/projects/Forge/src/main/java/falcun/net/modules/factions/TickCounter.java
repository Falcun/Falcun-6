package falcun.net.modules.factions;

import falcun.net.api.colors.FalcunColor;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.modules.ModuleCategory;

import java.awt.*;

@FalcunModuleInfo(fileName = "TickCounter", name = "Tick Counter", description = "TickCounter", version = "1.0.0", category = ModuleCategory.FACS)
public class TickCounter extends FalcunModule {
    @FalcunSetting("Fill")
    FalcunValue<Boolean> turbosettingsfill = new FalcunValue<>(true);

    @FalcunSetting("Redstone Ticks")
    FalcunValue<Boolean> redstoneTicks = new FalcunValue<>(false);

    @FalcunSetting("Game Ticks")
    FalcunValue<Boolean> gameTicks = new FalcunValue<>(false);

    @FalcunSetting("Seconds")
    FalcunValue<Boolean> seconds = new FalcunValue<>(false);

    @FalcunSetting("Type Color")
    FalcunValue<FalcunColor> tColor = new FalcunValue<>(new FalcunColor(Color.WHITE.getRGB()));

    @FalcunSetting("Value Color")
    FalcunValue<FalcunColor> vColor = new FalcunValue<>(new FalcunColor(Color.WHITE.getRGB()));

    @FalcunSetting("Static Chroma")
    FalcunValue<Boolean> isUsingStaticChroma = new FalcunValue<>(false);

    @FalcunSetting("Wave Chroma")
    FalcunValue<Boolean> isUsingWaveChroma = new FalcunValue<>(false);
}
