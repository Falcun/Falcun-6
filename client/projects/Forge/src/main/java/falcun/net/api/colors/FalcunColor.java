package falcun.net.api.colors;

import java.io.Serializable;

public final class FalcunColor implements Serializable {
	private static final long serialVersionUID = 1L;

	public int color;

	public FalcunColor() {
		color = 0xFFFFFFFF;
	}

	public FalcunColor(int color) {
		this.color = color;
	}


	public int getBGRA() {
		return getBGRA(color);
	}

	public static int getBGRA(int color) {
		int a = 0xFF & (color >> 24);
		int r = 0xFF & (color >> 16);
		int g = 0xFF & (color >> 8);
		int b = 0xFF & color;
		int rgb = r << 16 | g << 8 | b;
		return a << 24 | ((rgb & 16711680) >> 16 | rgb & '\uff00' | rgb << 16) & 16777215;
	}

	public static FalcunColor i(int color) {
		return new FalcunColor(color);
	}

	public static FalcunColor of(int color) {
		return i(color);
	}
}
