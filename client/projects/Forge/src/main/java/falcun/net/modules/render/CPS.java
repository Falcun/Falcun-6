package falcun.net.modules.render;

import falcun.net.api.colors.FalcunColor;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.modules.ModuleCategory;

import java.awt.*;

@FalcunModuleInfo(fileName = "CPS", name = "CPS", description = "CPS", version = "1.0.0", category = ModuleCategory.MISC)
public class CPS extends FalcunModule {
    @FalcunSetting("Right Click Counter")
    FalcunValue<Boolean> rightClick = new FalcunValue<>(false);

    @FalcunSetting("Color")
    FalcunValue<FalcunColor> color = new FalcunValue<>(new FalcunColor(Color.WHITE.getRGB()));

    @FalcunSetting("Background")
    FalcunValue<Boolean> backGround = new FalcunValue<>(true);

    @FalcunSetting("Background Color")
    FalcunValue<FalcunColor> background = new FalcunValue<>(new FalcunColor(new Color(0, 0, 0, 150).getRGB()));

    @FalcunSetting("Custom Font")
    FalcunValue<Boolean> customFont = new FalcunValue<>(false);

    @FalcunSetting("Static Chroma")
    FalcunValue<Boolean> isUsingStaticChroma = new FalcunValue<>(false);

    @FalcunSetting("Wave Chroma")
    FalcunValue<Boolean> isUsingWaveChroma = new FalcunValue<>(false);

}
