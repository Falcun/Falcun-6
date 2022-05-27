package falcun.net.modules.other;

import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.modules.ModuleCategory;

@FalcunModuleInfo(fileName = "Screenshotupload", name = "Screenshotupload", description = "Screenshot to imgur", version = "1.0.0", category = ModuleCategory.MISC)
public class ScreenshotUpload extends FalcunModule {
    @FalcunSetting("Link")
    public FalcunValue<Boolean> link = new FalcunValue<>(true);

    @FalcunSetting("Open")
    public FalcunValue<Boolean> open = new FalcunValue<>(true);

    @FalcunSetting("Delete")
    public FalcunValue<Boolean> delete = new FalcunValue<>(true);

    @FalcunSetting("Copy")
    public FalcunValue<Boolean> copy = new FalcunValue<>(true);

    public static ScreenshotUpload instance;
    public ScreenshotUpload(){
        instance = this;
    }
}
