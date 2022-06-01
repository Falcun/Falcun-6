package net.mattbenson.modules.types.fpssettings.cruches;

import net.mattbenson.Falcun;
import net.mattbenson.modules.types.fpssettings.FPSSettings;

public class WorldHandler {
	//Code at WorldClient.java
	public static int getAnimationTickCount() {
		return Falcun.getInstance().moduleManager.getModule(FPSSettings.class).LOW_ANIMATION_TICK ? 100 : 1000;
	}
}
