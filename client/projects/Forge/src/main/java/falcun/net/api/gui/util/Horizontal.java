package falcun.net.api.gui.util;

public enum Horizontal {
	LEFT(0), CENTER(1), RIGHT(2);

	public final int alignment;

	Horizontal(int align) {
		this.alignment = align;
	}

	public static Horizontal getAlignment(Horizontal v) {
		return v;
	}

	public static Horizontal getAlignment(int i) {
		for (Horizontal value : values()) {
			if (value.alignment == i) return value;
		}
		return null;
	}
}
