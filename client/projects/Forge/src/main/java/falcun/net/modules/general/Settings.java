package falcun.net.modules.general;


import falcun.net.api.colors.FalcunColor;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.modules.ModuleCategory;

import java.awt.*;

@FalcunModuleInfo(fileName = "Settings", name = "Settings", description = "Settings", version = "1.0.0", category = ModuleCategory.MISC)
public class Settings extends FalcunModule {
    @FalcunSetting("F5 Nametags")
    FalcunValue<Boolean> F5Nametags = new FalcunValue<>(true);

    @FalcunSetting("Sort Tab To Prioritize Falcun Users")
    FalcunValue<Boolean> sortTab = new FalcunValue<>(true);

    @FalcunSetting("Show Falcun Logo On Tab")
    FalcunValue<Boolean> showFalcunLogoOnTab = new FalcunValue<>(true);

    @FalcunSetting("Inventory Search")
    FalcunValue<Boolean> inventorySearch = new FalcunValue<>(true);

    @FalcunSetting("Inventory Search Color")
    FalcunValue<FalcunColor> inventorySearchColor = new FalcunValue<>(new FalcunColor(new Color(127,255,0, 150).getRGB()));
}
