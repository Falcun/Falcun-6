package falcun.net.modules.mods;

import falcun.net.api.colors.FalcunColor;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.modules.ModuleCategory;

import java.awt.*;

@FalcunModuleInfo(fileName = "MouseHUD", name = "MouseHUD", description = "MouseHUD", version = "1.0.0", category = ModuleCategory.MISC)
public class MouseHUD extends FalcunModule {
    @FalcunSetting("Background Color")
    FalcunValue<FalcunColor> background = new FalcunValue<>(new FalcunColor(new Color(0, 0, 0, 150).getRGB()));

    @FalcunSetting("Mouse Color")
    FalcunValue<FalcunColor> mouseColor = new FalcunValue<>(new FalcunColor(Color.BLACK.getRGB()));

    @FalcunSetting("Center Color")
    FalcunValue<FalcunColor> centerColor = new FalcunValue<>(new FalcunColor(Color.WHITE.getRGB()));

    @FalcunSetting("Sidebar Color")
    FalcunValue<FalcunColor> sidebarColor = new FalcunValue<>(new FalcunColor(Color.BLACK.getRGB()));

    @FalcunSetting("Font Color")
    FalcunValue<FalcunColor> fontColor = new FalcunValue<>(new FalcunColor(Color.WHITE.getRGB()));

    enum mouseMode{
        LEFT, RIGHT
    }

    @FalcunSetting("Mode")
    FalcunValue<mouseMode> Mode = new FalcunValue<>(mouseMode.RIGHT);
}
