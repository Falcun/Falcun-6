package falcun.net.modules.factions;

import falcun.net.Falcun;
import falcun.net.api.colors.FalcunColor;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunBounds;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.modules.ModuleCategory;

import java.awt.*;

@FalcunModuleInfo(fileName = "Breadcrumbs", name = "Breadcrumbs", description = "Breadcrumbs", version = "1.0.0", category = ModuleCategory.FACS)
public class Breadcrumbs extends FalcunModule {
    @FalcunSetting("Visualize Explosions")
    FalcunValue<Boolean> visualized = new FalcunValue<>(true);

    @FalcunSetting("Display Timer (sec)")
    @FalcunBounds(min = 1, max = 60)
    FalcunValue<Integer> age = new FalcunValue<>(15);

    @FalcunSetting("Track TNT")
    FalcunValue<Boolean> tnt = new FalcunValue<>(true);

    @FalcunSetting("TNT Color")
    FalcunValue<FalcunColor> vColor = new FalcunValue<>(new FalcunColor(Color.RED.getRGB()));

    @FalcunSetting("Sand Color")
    FalcunValue<FalcunColor> sandColor = new FalcunValue<>(new FalcunColor(Color.YELLOW.getRGB()));

    @FalcunSetting("Explosive Color")
    FalcunValue<FalcunColor> explosionColor = new FalcunValue<>(new FalcunColor(Color.RED.getRGB()));

    @FalcunSetting("Track sand")
    FalcunValue<Boolean> sand = new FalcunValue<>(true);

    @FalcunSetting("Line width")
    @FalcunBounds(min = 0.1f, max = 5.0f)
    FalcunValue<Float> lineWidth = new FalcunValue<>(1f);

    @FalcunSetting("Disable If Near Dispensers")
    FalcunValue<Boolean> dispenserChec = new FalcunValue<>(true);

    enum Render_Mode{
        POINTS, LINE, LINE_STRIP, LINE_LOOP
    }

    @FalcunSetting("Render Mode")
    FalcunValue<Render_Mode> mode = new FalcunValue<>(Render_Mode.LINE);



}
