package net.mattbenson.modules.types.fpssettings.cruches;

import net.minecraft.client.gui.FontRenderer;

public class OptifineHook {
	public float getCharWidth(FontRenderer renderer, char c) {
		if (renderer == null) {
			return 0;
		}

		return renderer.getCharWidth(c);
	}

	public float getOptifineBoldOffset(FontRenderer renderer) {
		return 1.0F;
	}
}