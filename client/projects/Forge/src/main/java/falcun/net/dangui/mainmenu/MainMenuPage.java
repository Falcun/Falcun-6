package falcun.net.dangui.mainmenu;

public enum MainMenuPage {
	MAIN("Main Menu"), SINGLE("SINGLE PLAYER"), MULTI("MULTI-PLAYER"), OPTIONS("OPTIONS"),  ACCOUNTS("ACCOUNTS"), REPLAY ("REPLAY RECORDINGS"), MODS("MOD LIST");

	public final String name;

	MainMenuPage(String s) {
		name = s;
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
