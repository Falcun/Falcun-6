package falcun.net.modules.render;

import falcun.net.api.colors.FalcunColor;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.modules.ModuleCategory;
import org.fusesource.jansi.Ansi;

import java.awt.*;

@FalcunModuleInfo(fileName = "ColoredRedstone", name = "ColoredRedstone", description = "ColoredRedstone", version = "1.0.0", category = ModuleCategory.MISC)
public class ColoredRedstone extends FalcunModule {
    @FalcunSetting("Redstone Color")
    FalcunValue<FalcunColor> redstoneColor = new FalcunValue<>(new FalcunColor(Color.RED.getRGB()));
}
