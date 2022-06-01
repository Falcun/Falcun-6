package falcun.net.modules.general;

import falcun.net.api.colors.FalcunColor;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunBounds;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.modules.ModuleCategory;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MovementInputFromOptions;

import java.awt.*;

@FalcunModuleInfo(fileName = "ToggleSneak", name = "Toggle Sneak", description = "ToggleSneak", version = "1.0.0", category = ModuleCategory.MISC)
public class ToggleSneak extends FalcunModule {

    @FalcunSetting("Background")
    public  FalcunValue<Boolean> backGround = new FalcunValue<>(false);

    @FalcunSetting("Background Color")
    public   FalcunValue<FalcunColor> background = new FalcunValue<>(new FalcunColor(new Color(0, 0, 0, 150).getRGB()));

    @FalcunSetting("Toggle Sneak")
    public FalcunValue<Boolean> optionToggleSneak = new FalcunValue<>(true);

    @FalcunSetting("Toggle Sprint")
    public   FalcunValue<Boolean> optionToggleSprint = new FalcunValue<>(true);

    @FalcunSetting("Double Tap Sprint")
    public FalcunValue<Boolean> optionDoubleTap = new FalcunValue<>(false);

    @FalcunSetting("Fly boost")
public    FalcunValue<Boolean> optionEnableFlyBoost  = new FalcunValue<>(true);

    @FalcunSetting("Fly Boost Speed Vertical")
    @FalcunBounds(min = 0.1D, max = 20D)
    public FalcunValue<Double> flyboostspeedVertical = new FalcunValue<>(2D);

    @FalcunSetting("Fly Boost Speed Horizontal")
    @FalcunBounds(min = 0.1D, max = 20D)
  public  FalcunValue<Double> flyboostspeedHorizontal = new FalcunValue<>(2D);

    @FalcunSetting("Custom Font")
    public   FalcunValue<Boolean> customFont = new FalcunValue<>(false);

    @FalcunSetting("Color")
    public  FalcunValue<FalcunColor> color = new FalcunValue<>(new FalcunColor(Color.WHITE.getRGB()));

public boolean wasSprintDisabled = false;

public boolean  sprintHeldAndReleased = false, sprintDoubleTapped = false;
    public boolean sprint = false;

    public static ToggleSneak instance = null;
    public ToggleSneak(){
        instance = this;
    }
    public static boolean isToggleSneak(){
        return instance != null && instance.isEnabled();
    }

    public void updateSprint(boolean newValue, boolean doubleTapped){
    }

    public void updateMovement(MovementInputFromOptions options, EntityPlayerSP thisPlayer) {
    }

}
