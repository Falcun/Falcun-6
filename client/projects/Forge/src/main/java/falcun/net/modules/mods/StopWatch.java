package falcun.net.modules.mods;

import falcun.net.api.colors.FalcunColor;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.*;
import falcun.net.modules.ModuleCategory;

import java.awt.*;

@FalcunModuleInfo(fileName = "StopWatch", name = "Stop Watch", description = "StopWatch", version = "1.0.0", category = ModuleCategory.MISC)
public class StopWatch extends FalcunModule {
	@FalcunSetting("Background")
	FalcunValue<Boolean> background = new FalcunValue<>(true);

	@FalcunSetting("Background Color")
	FalcunValue<FalcunColor> backgroundColor = new FalcunValue<>(new FalcunColor(new Color(0, 0, 0, 150).getRGB()));

	@FalcunSetting("Custom Font")
	FalcunValue<Boolean> customFont = new FalcunValue<>(false);

	@FalcunSetting("Color")
	FalcunValue<FalcunColor> color = new FalcunValue<>(new FalcunColor(Color.WHITE.getRGB()));

    @FalcunSetting("Start/Pause Key")
    FalcunValue<FalcunKeyBind> startkeyBind = new FalcunValue<>(new FalcunKeyBind(0));

	@FalcunKey
	@FalcunSetting("Reset Key")
	FalcunValue<FalcunKeyBind> resetkeyBind = new FalcunValue<>(new FalcunKeyBind(0));

	@FalcunSetting("Static Chroma")
	FalcunValue<Boolean> isUsingStaticChroma = new FalcunValue<>(false);

	@FalcunSetting("Wave Chroma")
	FalcunValue<Boolean> isUsingWaveChroma = new FalcunValue<>(false);
}
