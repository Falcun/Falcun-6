package falcun.net.modules.hypixel.skyblock.skytils.dungeons;

import falcun.net.api.colors.FalcunColor;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.api.modules.inheritance.FalcunEventBusModule;
import falcun.net.modules.ModuleCategory;

import java.awt.*;

@FalcunModuleInfo(fileName = "DungeonBlockOverlay", name = "Dungeon Block Overlay", description = "Color overlays for blocks", version = "1.0.0", category = ModuleCategory.SKYBLOCK)
public class DungeonBlockOverlay extends FalcunModule implements FalcunEventBusModule {
    @FalcunSetting("Cracked Bricks")
    public static FalcunValue<FalcunColor> crackedBricks = new FalcunValue<FalcunColor>(new FalcunColor(Color.CYAN.getRGB()));

    @FalcunSetting("Dispensers")
    public static FalcunValue<FalcunColor> dispensers = new FalcunValue<FalcunColor>(new FalcunColor(Color.ORANGE.getRGB()));

    @FalcunSetting("Levers")
    public static FalcunValue<FalcunColor> levers = new FalcunValue<FalcunColor>(new FalcunColor(Color.CYAN.getRGB()));

    @FalcunSetting("Tripwire String")
    public static FalcunValue<FalcunColor> tripwireString = new FalcunValue<FalcunColor>(new FalcunColor(Color.RED.getRGB()));

    @FalcunSetting("Normal Chests")
    public static FalcunValue<FalcunColor> normalChests = new FalcunValue<FalcunColor>(new FalcunColor(Color.GREEN.getRGB()));

    @FalcunSetting("Trapped Chests")
    public static FalcunValue<FalcunColor> trappedChests = new FalcunValue<FalcunColor>(new FalcunColor(Color.GREEN.getRGB()));

    @FalcunSetting("Bats")
    public static FalcunValue<FalcunColor> bats = new FalcunValue<FalcunColor>(new FalcunColor(Color.GREEN.getRGB()));
}
