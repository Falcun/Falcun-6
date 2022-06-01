package falcun.net.modules.render;

import falcun.net.api.colors.FalcunColor;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunBounds;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.modules.ModuleCategory;

import java.awt.*;

@FalcunModuleInfo(fileName = "ChunkBorders", name = "ChunkBorders", description = "ChunkBorders", version = "1.0.0", category = ModuleCategory.MISC)
public class ChunkBorders extends FalcunModule {
    @FalcunSetting("Color")
    FalcunValue<FalcunColor> vColor = new FalcunValue<>(new FalcunColor(Color.WHITE.getRGB()));

    @FalcunSetting("Line Width")
    @FalcunBounds(min = 0.1F, max = 7.5F)
    FalcunValue<Float> lineWidth = new FalcunValue<>(1F);

    @FalcunSetting("Radius")
    @FalcunBounds(min = 0, max = 6)
    FalcunValue<Integer> radius = new FalcunValue<>(3);

    @FalcunSetting("Height")
    @FalcunBounds(min = -10, max = 10)
    FalcunValue<Integer> height = new FalcunValue<>(0);

    @FalcunSetting("Position Y")
    @FalcunBounds(min = -10, max = 300)
    FalcunValue<Integer> pos = new FalcunValue<>(100);

    enum ChunkMode{
        STATIC, DYNAMIC, VERTICAL
    }

    @FalcunSetting("Chunkborder mode")
    FalcunValue<ChunkMode> mode = new FalcunValue<>(ChunkMode.STATIC);

    @FalcunSetting("Chroma")
    FalcunValue<Boolean> isUsingChroma = new FalcunValue<>(false);
}
