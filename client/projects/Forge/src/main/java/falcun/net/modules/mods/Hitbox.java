package falcun.net.modules.mods;

import falcun.net.api.colors.FalcunColor;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunBounds;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.modules.ModuleCategory;

import java.awt.*;

@FalcunModuleInfo(fileName = "Hitbox", name = "Hitbox", description = "Hitbox", version = "1.0.0", category = ModuleCategory.MISC)
public class Hitbox extends FalcunModule {
    @FalcunSetting("Line Width")
    @FalcunBounds(min = 1, max = 20)
    FalcunValue<Integer> lineWidth = new FalcunValue<>(2);

    @FalcunSetting("Line Color")
    FalcunValue<FalcunColor> lineColor = new FalcunValue<>(new FalcunColor(Color.RED.getRGB()));

    enum hitboxes{
        ALL, ENDERPEARLS_ONLY, ITEMS_ONLY, ENDERPEARLSANDITEMSONLY
    }

    @FalcunSetting("Hitbox Modes")
    FalcunValue<hitboxes> hitboxMode = new FalcunValue<>(hitboxes.ALL);
}
