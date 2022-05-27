package falcun.net.modules.factions;

import falcun.net.api.colors.FalcunColor;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunBounds;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.modules.ModuleCategory;

import java.awt.*;

@FalcunModuleInfo(fileName = "WorldEditCUI", name = "World Edit CUI", description = "WorldEditCUI", version = "1.0.0", category = ModuleCategory.FACS)
public class WorldEditCUI extends FalcunModule {
    @FalcunSetting("See Through")
    FalcunValue<Boolean> seeThrough = new FalcunValue<>(true);

    @FalcunSetting("Highlight Color")
    FalcunValue<FalcunColor> hColor = new FalcunValue<>(new FalcunColor(Color.BLACK.getRGB()));

    @FalcunSetting("Border Color")
    FalcunValue<FalcunValue> vColor = new FalcunValue<>(new FalcunValue(Color.BLACK.getRGB()));

    @FalcunSetting("Info Color")
    FalcunValue<FalcunValue> iColor = new FalcunValue<>(new FalcunValue(Color.WHITE.getRGB()));

    @FalcunSetting("Line Width")
    @FalcunBounds(min = 0.1F, max = 7.5F)
    FalcunValue<Float> lineWidth = new FalcunValue<>(1F);
}
