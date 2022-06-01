package falcun.net.modules.render;

import falcun.net.api.colors.FalcunColor;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunBounds;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.modules.ModuleCategory;

import java.awt.*;

@FalcunModuleInfo(fileName = "BlockOverlay", name = "BlockOverlay", description = "BlockOverlay", version = "1.0.0", category = ModuleCategory.MISC)
public class BlockOverlay extends FalcunModule {
    enum Modes{
        OUTLINE, HIGHLIGHT, BOTH
    }

    @FalcunSetting("Mode")
    FalcunValue<Modes> mode = new FalcunValue<>(Modes.OUTLINE);

    @FalcunSetting("Outline Color")
    FalcunValue<FalcunColor> vColor = new FalcunValue<>(new FalcunColor(Color.BLUE.getRGB()));

    @FalcunSetting("Highlight Color")
    FalcunValue<FalcunColor> hColor = new FalcunValue<>(new FalcunColor(Color.BLUE.getRGB()));

    @FalcunSetting("LIne Width")
    @FalcunBounds(min = 0.1F, max = 7.5F)
    FalcunValue<Float> lineWidth = new FalcunValue<>(1F);

    @FalcunSetting("Chroma")
    FalcunValue<Boolean> isUsingChroma = new FalcunValue<>(false);
}
