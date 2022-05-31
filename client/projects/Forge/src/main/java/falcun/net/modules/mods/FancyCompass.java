package falcun.net.modules.mods;

import falcun.net.Falcun;
import falcun.net.api.colors.FalcunColor;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.modules.ModuleCategory;

import java.awt.*;

@FalcunModuleInfo(fileName = "FancyCompass", name = "FancyCompass", description = "FancyCompass", version = "1.0.0", category = ModuleCategory.MISC)
public class FancyCompass extends FalcunModule {
    @FalcunSetting("Marker Color")
    FalcunValue<FalcunColor> colorM = new FalcunValue<>(new FalcunColor(Color.WHITE.getRGB()));

    @FalcunSetting("Direction Color")
    FalcunValue<FalcunColor> colorD = new FalcunValue<>(new FalcunColor(Color.WHITE.getRGB()));

    @FalcunSetting("Background")
    FalcunValue<Boolean> isBackground = new FalcunValue<>(false);

    @FalcunSetting("Background Color")
    FalcunValue<FalcunColor> background = new FalcunValue<>(new FalcunColor(new Color(0, 0, 0, 100).getRGB()));



}
