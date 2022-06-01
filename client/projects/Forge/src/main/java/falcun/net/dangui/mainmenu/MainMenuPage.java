package falcun.net.dangui.mainmenu;

public enum MainMenuPage {
	MAIN("MAIN MENU", () -> {

	}),
	// MAIN BUTTON IS NEVER GOING TO EXIST.
	// LEFT
	OPTIONS("OPTIONS", () -> {

	}),
	MODS("MODS", () -> {

	}),
	// CENTER
	SINGLE("SINGLE PLAYER", () -> {

	}),
	MULTI("MULTI-PLAYER", () -> {

	}),
	QUIT("QUIT-GAME", () -> {

	}),
	// RIGHT
	ACCOUNTS("ACCOUNTS", () -> {

	}),
	REPLAY("REPLAY RECORDINGS", () -> {

	});

	public final String name;
	public final Runnable buttonClick;

	MainMenuPage(String s, Runnable r) {
		name = s;
		buttonClick = r;
	}

	public static MainMenuPage getByName(String s) {
		for (MainMenuPage value : values()) {
			if (value.name.equalsIgnoreCase(s)) {
				return value;
			}
		}
		return null;
	}
}
