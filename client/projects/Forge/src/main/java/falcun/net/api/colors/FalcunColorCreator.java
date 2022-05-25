package falcun.net.api.colors;

import java.awt.*;

public class FalcunColorCreator {

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
		this.a = a;
		this.r = r;
		this.g = g;
		this.b = b;

	}

	public FalcunColorCreator(float a, int r, int g, int b) {
		this(r, g, b);
		this.a = (int) (255 * a);
	}

	public FalcunColorCreator(float a, float r, float g, float b) {
		this.a = (int) (255 * a);
		this.r = (int) (255 * r);
		this.g = (int) (255 * g);
		this.b = (int) (255 * b);
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
		int a = this.a << 24;
		int r = this.r << 16;
		int g = this.g << 8;
		int b = this.b;
		return new FalcunColor(a | r | g | b);
	}

}
