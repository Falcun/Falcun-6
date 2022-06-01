package falcun.net.modules.other;

import falcun.net.api.colors.FalcunColor;
import falcun.net.api.colors.FalcunColorCreator;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunKeyBind;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.modules.ModuleCategory;

@FalcunModuleInfo(fileName = "StopWatch", name = "Stop Watch", description = "StopWatch", version = "1.0.0", category = ModuleCategory.MISC)
public class BlockCoordinates extends FalcunModule {

    @FalcunSetting("Show Name")
    public FalcunValue<Boolean> showName = new FalcunValue<>(true);

    @FalcunSetting("Show Icon")
    public FalcunValue<Boolean> showIcon = new FalcunValue<>(true);

    @FalcunSetting("Custom Font")
    public FalcunValue<Boolean> customFont = new FalcunValue<>(true);

    @FalcunSetting("Shaded Coords")
    public FalcunValue<Boolean> shadedCoords = new FalcunValue<>(true);

    @FalcunSetting("Shout Coords Key")
    public FalcunValue<FalcunKeyBind> keyBind = new FalcunValue<>(new FalcunKeyBind());


    @FalcunSetting("Chat Format")
    public FalcunValue<String> chatFormat = new FalcunValue<>("X: {x}, Y: {y}, Z: {z} Blocks: {block}");


    @FalcunSetting("Background Color")
    public FalcunValue<FalcunColor> background = new FalcunValue<>(new FalcunColorCreator().setRed(0).setBlue(0).setGreen(0).setAlpha(150).getColor());


    @FalcunSetting("Label Color")
    public FalcunValue<FalcunColor> color = new FalcunValue<>(new FalcunColorCreator().setColor(0xffffffff).getColor());

    @FalcunSetting("Value Color")
    public FalcunValue<FalcunColor> vColor = new FalcunValue<>(new FalcunColorCreator().setColor(0xffffffff).getColor());

    @FalcunSetting("Static Chroma")
    public FalcunValue<Boolean> isUsingStaticChroma = new FalcunValue<>(false);


    @FalcunSetting("Wave Chroma")
    public FalcunValue<Boolean> isUsingWaveChroma = new FalcunValue<>(false);
}
