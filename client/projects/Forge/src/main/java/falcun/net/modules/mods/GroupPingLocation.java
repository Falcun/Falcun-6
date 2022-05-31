package falcun.net.modules.mods;

import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.*;
import falcun.net.modules.ModuleCategory;

@FalcunModuleInfo(fileName = "GroupParchcrumb", name = "GroupParchcrumb", description = "GroupParchcrumb", version = "1.0.0", category = ModuleCategory.MISC)
public class GroupPingLocation extends FalcunModule {
    @FalcunSetting("Display Timer (sec)")
    @FalcunBounds(min = 1, max = 60)
    FalcunValue<Integer> age = new FalcunValue<>(15);

    @FalcunSetting("Scale")
    @FalcunBounds(min = 1, max = 10)
    FalcunValue<Integer> scale = new FalcunValue<>(5);

    @FalcunSetting("Sends coordiantes to your group")
    FalcunValue<FalcunKeyBind> keyBind = new FalcunValue<>(new FalcunKeyBind(0));

    @FalcunSetting("Ping Location Key Horizontal")
    FalcunValue<Integer> keyBind1 = new FalcunValue<>(0);

    @FalcunSetting("Ping Mouse Location")
    FalcunValue<Boolean> isMouseLocation = new FalcunValue<>(false);
}
