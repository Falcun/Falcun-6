package falcun.net.modules.mods;

import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunBounds;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.modules.ModuleCategory;

@FalcunModuleInfo(fileName = "MotionBlur", name = "MotionBlur", description = "MotionBlur", version = "1.0.0", category = ModuleCategory.MISC)
public class MotionBlur extends FalcunModule {
    @FalcunSetting("Blur Amount")
    @FalcunBounds(min = 1.0, max = 10.0)
    FalcunValue<Double> amount = new FalcunValue<>(2D);
}
