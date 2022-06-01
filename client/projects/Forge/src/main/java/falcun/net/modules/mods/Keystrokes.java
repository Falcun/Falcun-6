package falcun.net.modules.mods;

import falcun.net.api.colors.FalcunColor;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.modules.ModuleCategory;

import java.awt.*;

@FalcunModuleInfo(fileName = "Keystrokes", name = "Keystrokes", description = "Keystrokes", version = "1.0.0", category = ModuleCategory.MISC)
public class Keystrokes extends FalcunModule {
    @FalcunSetting("Show CPS")
    FalcunValue<Boolean> keystrokecps = new FalcunValue<>(true);

    @FalcunSetting("Show Mouse Buttons")
    FalcunValue<Boolean> keystrokemouse = new FalcunValue<>(true);

    @FalcunSetting("Show Spacebar")
    FalcunValue<Boolean> spacebar = new FalcunValue<>(true);

    @FalcunSetting("Custom Font")
    FalcunValue<Boolean> isCustomFont = new FalcunValue<>(false);

    @FalcunSetting("background Color")
    FalcunValue<FalcunColor> backgroundColor = new FalcunValue<>(new FalcunColor(new Color(0, 0, 0, 150).getRGB()));

    @FalcunSetting("Key Unpressed Color")
    FalcunValue<FalcunColor> color = new FalcunValue<>(new FalcunColor(Color.WHITE.getRGB()));

    @FalcunSetting("Key Presseed Color")
    FalcunValue<FalcunColor> color2 = new FalcunValue<>(new FalcunColor(Color.BLACK.getRGB()));

    @FalcunSetting("Static Chroma")
    FalcunValue<Boolean> isUsingStaticChroma = new FalcunValue<>(false);

    @FalcunSetting("Wave Chroma")
    FalcunValue<Boolean> isUsingWaveChroma = new FalcunValue<>(false);
}
