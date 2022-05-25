package falcun.net.api.colors;

import java.awt.*;

public final class FalcunColorCreator {

	private int a, r, g, b;

	public FalcunColorCreator() {
		this.a = 255;
		this.r = 255;
		this.g = 255;
		this.b = 255;
	}

	public FalcunColorCreator(Color color) {
		this.a = color.getAlpha();
		this.r = color.getRed();
		this.g = color.getGreen();
		this.b = color.getBlue();
	}

	public FalcunColorCreator(int color) {
		setColor(color);
	}

	public FalcunColorCreator(int r, int g, int b) {
		this(255, r, g, b);
	}

	public FalcunColorCreator(int a, int r, int g, int b) {
		this.a = validate(a);
		this.r = validate(r);
		this.g = validate(g);
		this.b = validate(b);

	}

	public FalcunColorCreator(float a, int r, int g, int b) {
		this(r, g, b);
		this.a = validate(255, a);
	}

	private static int validate(int i) {
		return Math.max(0, Math.min(i, 255));
	}

	private static int validate(int i, float f) {
		return (int) Math.max(0, Math.min(i * f, 255));
	}


	public FalcunColorCreator(float a, float r, float g, float b) {
		this.a = validate(255, a);
		this.r = validate(255, r);
		this.g = validate(255, g);
		this.b = validate(255, b);
	}

	public FalcunColorCreator(float r, float g, float b) {
		this(1f, r, g, b);
	}

	public FalcunColorCreator setAlpha(Number val) {
		if (val instanceof Double) {
			this.a = (int) (255 * val.doubleValue());
		} else if (val instanceof Float) {
			this.a = (int) (255 * val.floatValue());
		} else if (val instanceof Byte) {
			this.a = (int) val.byteValue() / (int) Byte.MAX_VALUE;
		} else if (val instanceof Short) {
			this.a = val.shortValue();
		} else if (val instanceof Long) {
			this.a = (int) val.longValue();
		} else if (val instanceof Integer) {
			this.a = val.intValue();
		}
		return this;
	}

	public FalcunColorCreator setRed(Number val) {
		if (val instanceof Double) {
			this.r = (int) (255 * val.doubleValue());
		} else if (val instanceof Float) {
			this.r = (int) (255 * val.floatValue());
		} else if (val instanceof Byte) {
			this.r = (int) val.byteValue() / (int) Byte.MAX_VALUE;
		} else if (val instanceof Short) {
			this.r = val.shortValue();
		} else if (val instanceof Long) {
			this.r = (int) val.longValue();
		} else if (val instanceof Integer) {
			this.r = val.intValue();
		}
		return this;
	}

	public FalcunColorCreator setGreen(Number val) {
		if (val instanceof Double) {
			this.g = (int) (255 * val.doubleValue());
		} else if (val instanceof Float) {
			this.g = (int) (255 * val.floatValue());
		} else if (val instanceof Byte) {
			this.g = (int) val.byteValue() / (int) Byte.MAX_VALUE;
		} else if (val instanceof Short) {
			this.g = val.shortValue();
		} else if (val instanceof Long) {
			this.g = (int) val.longValue();
		} else if (val instanceof Integer) {
			this.g = val.intValue();
		}
		return this;
	}

	public FalcunColorCreator setBlue(Number val) {
		if (val instanceof Double) {
			this.b = (int) (255 * val.doubleValue());
		} else if (val instanceof Float) {
			this.b = (int) (255 * val.floatValue());
		} else if (val instanceof Byte) {
			this.b = (int) val.byteValue() / (int) Byte.MAX_VALUE;
		} else if (val instanceof Short) {
			this.b = val.shortValue();
		} else if (val instanceof Long) {
			this.b = (int) val.longValue();
		} else if (val instanceof Integer) {
			this.b = val.intValue();
		}
		return this;
	}

	public FalcunColorCreator setColor(int color) {
		this.a = 0xFF & (color >> 24);
		this.r = 0xFF & (color >> 16);
		this.g = 0xFF & (color >> 8);
		this.b = 0xFF & color;
		return this;
	}

	public FalcunColor getColor() {
		return new FalcunColor(getIntColor());
	}

	public int getIntColor() {
		int a = this.a << 24;
		int r = this.r << 16;
		int g = this.g << 8;
		int b = this.b;
		return a | r | g | b;
	}

	public static int ARGBtoBGRA(int color) {
		return FalcunColor.getBGRA(color);
	}

	public static int RGBtoBGRA(int color) {
		return ARGBtoBGRA(color);
	}

	public static int HSBAtoBGRA(float hh, float s, float bb, int a) {
		int r = 0, g = 0, b = 0;
		if (s == 0) {
			r = g = b = (int) (bb * 255.0f + 0.5f);
		} else {
			float h = (hh - (float) Math.floor(hh)) * 6.0f;
			float f = h - (float) java.lang.Math.floor(h);
			float p = bb * (1.0f - s);
			float q = bb * (1.0f - s * f);
			float t = bb * (1.0f - (s * (1.0f - f)));
			switch ((int) h) {
				case 0:
					r = (int) (bb * 255.0f + 0.5f);
					g = (int) (t * 255.0f + 0.5f);
					b = (int) (p * 255.0f + 0.5f);
					break;
				case 1:
					r = (int) (q * 255.0f + 0.5f);
					g = (int) (bb * 255.0f + 0.5f);
					b = (int) (p * 255.0f + 0.5f);
					break;
				case 2:
					r = (int) (p * 255.0f + 0.5f);
					g = (int) (bb * 255.0f + 0.5f);
					b = (int) (t * 255.0f + 0.5f);
					break;
				case 3:
					r = (int) (p * 255.0f + 0.5f);
					g = (int) (q * 255.0f + 0.5f);
					b = (int) (bb * 255.0f + 0.5f);
					break;
				case 4:
					r = (int) (t * 255.0f + 0.5f);
					g = (int) (p * 255.0f + 0.5f);
					b = (int) (bb * 255.0f + 0.5f);
					break;
				case 5:
					r = (int) (bb * 255.0f + 0.5f);
					g = (int) (p * 255.0f + 0.5f);
					b = (int) (q * 255.0f + 0.5f);
					break;
			}
		}
		return ARGBtoBGRA((a << 24) | (r << 16) | (g << 8) | (b));
	}

	public static int HSBAtoBGRA(float hh, float s, float bb, float a) {
		return HSBAtoBGRA(hh, s, bb, Math.max(0, Math.min((int) (255 * a), 255)));
	}

	public static int HSBtoBGRA(float hh, float s, float bb) {
		return HSBAtoBGRA(hh, s, bb, 1f);
	}

}
