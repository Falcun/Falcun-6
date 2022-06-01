package falcun.net.modules.mods;

import falcun.net.api.colors.FalcunColor;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.modules.ModuleCategory;

import java.awt.*;

@FalcunModuleInfo(fileName = "PotionStatus", name = "PotionStatus", description = "PotionStatus", version = "1.0.0", category = ModuleCategory.MISC)
public class PotionStatus extends FalcunModule {
    enum potionDesign{
        BOXES, ICON, SLIM
    }
    @FalcunSetting("Design")
    FalcunValue<potionDesign> potionstatusdesign = new FalcunValue<>(potionDesign.ICON);

    @FalcunSetting("Preset Potion Color")
    FalcunValue<Boolean> presetColor = new FalcunValue<>(true);

    @FalcunSetting("Potion Color")
    FalcunValue<FalcunColor> potionColor = new FalcunValue<>(new FalcunColor(Color.WHITE.getRGB()));
}
