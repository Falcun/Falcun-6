package falcun.net.api.gui.util;

public enum Vertical {
	TOP(0), CENTER(1), BOTTOM(2);

	public final int alignment;

	Vertical(int align) {
		this.alignment = align;
	}

	public static Vertical getAlignment(Vertical v) {
		return v;
	}

	public static Vertical getAlignment(int i) {
		for (Vertical value : values()) {
			if (value.alignment == i) return value;
		}
		return null;
	}
}
