package falcun.net.modules.mods;

import falcun.net.api.colors.FalcunColor;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.modules.ModuleCategory;

import java.awt.*;

@FalcunModuleInfo(fileName = "ArmorStatus", name = "Armor Status", description = "ArmorStatus", version = "1.0.0", category = ModuleCategory.MISC)
public class ArmorStatus extends FalcunModule {

    @FalcunSetting("Item Name")
    FalcunValue<Boolean> armorstatusitemname = new FalcunValue<>(false);

    @FalcunSetting("Item Equipped")
    FalcunValue<Boolean> armorstatusitemequipped = new FalcunValue<>(true);

    @FalcunSetting("Preset Durability Colors")
    FalcunValue<Boolean> duraColors = new FalcunValue<>(true);

    @FalcunSetting("Durability Color")
    FalcunValue<FalcunColor> valueColor = new FalcunValue<>(new FalcunColor(Color.WHITE.getRGB()));

    @FalcunSetting("Custom Font")
    FalcunValue<Boolean> customFont = new FalcunValue<>(true);
    enum Direction{
        VERTICAL, HORIZONTAL
    }

    @FalcunSetting("Direction")
    FalcunValue<Direction> armourstatusdirection = new FalcunValue<>(Direction.VERTICAL);
    enum Value_Display{
        PERCENT, VALUE_DAMAGE_MAX, VALUE_DAMAGE
    }

    FalcunValue<Value_Display> armorstatusdisplay = new FalcunValue<>(Value_Display.VALUE_DAMAGE);
    enum ValueLeftRight{
        LEFT, RIGHT
    }
    FalcunValue<ValueLeftRight> leftRight = new FalcunValue<>(ValueLeftRight.LEFT);
}
