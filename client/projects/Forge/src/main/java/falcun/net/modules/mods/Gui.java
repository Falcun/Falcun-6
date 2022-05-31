package falcun.net.modules.mods;

import falcun.net.Falcun;
import falcun.net.api.colors.FalcunColor;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunBounds;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.modules.ModuleCategory;

import java.awt.*;

@FalcunModuleInfo(fileName = "Gui", name = "Gui", description = "Gui", version = "1.0.0", category = ModuleCategory.MISC)
public class Gui extends FalcunModule {
    @FalcunSetting("Blur Radius")
    @FalcunBounds(min = 1, max = 100)
    FalcunValue<Integer> radius = new FalcunValue<>(12);

    @FalcunSetting("Fade Time")
    @FalcunBounds(min = 1, max = 1000)
    FalcunValue<Integer> fadeTime = new FalcunValue<>(200);

    @FalcunSetting("Gui Blur")
    FalcunValue<Boolean> guiBlur = new FalcunValue<>(true);

    @FalcunSetting("Background Color")
    FalcunValue<FalcunColor> colorD = new FalcunValue<>(new FalcunColor(new Color(18, 17, 22, 255).getRGB()));
}
