package falcun.xyz.dev.boredhuman.dancore.falcunfork.api.module;

import java.awt.Color;

public class ModColor {
	private float hue;
	private float saturation;
	private float value;
	private float alpha;
	private ColorStyle colorStyle = ColorStyle.STATIC;

	public ModColor(int color) {
		float[] hsb = new float[3];
		Color.RGBtoHSB((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, hsb);
		this.hue = hsb[0];
		this.saturation = hsb[1];
		this.value = hsb[2];
		this.alpha = ((color >> 24) & 0xFF) / 255F;
	}

	public int getIntColor() {
		int alpha = (int) (this.alpha * 255);
		int color = Color.HSBtoRGB(this.hue, this.saturation, this.value);
		return (alpha << 24) | (color & 0xFFFFFF);
	}

	public void setHue(float hue) {
		this.hue = hue;
	}

	public float getHue() {
		return this.hue;
	}

	public void setSaturation(float saturation) {
		this.saturation = saturation;
	}

	public float getSaturation() {
		return this.saturation;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public float getValue() {
		return this.value;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public float getAlpha() {
		return this.alpha;
	}

	public int getPlainColor() {
		return 0xFF000000 | Color.HSBtoRGB(this.hue, 1, 1);
	}

	public int getBGRA() {
		int alpha = (int) (this.alpha * 255);
		int rgb = Color.HSBtoRGB(this.hue, this.saturation, this.value);
		return (alpha << 24) | ((((rgb & 0xFF0000) >> 16) | (rgb & 0xFF00) | (rgb << 16)) & 0xFFFFFF);
	}

	public void setColorStyle(ColorStyle colorStyle) {
		this.colorStyle = colorStyle;
	}

	public ColorStyle getColorStyle() {
		return this.colorStyle;
	}

	public enum ColorStyle {
		STATIC, BREATH, CHROMA;
	}
}