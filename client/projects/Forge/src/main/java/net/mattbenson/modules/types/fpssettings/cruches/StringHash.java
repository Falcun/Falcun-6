package net.mattbenson.modules.types.fpssettings.cruches;

public class StringHash extends AbstractHash {
	public StringHash(String text, float red, float green, float blue, float alpha, boolean shadow) {
		super(new Object[] { text, Float.valueOf(red), Float.valueOf(green), Float.valueOf(blue), Float.valueOf(alpha),
				Boolean.valueOf(shadow) });
	}
}
