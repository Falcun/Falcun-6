package falcun.xyz.dev.boredhuman.dancore.falcunfork.util;

public class ColorUtil {

	public static float getAlpha(int color) {
		return ((color >> 24) & 0xFF) / 255f;
	}

	public static float getRed(int color) {
		return ((color >> 16) & 0xFF) / 255f;
	}

	public static float getGreen(int color) {
		return ((color >> 8) & 0xFF) / 255f;
	}

	public static float getBlue(int color) {
		return (color & 0xFF) / 255f;
	}

	public static float[] getColorComponents(int color) {
		return new float[]{ColorUtil.getRed(color), ColorUtil.getGreen(color), ColorUtil.getBlue(color), ColorUtil.getAlpha(color)};
	}
}