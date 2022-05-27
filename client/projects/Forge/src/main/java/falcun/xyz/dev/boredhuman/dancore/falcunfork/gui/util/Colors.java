package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.util;

public enum Colors {
	BLACK(0xFF000000), WHITE(0xFFFFFFFF), RED(0xFFFF0000), YELLOW(0xFFFFFF00), GREEN(0xFF00FF00), CYAN(0xFF00FFFF), PINK(0xFFFF00FF), BLUE(0xFF0000FF);

	private final int color;

	Colors(int color) {
		this.color = color;
	}

	public int getIntColor() {
		return this.color;
	}
}