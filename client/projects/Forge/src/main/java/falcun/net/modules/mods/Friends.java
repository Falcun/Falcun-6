package falcun.net.modules.mods;

import falcun.net.Falcun;
import falcun.net.api.colors.FalcunColor;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.modules.ModuleCategory;

import java.awt.*;

@FalcunModuleInfo(fileName = "Friends", name = "Friends", description = "Friends", version = "1.0.0", category = ModuleCategory.MISC)
public class Friends extends FalcunModule {
    @FalcunSetting("Hide outgoing friend requests")
    FalcunValue<Boolean> HIDE_OUTGOING_FRIEND_REQUESTS = new FalcunValue<>(false);

    @FalcunSetting("Hide incoming friend requests")
    FalcunValue<Boolean> HIDE_INCOMING_FRIEND_REQUESTS = new FalcunValue<>(false);

    @FalcunSetting("Team mode")
    FalcunValue<Boolean> TEAM_MODE = new FalcunValue<>(false);

    @FalcunSetting("Highlight color")
    FalcunValue<FalcunColor> HIGHLIGHT_COLOR = new FalcunValue<>(new FalcunColor(Color.BLUE.getRGB()));

    @FalcunSetting("Hide nametags")
    FalcunValue<Boolean> HIDE_NAMETAGS = new FalcunValue<>(false);

    @FalcunSetting("Hide friends")
    FalcunValue<Boolean> HIDE_FRIENDS = new FalcunValue<>(false);

    @FalcunSetting("Require shift")
    FalcunValue<Boolean> REQUIRE_SHIFT = new FalcunValue<>(false);

    @FalcunSetting("Show notifications")
    FalcunValue<Boolean> SHOW_NOTIFICATIONS = new FalcunValue<>(true);
}
