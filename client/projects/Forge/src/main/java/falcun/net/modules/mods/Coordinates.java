package falcun.net.modules.mods;

import falcun.net.Falcun;
import falcun.net.api.colors.FalcunColor;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.*;
import falcun.net.modules.ModuleCategory;

import java.awt.*;

@FalcunModuleInfo(fileName = "Coordinates", name = "Coordinates", description = "Coordinates", version = "1.0.0", category = ModuleCategory.MISC)
public class Coordinates extends FalcunModule {
    @FalcunSetting("Shot Compass")
    FalcunValue<Boolean> showCompass = new FalcunValue<>(true);

    @FalcunSetting("Show Label")
    FalcunValue<Boolean> showLabel = new FalcunValue<>(true);

    @FalcunSetting("Show Direction")
    FalcunValue<Boolean> showDirection = new FalcunValue<>(true);

    @FalcunSetting("Show Biome")
    FalcunValue<Boolean> showBiome = new FalcunValue<>(true);

    @FalcunSetting("Biome Preset Color")
    FalcunValue<Boolean> biomePresetColor = new FalcunValue<>(true);

    @FalcunSetting("Background")
    FalcunValue<Boolean> shadedCoords = new FalcunValue<>(true);

    @FalcunSetting("Static Chroma")
    FalcunValue<Boolean> isUsingStaticChroma = new FalcunValue<>(false);

    @FalcunSetting("Wave Chroma")
    FalcunValue<Boolean> isUsingWaveChroma = new FalcunValue<>(false);

    @FalcunSetting("Custom Font")
    FalcunValue<Boolean> customFont = new FalcunValue<>(false);

    @FalcunSetting("Show Avatar")
    FalcunValue<Boolean> showAvatar = new FalcunValue<>(false);

    @FalcunSetting("Shout Coordinates Key")
    FalcunValue<FalcunKeyBind> keyBind = new FalcunValue<>(new FalcunKeyBind(0));

    @FalcunSetting("Chat Format")
    FalcunValue<String> chatFormat = new FalcunValue<>("X: {x}, Y: {y}, Z: {z}");

    enum DisplayMode{
        HORIZONTAL, VERTICAL
    }
    @FalcunSetting("Display Mode")
    FalcunValue<DisplayMode> displayMode = new FalcunValue<>(DisplayMode.VERTICAL);

    @FalcunSetting("Background Color")
    FalcunValue<FalcunColor> backgroundColor = new FalcunValue<>(new FalcunColor(new Color(0, 0, 0, 150).getRGB()));

    @FalcunSetting("Label Color")
    FalcunValue<FalcunColor> color = new FalcunValue<>(new FalcunColor(Color.WHITE.getRGB()));

    @FalcunSetting("Value Color")
    FalcunValue<FalcunColor> vColor = new FalcunValue<>(new FalcunColor(Color.WHITE.getRGB()));

    @FalcunSetting("Compass Color")
    FalcunValue<FalcunColor> tColor = new FalcunValue<>(new FalcunColor(Color.WHITE.getRGB()));

    @FalcunSetting("Biome Color")
    FalcunValue<FalcunColor> bColor = new FalcunValue<>(new FalcunColor(Color.WHITE.getRGB()));

}
