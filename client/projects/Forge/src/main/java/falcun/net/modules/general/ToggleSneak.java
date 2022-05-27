package falcun.net.modules.general;

import falcun.net.api.colors.FalcunColor;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunBounds;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.modules.ModuleCategory;

import java.awt.*;

@FalcunModuleInfo(fileName = "ToggleSneak", name = "Toggle Sneak", description = "ToggleSneak", version = "1.0.0", category = ModuleCategory.MISC)
public class ToggleSneak extends FalcunModule {
    @FalcunSetting("Background")
    FalcunValue<Boolean> backGround = new FalcunValue<>(false);

    @FalcunSetting("Background Color")
    FalcunValue<FalcunColor> background = new FalcunValue<>(new FalcunColor(new Color(0, 0, 0, 150).getRGB()));

    @FalcunSetting("Toggle Sneak")
    FalcunValue<Boolean> optionToggleSneak = new FalcunValue<>(true);

    @FalcunSetting("Toggle Sprint")
    FalcunValue<Boolean> optionToggleSprint = new FalcunValue<>(true);

    @FalcunSetting("Double Tap Sprint")
    FalcunValue<Boolean> optionDoubleTap = new FalcunValue<>(false);

    @FalcunSetting("Fly boost")
    FalcunValue<Boolean> optionEnableFlyBoost  = new FalcunValue<>(true);

    @FalcunSetting("Fly Boost Speed Vertical")
    @FalcunBounds(min = 0.1D, max = 20D)
    FalcunValue<Double> flyboostspeedVertical = new FalcunValue<>(2D);

    @FalcunSetting("Fly Boost Speed Horizontal")
    @FalcunBounds(min = 0.1D, max = 20D)
    FalcunValue<Double> flyboostspeedHorizontal = new FalcunValue<>(2D);

    @FalcunSetting("Custom Font")
    FalcunValue<Boolean> customFont = new FalcunValue<>(false);

    @FalcunSetting("Color")
    FalcunValue<FalcunColor> color = new FalcunValue<>(new FalcunColor(Color.WHITE.getRGB()));

}
