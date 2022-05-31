package falcun.net.modules.mods;

import falcun.net.api.colors.FalcunColor;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunBounds;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.modules.ModuleCategory;

import java.awt.*;

@FalcunModuleInfo(fileName = "GroupParchcrumb", name = "GroupParchcrumb", description = "GroupParchcrumb", version = "1.0.0", category = ModuleCategory.MISC)
public class GroupParchcrumb extends FalcunModule {
    @FalcunSetting("Display Timer (sec)")
    @FalcunBounds(min = 1, max = 60)
    FalcunValue<Integer> age = new FalcunValue<>(5);

    @FalcunSetting("Send Chat Notification")
    FalcunValue<Boolean> isNotification = new FalcunValue<>(true);

    @FalcunSetting("Patch Line Color")
    FalcunValue<FalcunColor> patchColor = new FalcunValue<>(new FalcunColor(Color.ORANGE.getRGB()));
}
