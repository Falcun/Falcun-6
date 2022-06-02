package falcun.net.guidan.mainmenu;

import falcun.net.managers.FalcunGuiManager;

public enum MainMenuPage {
	MAIN("MAIN MENU", () -> FalcunGuiMainMenu.selectedPage = getByName("MAIN")),
	// MAIN BUTTON IS NEVER GOING TO EXIST.
	// LEFT
	OPTIONS("OPTIONS", () -> FalcunGuiMainMenu.selectedPage = getByName("OPTIONS")),
	MODS("MODS", () -> FalcunGuiMainMenu.selectedPage = getByName("MODS")),
	// CENTER
	SINGLE("SINGLE PLAYER", FalcunGuiManager::openSinglePlayerMenu),
	MULTI("MULTI-PLAYER", FalcunGuiManager::openMultiPlayerMenu),
	QUIT("QUIT-GAME", () -> {
//		Falcun.minecraft.shutdown();
	}),
	// RIGHT
	ACCOUNTS("ACCOUNTS", () -> FalcunGuiManager.openAccountSwitchMenu(new FalcunGuiMainMenu())),
	REPLAY("REPLAY RECORDINGS", () -> FalcunGuiMainMenu.selectedPage = getByName("REPLAY RECORDINGS"));

	public final String name;
	public final Runnable buttonClick;

	MainMenuPage(String s, Runnable r) {
		name = s;
		buttonClick = r;
	}

	public static MainMenuPage getByName(String s) {
		for (MainMenuPage value : values()) {
			if (value.name.equalsIgnoreCase(s) || value.name().equalsIgnoreCase(s)) {
				return value;
			}
		}
		return null;
	}
}
