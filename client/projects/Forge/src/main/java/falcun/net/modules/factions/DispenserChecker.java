package falcun.net.modules.factions;

import falcun.net.api.colors.FalcunColor;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.modules.ModuleCategory;

import java.awt.*;

@FalcunModuleInfo(fileName = "DispenserChecker", name = "DispenserChecker", description = "DispenserChecker", version = "1.0.0", category = ModuleCategory.FACS)
public class DispenserChecker extends FalcunModule {

    @FalcunSetting("Empty Highlight Color")
    FalcunValue<FalcunColor> hColor = new FalcunValue<>(new FalcunColor(Color.GREEN.getRGB()));

    @FalcunSetting("Tnt Highlight Color")
    FalcunValue<FalcunColor> tColor = new FalcunValue<>(new FalcunColor(Color.RED.getRGB()));

    @FalcunSetting("Not Empty Color")
    FalcunValue<FalcunColor> notEmptyColor = new FalcunValue<>(new FalcunColor(Color.YELLOW.getRGB()));

    enum Mode{
        MANUAL, DISPENSE
    }

    @FalcunSetting("Mode")
    FalcunValue<Mode> mode = new FalcunValue<>(Mode.MANUAL);


}
