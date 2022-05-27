package falcun.net.modules;

import net.minecraft.util.ResourceLocation;

public enum ModuleCategory {

	ALL("ALL MODS", "falcun:gui/maximize.png"),
	FACS("FACTIONS", "falcun:gui/badge.png"),
	HYPIXEL("HYPIXEL", "falcun:gui/hypixel_icon.png"),
	SKYBLOCK("SKYBLOCK", "falcun:gui/skyblock_icon.png"),
	COMBAT("COMBAT", "falcun:gui/user.png"),
	RENDER("RENDER", "falcun:gui/laptop.png"),
	MISC("OTHER", "falcun:gui/other.png"),
	GROUPS("GROUPS", "falcun:gui/groups.png");

	public final String name;
	public final ResourceLocation icon;

	ModuleCategory(String name, String resource) {
		this.name = name;
		this.icon = new ResourceLocation(resource);
	}
}
