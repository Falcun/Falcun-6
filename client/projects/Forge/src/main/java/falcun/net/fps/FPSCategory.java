package falcun.net.fps;

import net.minecraft.util.ResourceLocation;

public enum FPSCategory {
	ALL("ALL", "All settings to help improve performance!", "falcun:gui/fpsicons/all.png"),
	LIGHTING("LIGHTING", "Toggle specific lighting checks to improve your fps, or to improve the look of your game.", "falcun:gui/fpsicons/lighting.png"),

	;
	public final String name, desc;
	public final ResourceLocation icon;

	FPSCategory(String name, String description, String resource) {
		this.name = name;
		this.desc = description;
		this.icon = new ResourceLocation(resource);
	}


}
